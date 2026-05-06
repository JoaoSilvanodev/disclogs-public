package com.clogs.disclogs.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val albumId: String = "",
    val isLoading: Boolean = true,
    val albumTitle: String = "",
    val artistName: String = "",
    val coverUrl: String = "",
    val releaseYear: String = "",
    val totalTracks: String = "",
    val userRating: Double = 0.0,
    var isFavorite: Boolean = false,
    var isLogged: Boolean = false,
    val errorMessage: String? = null,
    val inLibrary: Boolean  = false,
    val reviewId: String? = null,
    val saveSuccess: Boolean = false,
    val avgRating: Double = 0.0,
    val countRating: Int = 0,
    val ratingStats: RatingStats = RatingStats(),
    val albumReviews: List<CommunityActivity> = emptyList(),
    val isReviewsLoading: Boolean = false
)

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(private val repository: AlbumRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun loadAlbumDetail(albumId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, albumId = albumId) }

            val spotifyResult = repository.getAlbumDetails(albumId)
            val supabaseResult = repository.getUserReview(albumId)
            val statsResult = repository.getAlbumRatingStats(albumId)

            val existingReview = supabaseResult.getOrNull()
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
                        ratingStats = stats
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
            val userId = repository.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(errorMessage = "Você precisa estar logado para avaliar." ) }
                return@launch
            }

            val isLoggedToDiary = diaryDateMillis != null

            val newReview = Review(
                albumId = _uiState.value.albumId,
                rating = rating,
                comment = text,
                isLogged = isLoggedToDiary,
                userId = userId,
                isFavorite = favorite,
                listenDate = diaryDateMillis
            )
            repository.saveUserReview(newReview)
        }
    }
}