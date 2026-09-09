package com.clogs.disclogs.data.remote.lastfm

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {

    // definir os endpoints da api
    @GET("2.0/")
    suspend fun searchAlbum(
        @Query("method") method: String = "album.search",
        @Query("api_key") apiKey: String,
        @Query("album") albumQuery: String,
        @Query("format") format: String = "json"
    ): LastFmSearchResponse

    @GET("2.0/")
    suspend fun getAlbumInfo(
        @Query("method") method: String = "album.getInfo",
        @Query("api_key") apiKey: String,
        @Query("artist") artist: String,
        @Query("album") albumName: String,
        @Query("autocorrect") autoCorrect: Int = 1,
        @Query("format") format: String = "json"
    ): Response<LastFmAlbumInfoResponse>
}