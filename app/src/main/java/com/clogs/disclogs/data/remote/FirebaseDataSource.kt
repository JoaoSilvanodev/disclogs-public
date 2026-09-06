package com.clogs.disclogs.data.remote

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton
import javax.inject.Inject

@Singleton // Garante que a instância é única em toda a aplicação
class FirebaseDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // =========================================================================
    // 1. AUTENTICAÇÃO (Firebase Auth)
    // =========================================================================

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

            saveProfile(newProfile)
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
            val existingProfile = getProfile(userId).getOrNull()

            if (existingProfile == null) {
                val newProfile = Profiles(
                    id = userId,
                    username = user?.displayName?.lowercase()?.replace(" ", "")
                        ?: "user_${userId.take(6)}",
                    fullName = user?.displayName ?: "Usuário ${userId.take(6)}",
                    avatarUrl = user?.photoUrl?.toString() ?: ""
                )
                saveProfile(newProfile)
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


    // =========================================================================
    // 2. PERFIS E SEGUIDORES (Firestore - Coleções "profiles" e "following")
    // =========================================================================

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


    // =========================================================================
    // 3. ÁLBUNS E ESTATÍSTICAS (Firestore - Coleção "albums")
    // =========================================================================

    /**
     * Salva ou atualiza os metadados do Álbum e incrementa a contagem semanal.
     */
    suspend fun saveOrUpdateAlbumMetadata(album: Album): Result<Unit> {
        return try {
            val docRef = firestore.collection("albums").document(album.id)
            val snapshot = docRef.get().await()

            val now = System.currentTimeMillis()
            val setWeekMills = 7 * 24 * 60 * 60 * 1000L

            val currentAlbum = snapshot.toObject(Album::class.java)

            val newWeeklyCount = if (currentAlbum != null) {
                if (now - currentAlbum.lastReviewedAt < setWeekMills) {
                    currentAlbum.weeklyCount + 1
                } else {
                    1
                }
            } else {
                1
            }
            val newTotalReviews = (currentAlbum?.totalReviews ?: 0) + 1

            val updatedAlbum = album.copy(
                weeklyCount = newWeeklyCount,
                totalReviews = newTotalReviews,
                lastReviewedAt = now
            )

            docRef.set(updatedAlbum).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtém os álbuns mais avaliados dos últimos 7 dias na coleção "albums".
     */
    suspend fun getTrendingAlbums(): Result<List<Album>> {
        return try {
            val weekBefore = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)

            val querySnapshot = firestore.collection("albums")
                .whereGreaterThan("lastReviewedAt", weekBefore)
                .orderBy("lastReviewedAt", Query.Direction.DESCENDING)
                .orderBy("weeklyCount", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val albums = querySnapshot.toObjects(Album::class.java)
            Result.success(albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtém a lista de álbuns da biblioteca pessoal do usuário.
     * TODO: Buscar as reviews do usuário e retornar os objetos de Álbum correspondentes.
     */
    suspend fun getLibraryAlbums(userId: String): Result<List<Album>> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            val albumIds = querySnapshot.toObjects(Review::class.java).map { it.albumId }
            val albums = firestore.collection("albums")
                .whereIn("id", albumIds)
                .get()
                .await()
                .toObjects(Album::class.java)
            Result.success(albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Calcula a nota média e o número total de avaliações de um álbum.
     * TODO: Consultar a coleção 'reviews' por album_id e calcular a média.
     */
    suspend fun getAverageRating(albumId: String): Result<Pair<Double, Int>> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("album_id", albumId)
                .get()
                .await()
            val reviews = querySnapshot.toObjects(Review::class.java)
            val totalRating = reviews.sumOf { it.rating ?: 0.0 } / reviews.size
            val totalReviews = reviews.size
            Result.success(totalRating to totalReviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtém a distribuição das notas (estatísticas e histograma) para um álbum.
     * TODO: Agrupar as notas de 0,5 a 5,0 no Firestore.
     */
    suspend fun getAlbumRatingStats(albumId: String): Result<RatingStats> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("album_id", albumId)
                .get()
                .await()
            val reviews = querySnapshot.toObjects(Review::class.java)
            val distribution = IntArray(10)
            reviews.forEach { review ->
                val rating = review.rating ?: 0.0
                distribution[(rating * 2).toInt()]++
            }
            Result.success(RatingStats(distribution = distribution.toList()))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    // =========================================================================
    // 4. REVIEWS, LIKES E ATIVIDADES (Firestore - Coleções "reviews", "review_likes")
    // =========================================================================

    /**
     * Busca a avaliação (Review) de um usuário para um álbum específico.
     */
    suspend fun getUserInteraction(userId: String, albumId: String): Result<Review?> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("album_id", albumId)
                .get()
                .await()

            val review = querySnapshot.documents.firstOrNull()?.toObject(Review::class.java)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Salva ou atualiza uma Review na coleção "reviews".
     */
    suspend fun saveInteraction(review: Review): Result<Unit> {
        return try {
            val docId = review.id?.toString() ?: firestore.collection("reviews").document().id
            val reviewToSave = review.copy(id = docId.toLongOrNull() ?: System.currentTimeMillis())

            firestore.collection("reviews").document(docId).set(reviewToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca todas as reviews cadastradas para um determinado álbum.
     */
    suspend fun getReviewsByAlbumId(albumId: String): Result<List<CommunityActivity>> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("album_id", albumId)
                .get()
                .await()

            val reviews = querySnapshot.toObjects(CommunityActivity::class.java)
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca uma Review pelo seu ID único de documento.
     * TODO: Buscar documento na coleção 'reviews' por ID.
     */
    suspend fun getReviewById(reviewId: String): Result<Review?> {
        return try {
            val doc = firestore.collection("reviews").document(reviewId).get().await()
            val review = doc.toObject(Review::class.java)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Remove uma Review da coleção "reviews" pelo ID.
     */
    suspend fun deleteReview(reviewId: Long): Result<Unit> {
        return try {
            firestore.collection("reviews")
                .document(reviewId.toString())
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retorna a contagem total de reviews do usuário logado.
     */
    suspend fun getReviewsCount(): Result<Int> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            Result.success(querySnapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adiciona uma curtida na coleção "review_likes" e incrementa o contador da review.
     * TODO: Criar o documento de like e atualizar o contador likesCount na review.
     */
    suspend fun likeReview(reviewId: Long): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val newLike = mapOf(
                "user_id" to userId,
                "review_id" to reviewId
            )
            firestore.collection("review_likes").add(newLike).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Remove uma curtida da coleção "review_likes" e decrementa o contador da review.
     * TODO: Deletar o documento de igualdade user_id/review_id e atualizar likesCount.
     */
    suspend fun unlikeReview(reviewId: Long): Result<Unit> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val querySnapshot = firestore.collection("review_likes")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("review_id", reviewId)
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

    /**
     * Busca o objeto de CommunityActivity por ID da review.
     * TODO: Carregar dados da review e os dados do autor (profile) correspondente.
     */
    suspend fun getCommunityActivityById(reviewId: Long): Result<CommunityActivity?> {
        return try {
            val doc = firestore.collection("community_activity").document(reviewId.toString()).get().await()
            val activity = doc.toObject(CommunityActivity::class.java)
            Result.success(activity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retorna o feed de atividades recentes dos amigos que o usuário logado segue.
     */
    suspend fun getFriendActivity(): Result<List<CommunityActivity>> {
        return try {
            val userId = getCurrentUser() ?: throw Exception("Usuário não encontrado")
            val querySnapshot = firestore.collection("community_activity")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            val activity = querySnapshot.toObjects(CommunityActivity::class.java)
            Result.success(activity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}