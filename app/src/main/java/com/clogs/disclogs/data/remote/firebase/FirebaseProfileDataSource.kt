package com.clogs.disclogs.data.remote.firebase

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseProfileDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Salva ou atualiza os dados de um perfil na coleção "profiles".
     */
    suspend fun saveProfile(profile: Profiles): Result<Unit> {
        return try {
            firestore.collection("profiles").document(profile.id).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca os dados do perfil de um usuário pelo ID.
     */
    suspend fun getProfile(userId: String): Result<Profiles?> {
        return try {
            val doc = firestore.collection("profiles").document(userId).get().await()
            val profile = doc.toObject(Profiles::class.java)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<Profiles> {
        return try {
            val profile = getProfile(userId).getOrNull() ?: throw Exception("Perfil não encontrado")
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualiza dados parciais do perfil do usuário logado.
     */
    suspend fun updateProfile(
        fullName: String?,
        username: String?,
        top4: List<Album>?
    ): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val profile = getProfile(userId).getOrNull() ?: throw Exception("Perfil não encontrado")

            val updatedProfile = profile.copy(
                fullName = fullName ?: profile.fullName,
                username = username ?: profile.username,
                top4 = top4 ?: profile.top4
            )
            saveProfile(updatedProfile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualiza o token FCM de notificações no perfil do usuário logado.
     */
    suspend fun updateFcmToken(token: String): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val profile = getProfile(userId).getOrNull() ?: throw Exception("Perfil não encontrado")
            val updatedProfile = profile.copy(fcmToken = token)
            saveProfile(updatedProfile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca usuários no Firestore pesquisando pelo nome de usuário.
     */
    suspend fun searchUsers(query: String): Result<List<Profiles>> {
        return try {
            val querySnapshot = firestore.collection("profiles")
                .whereGreaterThanOrEqualTo("username", query.lowercase())
                .whereLessThanOrEqualTo("username", query.lowercase() + "\uf8ff")
                .get()
                .await()
            val users = querySnapshot.toObjects(Profiles::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifica se o usuário logado segue o usuário alvo.
     */
    suspend fun checkIfFollowing(targetUserId: String): Result<Boolean> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val querySnapshot = firestore.collection("following")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("following_id", targetUserId)
                .get()
                .await()

            Result.success(querySnapshot.size() > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adiciona uma relação de seguir na coleção "following".
     */
    suspend fun followUser(targetUserId: String): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val newFollowing = mapOf(
                "user_id" to userId,
                "following_id" to targetUserId
            )
            firestore.collection("following").add(newFollowing).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Remove a relação de seguir da coleção "following".
     */
    suspend fun unfollowUser(targetUserId: String): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val querySnapshot = firestore.collection("following")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("following_id", targetUserId)
                .get()
                .await()

            for (doc in querySnapshot.documents) {
                doc.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
