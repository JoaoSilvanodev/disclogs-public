package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review
import com.clogs.disclogs.data.model.ReviewRow
import com.clogs.disclogs.data.model.TrendingAlbums
import com.clogs.disclogs.data.remote.SupabaseDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(

    private val spotifyDataSource: SpotifyRemoteDataSource,
    private val supabaseDataSource: SupabaseDataSource,
    private val supabaseClient: SupabaseClient

) : AlbumRepository {

    override fun getCurrentUserId(): String? {
        return supabaseDataSource.getCurrentUserId()
    }

    override suspend fun searchAlbums(query: String, tipo: String): Result<List<Album>> {

        return spotifyDataSource.searchAlbuns(query, tipo)

    }

    override suspend fun saveUserReview(review: Review): Result<Unit> {
        return try {
            supabaseClient.from("reviews").insert(review)

            Result.success(Unit)
        } catch (e: Exception) {
            println("DISCLOGS DEBUG: Erro ao salvar review -> ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getAlbumDetails(albumId: String?): Result<Album> {
        return if (albumId != null) {
            spotifyDataSource.getAlbumDetails(albumId)
        } else {
            Result.failure(Exception("Album ID is null"))
        }
    }

    // método para obter os albums em alta
    override suspend fun getTrendingAlbums(): Result<List<Album>> {
        return try {
            // vai no supabase e pega os álbuns mais avaliados da tabela trending álbuns
            val topResults =
                supabaseClient.postgrest["trending_albums"].select().decodeList<TrendingAlbums>()

            if (topResults.isEmpty()) {
                return Result.success(emptyList())
            }

            // extrai so os ‘IDs’ numa lista de ‘strings’
            val albumIds = topResults.map { it.albumId }

            val albumList = coroutineScope {
                albumIds.map { id ->
                    async { spotifyDataSource.getAlbumDetails(id).getOrNull() }
                }
                    .awaitAll()
                    .filterNotNull()
            }
            Result.success(albumList)

        } catch (e: Exception) {
            println("DEBUG: Erro ao buscar albums em alta - ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getLibraryAlbums(): Result<List<Album>> {
        // Abre o bloco try para capturar qualquer erro de rede ou autenticação
        return try {

            // Chama a função existente do repositório para obter o ‘ID’ da sessão, se o retorno for nulo lança o erro.
            val userId = getCurrentUserId() ?: throw Exception("Usuário não encontrado")

            // Faz a chamada ao Supabase na tabela "reviews"
            val reviewRows = supabaseClient.postgrest["reviews"]
                // Inicia o processo de leitura (SELECT)
                .select {
                    filter {
                        // Filtra o banco comparando a coluna "user_id" com a sua variável 'userId'
                        eq("user_id", userId)
                    }
                }
                // Converte a resposta JSON numa lista de objetos do tipo ReviewRow
                .decodeList<Review>()

            if (reviewRows.isEmpty()) {
                return Result.success(emptyList())
            }

            val albunsReais = coroutineScope {
                reviewRows.map { review ->
                    async {
                        spotifyDataSource.getAlbumDetails(review.albumId).getOrNull()
                            ?.copy(userRating = review.rating ?: 0.0)
                    }
                } // Aguarda todas as buscas simultâneas finalizarem
                    .awaitAll()
                    // Limpa a lista removendo os álbuns que retornaram nulo (que deram erro no Spotify)
                    .filterNotNull()
            }
            // Devolve a lista final envelopada como sucesso
            Result.success(albunsReais)

            // Captura exceções lançadas no bloco try (incluindo o throw do usuário nulo)
        } catch (e: Exception) {
            println("DEBUG: Erro ao buscar Biblioteca - ${e.message}")
            // Retorna o pacote de erro para a ViewModel tratar
            Result.failure(e)
        }
    }

    override suspend fun getUserReview(albumId: String): Result<Review?> {
        return try {
            val userId = getCurrentUserId() ?: return Result.success(null)

            val reviews = supabaseClient.postgrest["reviews"].select {
                    filter {
                        eq("user_id", userId)
                        eq("album_id", albumId)
                    }
                    order(
                        "created_at",
                        order = io.github.jan.supabase.postgrest.query.Order.DESCENDING
                    )
                    limit(1)
                }.decodeList<Review>()

            Result.success(reviews.firstOrNull())
        } catch (e: Exception) {
            println("DEBUG: Erro ao checar review existente - ${e.message}")
            Result.success(null)
        }
    }

    override suspend fun getAverageRating(albumId: String): Result<Pair<Double, Int>> {
        return try {

            val ratings = supabaseClient.postgrest["reviews"].select {
                    filter {
                        eq("album_id", albumId)
                    }
                }.decodeList<RatingRow>()

            if (ratings.isEmpty()) {
                return Result.success(Pair(0.0, 0))
            }

            val average = ratings.map { it.rating }.average()
            val count = ratings.size

            Result.success(Pair(average, count))
        } catch (e: Exception) {
            println("DEBUG: Erro ao buscar média - ${e.message}")
            Result.success(Pair(0.0, 0))
        }
    }

    override suspend fun getAlbumRatingStats(albumId: String): Result<RatingStats> {
        return try {

            val ratings = supabaseClient.postgrest["reviews"].select {
                    filter { eq("album_id", albumId) }
                }.decodeList<RatingRow>()

            if (ratings.isEmpty()) {
                return Result.success(RatingStats())
            }
            val average = ratings.map { it.rating }.average()
            val count = ratings.size

            val distributionMap = ratings.groupingBy { it.rating.toFloat() }.eachCount()

            // Cria uma lista de 10 posições, do 0,5 ao 5,0 (passo de 0,5)
            val distributionArray = (1..10).map { i ->
                val startValue = i / 2f
                distributionMap[startValue] ?: 0
            }

            Result.success(
                RatingStats(
                    average = average, totCount = count, distribution = distributionArray
                )
            )

        } catch (e: Exception) {
            println("DEBUG: Erro ao buscar stats do álbum - ${e.message}")
            Result.success(RatingStats())
        }
    }

    override suspend fun getArtistDetails(artistId: String): Result<Album> {
        return spotifyDataSource.getArtistDetails(artistId)
    }

    override suspend fun getArtistReleases(artistId: String): Result<List<Album>> {
        return spotifyDataSource.getArtistReleases(artistId)
    }

    // cuida das reviews mais votadas da homeScreen
    override suspend fun getCommunityActivity(): Result<List<CommunityActivity>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = supabaseClient.postgrest["reviews"].select(
                        columns = io.github.jan.supabase.postgrest.query.Columns.raw(
                            "*, profiles(username, full_name, avatar_url)"
                        )
                    ) {
                        // Ordenar pelas mais curtidas primeiro
                        order(
                            "likes_count", io.github.jan.supabase.postgrest.query.Order.DESCENDING
                        )
                        // Critério de desempate: as mais recentes
                        order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                        limit(10)
                    }.decodeList<CommunityActivity>()
                Result.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    // cuida das reviews mais votadas da detailScreen
    override suspend fun getReviewsByAlbum(albumId: String): Result<List<CommunityActivity>> {
        return try {
            val result = supabaseClient.postgrest["reviews"].select(
                    columns = io.github.jan.supabase.postgrest.query.Columns.raw(
                        "*, profiles!user_id(username, full_name, avatar_url)"
                    )
                ) {
                    filter { eq("album_id", albumId) }
                    // Ordenar pelas mais curtidas primeiro
                    order("likes_count", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)

                }.decodeList<CommunityActivity>()
            println("DISCLOGS DEBUG: getReviewsByAlbum - sucesso! Encontrou ${result.size} reviews")
            Result.success(result)

        } catch (e: io.github.jan.supabase.postgrest.exception.PostgrestRestException) {
            println("DISCLOGS ERRO MESSAGE: ${e.message}")
            println("DISCLOGS ERRO DETAILS: ${e.details}")
            println("DISCLOGS ERRO HINT: ${e.hint}")
            Result.failure(e)
        } catch (e: Exception) {
            println("DISCLOGS ERRO GERAL: ${e.message}")
            Result.failure(e)
        }
    }
}


@Serializable
data class RatingRow(val rating: Double)