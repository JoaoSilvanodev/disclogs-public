package com.clogs.disclogs.data.remote.spotify

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


// Este arquivo é a ‘interface’ que o Retrofit vai ler para saber para onde mandar os dados.


interface SpotifyApiService {

    // Rota 1: Pegar o ‘Token’ de Acesso
    // O Spotify exige que seja um POST em formato de formulário (FormUrlEncoded)

    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun getToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): SpotifyTokenResponse


    // Rota 2: Buscar os Álbuns
    // Aqui usamos o GET padrão, passando o "Bearer Token" que pegamos na Rota 1
    @GET("v1/search")
    suspend fun searchAlbuns(
        @Header("Authorization") bearerToken: String,
        @Query("q") query: String,
        @Query("type") type: String = "album,artist,track"
    ): SpotifySearchResponses


    @GET("v1/albums/{id}")
    suspend fun getAlbumDetails(
        @Header("Authorization") bearerToken: String,
        @Path("id") albumId: String?
    ): SpotifyAlbumDto

    @GET("v1/artists/{id}")
    suspend fun getArtistDetails(
        @Header("Authorization") bearerToken: String,
        @Path("id") artistId: String
    ): SpotifyArtistItemDto


    @GET("v1/artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("Authorization") bearerToken: String,
        @Path("id") artistId: String,
        @Query("include_groups") includeGroups: String = "album",
        @Query("market") market: String = "BR",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): SpotifyAlbuns
}