package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review

interface AlbumRepository {

    fun getCurrentUserId(): String?

    // Busca álbuns pelo nome retornando uma lista de álbuns
    suspend fun searchAlbums(query: String, tipo: String = "album"): Result<List<Album>>

    suspend fun saveUserReview(review: Review): Result<Unit>

    suspend fun getAlbumDetails(albumId: String?): Result<Album>

    suspend fun getTrendingAlbums(): Result<List<Album>>

    suspend fun getLibraryAlbums(): Result<List<Album>>

    suspend fun getUserReview(albumId: String): Result<Review?>

    suspend fun getAverageRating(albumId: String): Result<Pair<Double,Int>>

    suspend fun getAlbumRatingStats(albumId: String): Result<RatingStats>

    suspend fun getArtistDetails(artistId: String): Result<Album>

    suspend fun getArtistReleases(artistId: String): Result<List<Album>>

    suspend fun getCommunityActivity(): Result<List<CommunityActivity>>
    suspend fun getReviewsByAlbum(albumId: String): Result<List<CommunityActivity>>

}

