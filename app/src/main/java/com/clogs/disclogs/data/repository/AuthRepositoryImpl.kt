package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.model.TrendingAlbums
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.Spotify
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.SSO
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        nomeCompleto: String,
        nick: String
    ): Result<Unit> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password

                // O pacote de metadados para o nosso Trigger do banco!
                data = buildJsonObject {
                    put("full_name", nomeCompleto)
                    put("username", nick)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithSpotify(): Result<Unit> {
        return try {
            supabase.auth.signInWith(Spotify)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(): Result<Unit> {
        return try {
            supabase.auth.signInWith(Google)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        // Checa se o Supabase tem uma sessão ativa salva no celular
        return supabase.auth.currentSessionOrNull() != null
    }

}

@Serializable
data class Top4UpdateRequest(
    @SerialName("top_4_albums") val top4Albums: List<Album>
)