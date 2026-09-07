package com.clogs.disclogs.data.remote.lastfm


import com.clogs.disclogs.data.model.Album

class LastfmRemoteDataSource(
    private val apiService: LastFmApiService,
    private val apiKey: String
) {
    suspend fun searchAlbums(query: String): Result<List<Album>> {
        return try {
            val response = apiService.searchAlbum(
                apiKey = apiKey,
                albumQuery = query
            )
            if (response.results?.albumMatches?.album != null) {
                val albums = response.results.albumMatches.album.map { albumDto ->
                    Album(
                        title = albumDto.name ?: "",
                        artist = albumDto.artist ?: "",
                        coverUrl = albumDto.image?.firstOrNull()?.url ?: ""
                    )
                }
                Result.success(albums)
            } else {
                Result.failure(Exception("Nenhum álbum encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumDetail(artist: String, albumName: String): Result<LastFmAlbumDetailsDto?> {
        return try {
            val response = apiService.getAlbumInfo(
                apiKey = apiKey,
                artist = artist,
                albumName = albumName
            )
            if (response.isSuccessful) {
                Result.success(response.body()?.album)
            } else {
                Result.failure(Exception("Erro ao buscar detalhes do álbum ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}