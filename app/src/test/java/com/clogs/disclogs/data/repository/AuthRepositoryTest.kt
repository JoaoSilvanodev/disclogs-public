package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthRepositoryImplTest {
    // 1. Criamos os dublês
    private val supabase = mockk<SupabaseClient>(relaxed = true)
    private val mockAuth = mockk<Auth>(relaxed = true) // Dublê da Autenticação
    private val mockUser = mockk<UserInfo>(relaxed = true) // Dublê do Usuário

    private val repository = AuthRepositoryImpl(supabase)

    @Test
    fun `ao atualizar o Top 4, deve chamar o postgrest com os dados corretos`() = runTest {

        // --- PREPARAÇÃO (GIVEN) ---
        // "Ensinando" os dublês o que responder:
        every { mockUser.id } returns "id-falso-123" // Meu ID é esse
        every { mockAuth.currentUserOrNull() } returns mockUser // Sim, tem alguém logado!

        // O Supabase-kt usa plugins, então ensinamos o mock a devolver nosso plugin falso
        every { supabase.auth } returns mockAuth

        val mockAlbums = listOf(
            Album(id = "1", title = "Dark Side", artist = "Pink Floyd", coverUrl = "")
        )

        // --- AÇÃO (WHEN) ---
        val result = repository.updateProfile(top4AlbumIds = mockAlbums)

        result.onFailure { erro ->
            println("🚨 MOTIVO DA FALHA: ${erro.message}")
            erro.printStackTrace()
        }

        // --- VERIFICAÇÃO (THEN) ---
        // 1. Agora, sim, o teste não vai bater na Exception e vai ser sucesso
        assert(result.isSuccess)

        // 2. Garante que o banco de dados foi acionado
        coVerify { supabase.postgrest["profiles"].update<Unit>(any()) }
    }
}
