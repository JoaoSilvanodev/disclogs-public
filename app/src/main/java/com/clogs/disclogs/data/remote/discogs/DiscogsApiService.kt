package com.clogs.disclogs.data.remote.discogs

import com.clogs.disclogs.data.model.DiscogsSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DiscogsApiService {

    @GET("database/search")
    suspend fun searchMasterRelease(
        @Header("Authorization") authHeader: String,
        @Header("User-Agent") userAgent: String = "DisclogsApp/1.0",

        @Query("artist") artist: String,
        @Query("release_title") releaseTitle: String?,
        @Query("type") type: String = "master",
        @Query("per_page") perPage: Int = 3
    ): Response<DiscogsSearchResponse>

}