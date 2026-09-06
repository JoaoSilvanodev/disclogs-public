package com.clogs.disclogs.di

import com.clogs.disclogs.BuildConfig
import com.clogs.disclogs.data.remote.discogs.DiscogsApiService
import com.clogs.disclogs.data.remote.lastfm.LastFmApiService
import com.clogs.disclogs.data.remote.spotify.SpotifyApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }


private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val SpotifyRetrofit = Retrofit.Builder()
    .baseUrl("https://api.spotify.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val spotifyApi: SpotifyApiService = SpotifyRetrofit.create(SpotifyApiService::class.java)


private val lastFmRetrofit = Retrofit.Builder()
    .baseUrl("https://ws.audioscrobbler.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val lastFmApi: LastFmApiService = lastFmRetrofit.create(LastFmApiService::class.java)

private val discogsRetrofit = Retrofit.Builder()
    .baseUrl("https://api.discogs.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val discogsApi: DiscogsApiService = discogsRetrofit.create(DiscogsApiService::class.java)
}