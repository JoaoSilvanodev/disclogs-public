package com.clogs.disclogs.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.FriendActivity
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeUiState(
    val isLoading: Boolean = true,
    val trendingAlbums: List<Album> = emptyList(),
    val errorMessage: String? = null,
    val friendsActivity: List<FriendActivity> = emptyList(),
    val friendAlbum: List<Album> = emptyList(),
    val communityActivity: List<CommunityActivity> = emptyList()
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        fetchHomeData()
        fetchFriendsActivity()
        fetchCommunityActivity()
    }

    private fun fetchCommunityActivity() {
        viewModelScope.launch {
            repository.getCommunityActivity()
                .onSuccess { activities ->
                    _uiState.update { it.copy(communityActivity = activities) }
                }
        }
    }


    fun fetchHomeData() {
        viewModelScope.launch {

            if (_uiState.value.trendingAlbums.isEmpty()) {
                _uiState.update { it.copy(isLoading = true) }
            }

            val result = repository.getTrendingAlbums()

            result.onSuccess { albums ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        trendingAlbums = albums
                    )
                }
            }.onFailure { erro ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = erro.message
                    )
                }
            }
        }
    }

    fun fetchFriendsActivity() {
        viewModelScope.launch {
            profileRepository.getFriendActivity()
                .onSuccess { activities ->
                    val friendsAlbum = coroutineScope {
                        activities
                            .distinctBy { it.albumId }
                            .map { activity ->
                                async { repository.getAlbumDetails(activity.albumId).getOrNull() }
                            }
                            .awaitAll()
                            .filterNotNull()
                    }

                    _uiState.update {
                        it.copy(friendsActivity = activities, friendAlbum = friendsAlbum)
                    }
                }
                .onFailure { erro ->
                    println("DISCLOGS DEBUG: Erro ao buscar amigos: ${erro.message}")
                }
        }
    }

}