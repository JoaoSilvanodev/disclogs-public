package com.clogs.disclogs.data.repository

import android.R.attr.order
import androidx.compose.ui.res.stringResource
import com.clogs.disclogs.R
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.FollowingId
import com.clogs.disclogs.data.model.FriendActivity
import com.clogs.disclogs.data.model.Profiles
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : ProfileRepository {


    override suspend fun getCurrentUser(): Result<Profiles> {
        return try {
            // 1. Descobre quem é o usuário logado no momento
            val user = supabase.auth.currentUserOrNull() ?: throw Exception("Nenhum usuário logado")

            // 2. Vai na tabela "profiles" e busca a linha onde o id == id do usuário
            val profile = supabase.postgrest["profiles"]
                .select {
                    filter {
                        eq("id", user.id)
                    }
                }.decodeSingle<Profiles>() // Transforma o JSON do banco na sua data class Profiles

            Result.success(profile)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(
        fullName: String?,
        username: String?,
        top4AlbumIds: List<Album>?
    ): Result<Unit> {
        return try {
            val user = supabase.auth.currentUserOrNull() ?: throw Exception("Usuário não logado")

            println("DEBUG: Atualizando perfil para o usuário ${user.id}")
            println("DEBUG: Dados - fullName: $fullName, username: $username, top4: ${top4AlbumIds?.size} álbuns")


            if (top4AlbumIds != null) {
                println("DEBUG: caiu no if (top4AlbumIds != null)")
                val updateRequest = Top4UpdateRequest(top4Albums = top4AlbumIds)

                supabase.postgrest["profiles"].update(updateRequest) {
                    println("DEBUG: Bloco update acionado supabase.postgrest[profiles].update")
                    filter { eq("id", user.id) }
                }
            }


            Result.success(Unit)

        } catch (e: Exception) {
            println("DEBUG: ERRO AO ATUALIZAR SUPABASE: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getReviewsCount(): Result<Int> {
        return try {
            val user = supabase.auth.currentUserOrNull() ?: throw Exception("Usuário não logado")

            val count = supabase.postgrest["reviews"]
                .select {
                    filter { eq("user_id", user.id) }
                    count(io.github.jan.supabase.postgrest.query.Count.EXACT)
                }.countOrNull()?.toInt() ?: 0

            Result.success(count)

        } catch (e: Exception) {
            println("DEBUG: erro ao buscar contagem de reviews: ${e.message}")
            Result.success(0)
        }
    }


    override suspend fun searchUsers(query: String): Result<List<Profiles>> {
        return withContext(Dispatchers.IO) {
            try {

                val users = supabase.postgrest["profiles"]
                    .select {
                        filter {
                            // 'ilike' ignora maiúsculas/minúsculas. O % funciona como um "coringa"
                            ilike("username", "%$query%")
                        }
                    }.decodeList<Profiles>()

                Result.success(users)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun checkIfFollowing(targetUserId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {

            try {
                val currentUserId = supabase.auth.currentUserOrNull()?.id
                    ?: return@withContext Result.failure(Exception("Usuário não logado"))

                val count = supabase.postgrest["follows"].select {
                    filter {
                        eq("follower_id", currentUserId)
                        eq("following_id", targetUserId)
                    }
                    head = true // Retorna só a quantidade, sem baixar dados (mais rápido)
                }.countOrNull()

                Result.success(count != null && count > 0)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun followUser(targetUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUserId = supabase.auth.currentUserOrNull()?.id
                    ?: return@withContext Result.failure(Exception("Usuário não logado"))

                val followRow = mapOf(
                    "follower_id" to currentUserId,
                    "following_id" to targetUserId
                )

                supabase.postgrest["follows"].insert(followRow)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun unfollowUser(targetUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUserId = supabase.auth.currentUserOrNull()?.id
                    ?: return@withContext Result.failure(Exception("Usuário não logado"))

                supabase.postgrest["follows"].delete {
                    filter {
                        eq("follower_id", currentUserId)
                        eq("following_id", targetUserId)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserProfile(userId: String): Result<Profiles> {
        return withContext(Dispatchers.IO) {
            try {
                val profile = supabase.postgrest["profiles"]
                    .select { filter { eq("id", userId) } }
                    .decodeSingle<Profiles>()
                Result.success(profile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUserId = supabase.auth.currentUserOrNull()?.id
                    ?: return@withContext Result.failure(Exception("Usuário não logado"))
                println("DISCLOGS DEBUG: Tentando atualizar o token para o ID: $currentUserId")

                if (currentUserId == null) return@withContext Result.failure(Exception("ID nulo"))

                supabase.postgrest["profiles"]
                    .update({
                        set("fcm_token", token)
                    }) {
                        filter { eq("id", currentUserId) }
                    }

                Result.success(Unit)
            } catch (e: Exception) {
                println("DISCLOGS DEBUG: Erro real no update: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun getFriendActivity(): Result<List<FriendActivity>> {
        return withContext(Dispatchers.IO) {
            try {

                val currentUserId = supabase.auth.currentUserOrNull()?.id
                    ?: return@withContext Result.success(emptyList())

                val followingList = supabase.postgrest["follows"]
                    .select {
                        filter {
                            eq("follower_id", currentUserId)
                        }
                    }.decodeList<FollowingId>()

                val friendsIds = followingList.map { it.followingId }

                if (friendsIds.isEmpty()) {
                    return@withContext Result.success(emptyList())
                }

                val activities = supabase.postgrest["reviews"]
                    .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("*, profiles(username, avatar_url)")) {
                        filter { isIn("user_id", friendsIds) }
                        order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                        limit(10)
                    }.decodeList<FriendActivity>()

                Result.success(activities)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }
}