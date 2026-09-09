package com.clogs.disclogs.data.repository


import android.util.Log
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseAlbumDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseAuthDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseReviewDataSource
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(

    private val spotifyDataSource: SpotifyRemoteDataSource,
    private val lastfmRemoteDataSource: LastfmRemoteDataSource,
    private val authDataSource: FirebaseAuthDataSource,
    private val albumDataSource: FirebaseAlbumDataSource,
    private val reviewDataSource: FirebaseReviewDataSource,
    private val discogsRemoteDataSource: DiscogsRemoteDataSource

) : AlbumRepository {

    override fun getCurrentUserId(): String {
        return authDataSource.getCurrentUser() ?: throw Exception("Usuário não encontrado")
    }

    override suspend fun searchAlbums(query: String, tipo: String): Result<List<Album>> {
        return try {
            spotifyDataSource.searchAlbuns(query, tipo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserReview(review: Review): Result<Unit> {
        return try {

            val album = albumDataSource.getAlbumById(review.albumId)
                ?: return Result.failure(Exception("Álbum não encontrado"))
            
            reviewDataSource.saveInteraction(review, album)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbumDetails(albumId: String): Result<Album> {
        return try {

            // 1. Tenta pegar do cache do Firebase primeiro
            val cachedAlbum = albumDataSource.getAlbumById(albumId)

            if (cachedAlbum != null) {
                Log.d("AlbumRepositoryImpl", "Álbuns do cache: $cachedAlbum")
                return Result.success(cachedAlbum)
            }
            // 2. Se não estava no Firebase, busca no Spotify
            Log.d("AlbumRepositoryImpl", "Álbuns não encontrados no cache")
            val spotifyResult = spotifyDataSource.getAlbumDetails(albumId)

            // 3. Se o Spotify respondeu com sucesso, salva no Firebase para cache
            if (spotifyResult.isSuccess) {
                val album = spotifyResult.getOrThrow()

                val lastFmInfo = lastfmRemoteDataSource.getAlbumDetail(
                    album.artist,
                    album.title
                ).getOrNull()

                val rawWiki = lastFmInfo?.wiki?.summary ?: ""
                val cleanWiki = rawWiki.substringBefore("<a").trim().ifBlank { null }
                val extractedUrl = if (rawWiki.contains("href=\"")) {
                    rawWiki.substringAfter("href=\"").substringBefore("\"")
                } else null

                // Extrai a resenha (wiki) e os gêneros (tags)
                val genres = lastFmInfo?.tags?.tag?.map { it.name } ?: emptyList()

                // Consulta o Discogs usando o Artista e Nome que vieram do Spotify
                val discogsResult = discogsRemoteDataSource.searchMasterRelease(
                    artist = album.artist,
                    releaseTitle = album.title
                ).getOrNull()

                // Pega o primeiro resultado retornado
                val discogsItem = discogsResult?.results?.firstOrNull()

                val label = discogsItem?.label?.firstOrNull()
                val format = discogsItem?.format?.firstOrNull()
                val catalog = discogsItem?.catno

                // Monta o álbum completo com o copy
                val fullAlbum = album.copy(
                    wikiSummary = cleanWiki,
                    genres = genres,
                    recordLabel = label,
                    physicalFormat = format,
                    catalogNumber = catalog,
                    lastFmUrl = extractedUrl
                )
                albumDataSource.saveAlbumLocaly(fullAlbum)
                return Result.success(fullAlbum)
            } else {
                return Result.failure(Exception("Erro ao buscar álbum no Spotify"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrendingAlbums(): Result<List<Album>> {
        return albumDataSource.getTrendingAlbums()
    }

    override suspend fun getLibraryAlbums(): Result<List<Album>> {
        return albumDataSource.getLibraryAlbums(getCurrentUserId())
    }

    override suspend fun getUserReview(albumId: String): Result<Review?> {
        val userId = getCurrentUserId()
        return reviewDataSource.getUserInteraction(userId, albumId)
    }

    override suspend fun getAverageRating(albumId: String): Result<Pair<Double, Int>> {
        return albumDataSource.getAverageRating(albumId)
    }

    override suspend fun getAlbumRatingStats(albumId: String): Result<RatingStats> {
        return albumDataSource.getAlbumRatingStats(albumId)
    }

    override suspend fun getArtistDetails(artistId: String): Result<Album> {
        return spotifyDataSource.getArtistDetails(artistId)
    }

    override suspend fun getArtistReleases(artistId: String): Result<List<Album>> {
        return spotifyDataSource.getArtistReleases(artistId)
    }

    override suspend fun getCommunityActivity(): Result<List<CommunityActivity>> {
        return reviewDataSource.getFriendActivity()
    }

    override suspend fun getReviewsByAlbum(albumId: String): Result<List<CommunityActivity>> {
        return reviewDataSource.getReviewsByAlbumId(albumId)
    }
}

