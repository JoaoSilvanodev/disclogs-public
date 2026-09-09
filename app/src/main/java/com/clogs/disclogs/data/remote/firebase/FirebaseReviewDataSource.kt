package com.clogs.disclogs.data.remote.firebase

import android.util.Log
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseReviewDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val albumDataSource: FirebaseAlbumDataSource
) {

    private fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }

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
    suspend fun saveInteraction(review: Review, album: Album): Result<Unit> {
        return try {
            // 1. Salva o documento da Review
            val docId = review.id?.toString() ?: firestore.collection("reviews").document().id
            firestore.collection("reviews").document(docId).set(review).await()

            Log.d("AlbumRepositoryImpl", "Review salva com sucesso datasource: $review")

            // 2. Atualiza os contadores do Álbum na coleção "albums"
            albumDataSource.incrementAlbumReviewCount(album)

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
            firestore.collection("reviews").document(reviewId.toString()).delete().await()
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
     */
    suspend fun getCommunityActivityById(reviewId: Long): Result<CommunityActivity?> {
        return try {
            val doc = firestore.collection("community_activity")
                .document(reviewId.toString())
                .get()
                .await()
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
