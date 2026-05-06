package com.clogs.disclogs.ui.screens.settings

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.AuthRepository
import com.clogs.disclogs.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SettingsUiState(
    val isLoading: Boolean = true,
    val profile: Profiles? = null,
    val email: String = "",
    val top4Ids: List<Album> = emptyList(),
    val errorMessage: String? = null,
    val isSearching: Boolean = false,
    val searchResults: List<Album> = emptyList(),
    val searchError: String? = null
)



@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()


    // Para cancelar buscas antigas se o usuário digitar rápido
    private var searchJob: Job? = null

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val profileResult = profileRepository.getCurrentUser()

            profileResult.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profile = profile,
                        top4Ids = profile.top4 ?: emptyList()
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun updateTop4(newTop4Ids: List<Album>) {
        println("DEBUG: Chamando updateTop4 com ${newTop4Ids.size} itens")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = profileRepository.updateProfile(
                top4AlbumIds = newTop4Ids
            )

            result.onSuccess {
                println("botão de salvar top 4 clicado")
                _uiState.update { it.copy(isLoading = false, top4Ids = newTop4Ids) }
            }.onFailure { e ->
                _uiState.update {
                    println(e.message)
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao salvar Top 4"
                    )

                }
            }
        }
    }

    fun removeAlbumFromTop4(album: Album) {
        // Pega a lista atual, filtra tirando o álbum clicado, e atualiza o estado
        val currentList = _uiState.value.top4Ids.toMutableList()
        currentList.removeIf { it.id == album.id }

        _uiState.update { it.copy(top4Ids = currentList) }
    }

    fun searchAlbums(query: String) {

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        // Cancela a busca anterior se o usuário ainda estiver digitando (Debounce)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, searchError = null) }
            delay(500)

            val resultados = albumRepository.searchAlbums(query)

            resultados.onSuccess { listaAlbuns ->
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        searchResults = listaAlbuns
                    )
                }
            }.onFailure { erro ->

                _uiState.update {
                    it.copy(
                        isSearching = false,
                        searchError = "Erro ao buscar álbuns",
                        searchResults = emptyList()
                    )
                }
            }
        }
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchResults = emptyList(),
                isSearching = false,
                searchError = null
            )
        }
    }
}