package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review

import com.clogs.disclogs.data.model.TrendingAlbums
import com.clogs.disclogs.data.remote.FirebaseDataSource
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import kotlinx.serialization.Serializable
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(

    private val spotifyDataSource: SpotifyRemoteDataSource,
    private val lastfmRemoteDataSource: LastfmRemoteDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val discogsRemoteDataSource: DiscogsRemoteDataSource

) : AlbumRepository {

    override fun getCurrentUserId(): String {
        return firebaseDataSource.getCurrentUser() ?: throw Exception("Usuário não encontrado")
    }

    override suspend fun searchAlbums(query: String, tipo: String): Result<List<Album>> {
        return spotifyDataSource.searchAlbuns(query, tipo)
    }

    override suspend fun saveUserReview(review: Review): Result<Unit> {
        return try {
            firebaseDataSource.saveInteraction(review)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbumDetails(albumId: String?): Result<Album> {
        return try {
            spotifyDataSource.getAlbumDetails(albumId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrendingAlbums(): Result<List<Album>> {
        return firebaseDataSource.getTrendingAlbums()
    }

    override suspend fun getLibraryAlbums(): Result<List<Album>> {
        return firebaseDataSource.getLibraryAlbums(getCurrentUserId())
    }

    override suspend fun getUserReview(albumId: String): Result<Review?> {
        val userId = getCurrentUserId()
        return firebaseDataSource.getUserInteraction(userId, albumId)
    }

    override suspend fun getAverageRating(albumId: String): Result<Pair<Double, Int>> {
        return firebaseDataSource.getAverageRating(albumId)
    }

    override suspend fun getAlbumRatingStats(albumId: String): Result<RatingStats> {
        return firebaseDataSource.getAlbumRatingStats(albumId)
    }

    override suspend fun getArtistDetails(artistId: String): Result<Album> {
        return spotifyDataSource.getArtistDetails(artistId)
    }

    override suspend fun getArtistReleases(artistId: String): Result<List<Album>> {
        return spotifyDataSource.getArtistReleases(artistId)
    }

    override suspend fun getCommunityActivity(): Result<List<CommunityActivity>> {
        return firebaseDataSource.getFriendActivity()
    }

    override suspend fun getReviewsByAlbum(albumId: String): Result<List<CommunityActivity>> {
        return firebaseDataSource.getReviewsByAlbumId(albumId)
    }
}


@Serializable
data class RatingRow(val rating: Double)