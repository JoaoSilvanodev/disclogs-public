package com.clogs.disclogs.data.remote.discogs

import com.clogs.disclogs.data.model.DiscogsSearchResponse

class DiscogsRemoteDataSource(
    private val apiService: DiscogsApiService,
    private val authToken: String
) {
    suspend fun searchMasterRelease(
        artist: String,
        releaseTitle: String?
    ): Result<DiscogsSearchResponse?> {
        return try {

            val response = apiService.searchMasterRelease(
                authHeader = "Discogs token=$authToken",
                artist = artist,
                releaseTitle = releaseTitle
            )

            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Erro api discogs: ${response.code()}"))
            }
        } catch (e: Exception) {
            println("DISCLOGS DEBUG FATAL: Erro ao converter JSON do Discogs -> ${e.message}")
            Result.failure(e)
        }
    }
}