package com.clogs.disclogs.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.RatingStats
import com.clogs.disclogs.data.model.Review
import com.clogs.disclogs.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


data class AlbumDetailUiState(
    // spotify
    val albumId: String = "",
    val isLoading: Boolean = true,
    val albumTitle: String = "",
    val artistName: String = "",
    val coverUrl: String = "",
    val releaseYear: String = "",
    val totalTracks: String = "",

    // Self stats
    val userRating: Double = 0.0,
    var isFavorite: Boolean = false,
    var isLogged: Boolean = false,
    val errorMessage: String? = null,
    val inLibrary: Boolean = false,
    val reviewId: String? = null,
    val saveSuccess: Boolean = false,
    val avgRating: Double = 0.0,
    val countRating: Int = 0,
    val ratingStats: RatingStats = RatingStats(),
    val albumReviews: List<CommunityActivity> = emptyList(),
    val isReviewsLoading: Boolean = false,

    // lastFm
    val wikiSummary: String? = null,
    val genre: List<String> = emptyList(),
    val lastFmUrl: String? = null,

    // Discogs
    val recordLabel: String? = null,
    val catalogNumber: String? = null,
    val physicalFormat: String? = null
)

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(private val repository: AlbumRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun loadAlbumDetail(albumId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, albumId = albumId) }

            val spotifyResult = repository.getAlbumDetails(albumId)
            val reviewResult = repository.getUserReview(albumId)
            val statsResult = repository.getAlbumRatingStats(albumId)

            val existingReview = reviewResult.getOrNull()
            val stats = statsResult.getOrNull() ?: RatingStats()

            val formatAverage = (stats.average * 10.0).roundToInt() / 10.0

            spotifyResult.onSuccess { album ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        albumTitle = album.title,
                        artistName = album.artist,
                        coverUrl = album.coverUrl,
                        releaseYear = album.releaseYear,
                        totalTracks = album.totalTracks,
                        inLibrary = existingReview != null,
                        userRating = existingReview?.rating ?: 0.0,
                        isFavorite = existingReview?.isFavorite ?: false,
                        avgRating = formatAverage,
                        countRating = stats.totCount,
                        ratingStats = stats,
                        wikiSummary = album.wikiSummary,
                        genre = album.genres,
                        lastFmUrl = album.lastFmUrl,
                        recordLabel = album.recordLabel,
                        catalogNumber = album.catalogNumber,
                        physicalFormat = album.physicalFormat
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    println("ERRO NA FUNÇÃO loadAlbumDetail status: ${exception.message}")
                    it.copy(isLoading = false)
                }
            }
        }
        fetchAlbumReviews(albumId)
    }

    fun fetchAlbumReviews(albumId: String) {
        viewModelScope.launch {
            repository.getReviewsByAlbum(albumId)
                .onSuccess { reviews ->
                    _uiState.update { it.copy(albumReviews = reviews) }
                }
        }

    }

    fun saveReview(
        rating: Double,
        text: String,
        diaryDateMillis: Long?,
        precision: String,
        favorite: Boolean
    ) {
        viewModelScope.launch {
            val userId = runCatching { repository.getCurrentUserId() }.getOrNull()
            if (userId == null) {
                _uiState.update { it.copy(errorMessage = "Você precisa estar logado para avaliar.") }
                return@launch
            }

            val isLoggedToDiary = diaryDateMillis != null

            // montar um objeto album a partir do uistate dos detalhes e colocar esse album completo na resposta da review

            val album = Album(
                id = _uiState.value.albumId,
                title = _uiState.value.albumTitle,
                artist = _uiState.value.artistName,
                coverUrl = _uiState.value.coverUrl,
                releaseYear = _uiState.value.releaseYear,
                totalTracks = _uiState.value.totalTracks,
                userRating = _uiState.value.userRating,
                wikiSummary = _uiState.value.wikiSummary,
                genres = _uiState.value.genre,
                recordLabel = _uiState.value.recordLabel,
                catalogNumber = _uiState.value.catalogNumber,
                physicalFormat = _uiState.value.physicalFormat,
                averageRating = _uiState.value.avgRating,
                totalRating = _uiState.value.countRating.toDouble(),
                weeklyCount = 0,
                totalReviews = _uiState.value.countRating,
                lastReviewedAt = System.currentTimeMillis()
            )


            val newReview = Review(
                userId = userId,
                albumId = album.id,
                albumTitle = album.title,
                coverUrl = album.coverUrl,
                rating = rating,
                comment = text,
                isFavorite = favorite,
                isLogged = isLoggedToDiary,
                listenDate = diaryDateMillis,
                datePrecision = precision
            )



            repository.saveUserReview(newReview)
        }
    }
}