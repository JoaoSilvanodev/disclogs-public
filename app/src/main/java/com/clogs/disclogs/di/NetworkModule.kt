package com.clogs.disclogs.di

import com.clogs.disclogs.BuildConfig
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

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.spotify.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val spotifyApi: SpotifyApiService = retrofit.create(SpotifyApiService::class.java)
}