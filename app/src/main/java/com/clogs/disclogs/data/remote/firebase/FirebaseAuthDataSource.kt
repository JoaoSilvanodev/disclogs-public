package com.clogs.disclogs.data.remote.firebase

import com.clogs.disclogs.data.model.Profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    /**
     * Retorna o ID único (UID) do usuário atualmente logado no dispositivo.
     */
    fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Faz o login com E-mail e Senha no Firebase Auth.
     */
    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw Exception("Usuário não encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cadastra um novo usuário no Firebase Auth e cria o perfil inicial no Firestore.
     */
    suspend fun signUp(email: String, password: String, username: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Usuário não encontrado")

            val newProfile = Profiles(
                id = userId,
                username = username
            )

            firestore.collection("profiles").document(newProfile.id).set(newProfile).await()
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Efetua o login com Google utilizando o idToken do Credential Manager.
     */
    suspend fun signWithGoogle(idToken: String): Result<String> {
        return try {
            val credentials = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credentials).await()
            val userId = authResult?.user?.uid ?: throw Exception("Usuário não encontrado")

            val user = authResult.user
            val doc = firestore.collection("profiles").document(userId).get().await()
            val existingProfile = doc.toObject(Profiles::class.java)

            if (existingProfile == null) {
                val newProfile = Profiles(
                    id = userId,
                    username = user?.displayName?.lowercase()?.replace(" ", "")
                        ?: "user_${userId.take(6)}",
                    fullName = user?.displayName ?: "Usuário ${userId.take(6)}",
                    avatarUrl = user?.photoUrl?.toString() ?: ""
                )
                firestore.collection("profiles").document(newProfile.id).set(newProfile).await()
            }
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Desloga o usuário da sessão do Firebase Auth.
     */
    fun signOut() {
        auth.signOut()
    }
}
