package com.clogs.disclogs.data.remote


import com.clogs.disclogs.data.model.Review
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class SupabaseDataSource(private val supabaseClient: SupabaseClient) {

    // autenticação

    fun getCurrentUserId(): String? {
        return supabaseClient.auth.currentUserOrNull()?.id
    }

    // métodos de interação

    suspend fun getUserInteraction(albumId: String): Review? {
        val userId = getCurrentUserId() ?: return null

        return try {

            supabaseClient.from("reviews")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("album_id", albumId)
                    }
                }
                .decodeSingleOrNull<Review>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveInteraction(interaction: Review) {
        try {
            supabaseClient.from("reviews").upsert(interaction)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


}