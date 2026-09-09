package com.clogs.disclogs.data.remote.firebase

import android.util.Log
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review
import com.clogs.disclogs.data.model.getCurrentWeekId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAlbumDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Busca álbuns pelo nome na coleção "albums".
     */
    suspend fun getAlbumByName(name: String): Result<Album> {
        return try {
            val querySnapshot = firestore.collection("albums")
                .whereEqualTo("title", name)
                .get()
                .await()

            val albums = querySnapshot.toObjects(Album::class.java)
            Result.success(albums.first())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
                if ((now - currentAlbum.lastReviewedAt) < setWeekMills) {
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
            val currentWeek = getCurrentWeekId()

            val querySnapshot = firestore.collection("albums")
                .whereEqualTo("currentWeekId", currentWeek)
                .orderBy("weeklyCount", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            var albums = querySnapshot.toObjects(Album::class.java)

            if (albums.isEmpty()) {
                val fallbackQuerySnapshot = firestore.collection("albums")
                    .orderBy("totalReviews", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()
                albums = fallbackQuerySnapshot.toObjects(Album::class.java)
            }

            Result.success(albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementAlbumReviewCount(album: Album): Result<Unit> {
        return try {

            val docRef = firestore.collection("albums").document(album.id)
            val snapshot = docRef.get().await()

            val currentWeek = getCurrentWeekId() // ID da semana atual
            val exitingAlbum =
                snapshot.toObject(Album::class.java) // cria um objeto Album com os dados do documento

            val newWeeklyCount =

                if (exitingAlbum != null && exitingAlbum.currentWeekId == currentWeek) {
                    exitingAlbum.weeklyCount + 1 // Incrementa a contagem semanal
                } else {
                    1
                }
            // Incrementa o total de avaliações do álbum e atualiza a semana atual
            val newTotalReviews = (exitingAlbum?.totalReviews ?: 0) + 1

            val updatedAlbum = (exitingAlbum ?: album).copy(
                weeklyCount = newWeeklyCount,
                totalReviews = newTotalReviews,
                currentWeekId = currentWeek,
                lastReviewedAt = System.currentTimeMillis()
            )

            docRef.set(updatedAlbum).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtém a lista de álbuns da biblioteca pessoal do usuário.
     */
    suspend fun getLibraryAlbums(userId: String): Result<List<Album>> {
        return try {
            val querySnapshot = firestore.collection("reviews")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val reviews = querySnapshot.toObjects(Review::class.java)
            val albums = reviews.mapNotNull { review ->
                getAlbumById(review.albumId)
            }

            Log.d("AlbumRepositoryImpl", "Álbuns da biblioteca: $albums")
            Result.success(albums)
        } catch (e: Exception) {
            Log.e("AlbumRepositoryImpl", "Erro ao buscar álbuns da biblioteca: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Calcula a nota média e o número total de avaliações de um álbum.
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

    /**
     * Salva uma cópia do álbum para a coleção albums locais para evitar consultas à API do Spotify.
     */
    suspend fun saveAlbumLocaly(album: Album): Result<Unit> {
        return try {
            val docRef = firestore.collection("albums").document(album.id)
            docRef.set(album).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumById(albumId: String): Album? {
        return try {
            val doc = firestore.collection("albums").document(albumId).get().await()
            doc.toObject(Album::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
