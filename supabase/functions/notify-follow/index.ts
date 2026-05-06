import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'
// Usamos a biblioteca oficial do Google direto do NPM no Deno!
import { GoogleAuth } from 'npm:google-auth-library'

serve(async (req) => {
  try {
    // 1. Recebe os dados do Gatilho (quem seguiu quem)
    const payload = await req.json()
    const followerId = payload.record.follower_id
    const followingId = payload.record.following_id

    // 2. Conecta no banco de dados para buscar os nomes e o token
    const supabaseClient = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY') ?? ''
    )

    // Busca o nome do Fã
    const { data: follower } = await supabaseClient
      .from('profiles')
      .select('username')
      .eq('id', followerId)
      .single()

    // Busca o token do Ídolo
    const { data: following } = await supabaseClient
      .from('profiles')
      .select('fcm_token')
      .eq('id', followingId)
      .single()

    const fcmToken = following?.fcm_token
    if (!fcmToken) {
      console.log("Usuário não tem FCM Token configurado.")
      return new Response(JSON.stringify({ message: 'Sem token' }), { status: 200 })
    }

    // 3. Puxa a chave do cofre (Secrets)
    const serviceAccountJson = Deno.env.get('FIREBASE_SERVICE_ACCOUNT')
    if (!serviceAccountJson) {
      throw new Error("Chave do Firebase não encontrada no cofre!")
    }

    // Converte o texto do cofre de volta para um objeto
    const serviceAccount = JSON.parse(serviceAccountJson)

    // 4. Gera o Token de Autorização do Google (O "Crachá")
    const auth = new GoogleAuth({
      credentials: {
        client_email: serviceAccount.client_email,
        private_key: serviceAccount.private_key,
      },
      scopes: ['https://www.googleapis.com/auth/firebase.messaging'],
    })
    const accessToken = await auth.getAccessToken()

    // 5. Monta a mensagem pro Firebase
    const fcmPayload = {
      message: {
        token: fcmToken,
        notification: {
          title: "Novo Seguidor!",
          body: `@${follower?.username} começou a te seguir no Disclogs.`,
        },
        data: {
          type: "NEW_FOLLOWER",
          follower_id: followerId
        }
      }
    }

    // 6. Dispara pro Firebase (Usando o project_id que veio direto da chave!)
    const res = await fetch(`https://fcm.googleapis.com/v1/projects/${serviceAccount.project_id}/messages:send`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(fcmPayload),
    })

    const fcmResult = await res.json()
    console.log("Resultado do FCM:", fcmResult)

    return new Response(JSON.stringify({ success: true, fcmResult }), { headers: { "Content-Type": "application/json" } })

  } catch (error) {
    console.error("Erro na Edge Function:", error)
    return new Response(JSON.stringify({ error: error.message }), { status: 500 })
  }
})