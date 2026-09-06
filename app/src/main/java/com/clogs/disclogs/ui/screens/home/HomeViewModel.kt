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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* data classe que segura a atividade
* de um usuário com os dados do album
* */
data class FriendListening(
    val activity: CommunityActivity,
    val album: Album
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val trendingAlbums: List<Album> = emptyList(),
    val errorMessage: String? = null,
    val friendsActivity: List<FriendActivity> = emptyList(),
    val friendListening: List<FriendListening> = emptyList(),
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
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            println("DISCLOGS DEBUG: Iniciando carregamento da Home...")

            try {
                // 1. Disparar buscas em paralelo
                val trendingDeferred = async { repository.getTrendingAlbums() }
                val friendsDeferred = async { profileRepository.getFriendActivity() }
                val communityDeferred = async { repository.getCommunityActivity() }

                // 2. Aguardar resultados
                val trendingResult = trendingDeferred.await()
                val friendResult = friendsDeferred.await()
                val communityResult = communityDeferred.await()

                // 3. Processar Trending
                trendingResult.onSuccess { albums ->
                    _uiState.update { it.copy(trendingAlbums = albums) }
                }.onFailure { e ->
                    println("DISCLOGS DEBUG: Erro ao buscar Trending: ${e.message}")
                }

                // 4. Processar Comunidade
                communityResult.onSuccess { activities ->
                    _uiState.update { it.copy(communityActivity = activities) }
                }.onFailure { e ->
                    println("DISCLOGS DEBUG: Erro ao buscar Community: ${e.message}")
                }

                // 5. Processar Amigos e buscar detalhes no Spotify
                friendResult.onSuccess { activities ->
                    println("DISCLOGS DEBUG: Atividades de amigos encontradas: ${activities.size}")
                    
                    val combinedActivity = activities.map { activity ->
                        async {
                            val album = repository.getAlbumDetails(
                                activity.albumId
                            )
                            album.getOrNull()?.let {
                                FriendListening(
                                    activity = activity,
                                    album = it
                                )
                            }
                        }
                    }.awaitAll().filterNotNull()

                    println("DISCLOGS DEBUG: Amigos processados com sucesso: ${combinedActivity.size}")
                    _uiState.update { it.copy(friendListening = combinedActivity) }

                }.onFailure { e ->
                    println("DISCLOGS DEBUG: Erro na query de amigos: ${e.message}")
                    e.printStackTrace()
                }

            } catch (e: Exception) {
                println("DISCLOGS DEBUG: Erro fatal no HomeViewModel: ${e.message}")
            } finally {
                // GARANTIA: O loading só desativa quando tudo termina
                _uiState.update { it.copy(isLoading = false) }
                println("DISCLOGS DEBUG: Carregamento finalizado.")
            }
        }
    }
}
