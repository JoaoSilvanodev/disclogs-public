package com.clogs.disclogs.data.remote.spotify

import android.util.Base64
import com.clogs.disclogs.BuildConfig
import com.clogs.disclogs.data.model.Album

class SpotifyRemoteDataSource(private val apiService: SpotifyApiService) {

    private var cachedToken: String? = null

    private suspend fun getValidToken(): String {
        cachedToken?.let { return it } // Se já temos um ‘token’ na memória usamos ele

        // Se não tem o 'token' é gerado em Base64 usando as variáveis seguras do BuildConfig
        val authString = "${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}"
        val encodedAuth = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)

        val tokenResponse = apiService.getToken(authHeader = "Basic $encodedAuth")

        cachedToken = tokenResponse.accessToken
        return tokenResponse.accessToken
    }

    suspend fun searchAlbuns(query: String, type: String): Result<List<Album>> {
        return try {
            val token = getValidToken()

            // mandando o type pro Retrofit
            val response = apiService.searchAlbuns(
                bearerToken = "Bearer $token",
                query = query,
                type = "album,artist,track"
            )

            val albunsMap = mutableListOf<Album>()

            response.artists?.items?.forEach { artist ->
                albunsMap.add(
                    Album(
                        id = artist.id,
                        title = artist.name,
                        artist = "Artista",
                        coverUrl = artist.images?.firstOrNull()?.url ?: "",
                        releaseYear = "",
                        totalTracks = "",
                        type = "artist"
                    )
                )
            }
            response.albuns?.itens?.forEach { album ->
                albunsMap.add(
                    Album(
                        id = album.id,
                        title = album.name,
                        artist = album.artists.firstOrNull()?.name ?: "",
                        coverUrl = album.images.firstOrNull()?.url ?: "",
                        releaseYear = album.year ?: "",
                        totalTracks = album.totalTracks.toString(),
                        type = "album"
                    )
                )
            }
            response.tracks?.items?.forEach { track ->
                albunsMap.add(
                    Album(
                        id = track.id,
                        title = track.name,
                        artist = track.artists.firstOrNull()?.name ?: "",
                        coverUrl = track.album.images.firstOrNull()?.url ?: "",
                        releaseYear = track.album.year ?: "",
                        totalTracks = "1",
                        type = "track"
                    )
                )
            }

            Result.success(albunsMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumDetails(albumId: String?): Result<Album> {
        return try {
            val token = getValidToken()

            val response = apiService.getAlbumDetails(
                bearerToken = "Bearer $token",
                albumId = albumId
            )

            val albumMap = Album(
                id = response.id,
                title = response.name,
                artist = response.artists.firstOrNull()?.name ?: "Artista Desconhecido",
                coverUrl = response.images.firstOrNull()?.url ?: "",
                releaseYear = response.year,
                totalTracks = response.totalTracks
            )

            Result.success(albumMap)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getArtistDetails(artistId: String): Result<Album> {
        return try {

            val token = getValidToken()
            val response = apiService.getArtistDetails(
                bearerToken = "Bearer $token",
                artistId = artistId
            )

            val artistInfo = Album(
                id = response.id,
                title = response.name, // Nome do artista
                artist = "Artista",
                coverUrl = response.images?.firstOrNull()?.url ?: "",
                // O Spotify devolve popularidade ou seguidores, você pode adaptar aqui se tiver nos DTOs
                totalTracks = "", // Deixe vazio
                releaseYear = "",
                type = "artist"
            )
            Result.success(artistInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getArtistReleases(artistId: String): Result<List<Album>> {
        return try {
            val token = getValidToken()

            val totalReleases = mutableListOf<Album>()
            var currentOffSet = 0
            var totAlbums = 1

            while (currentOffSet < totAlbums) {

                val response = apiService.getArtistAlbums(
                    bearerToken = "Bearer $token",
                    artistId = artistId,
                    offset = currentOffSet
                )
                totAlbums = response.total

                val chunk = response.itens.map { dto ->
                    Album(
                        id = dto.id,
                        title = dto.name,
                        artist = dto.artists.firstOrNull()?.name ?: "Unknown",
                        coverUrl = dto.images.firstOrNull()?.url ?: "",
                        releaseYear = dto.year?.take(4) ?: "",
                        totalTracks = dto.totalTracks.toString(),
                        type = dto.albumType ?: "album"
                    )
                }
                totalReleases.addAll(chunk)

                currentOffSet += 10
            }
            Result.success(totalReleases)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
