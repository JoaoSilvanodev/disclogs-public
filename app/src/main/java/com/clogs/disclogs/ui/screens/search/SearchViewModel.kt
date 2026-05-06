package com.clogs.disclogs.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.clogs.disclogs.data.repository.AlbumRepository
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    // Injeção de dependência do AlbumRepository
    private val repository: AlbumRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() // herda do Android a capacidade de sobreviver à rotação de tela
{


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var debounceSearch: Job? = null

    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        debounceSearch?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(albuns = emptyList(), users = emptyList()) }
            return
        }
        debounceSearch = viewModelScope.launch {
            delay(500)
            performSearch(query, _uiState.value.selectedFilter)
        }
    }

    fun onFilterChange(newFilter: String) {
        _uiState.update { it.copy(selectedFilter = newFilter) }
        // Se já tiver uma query digitada e a pessoa mudar de aba, pesquisa de novo!
        if (_uiState.value.searchQuery.isNotBlank()) {
            search(_uiState.value.searchQuery)
        }
    }

    private suspend fun performSearch(query: String, filter: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        if (filter.contains("Usuário", ignoreCase = true) || filter.contains(
                "User",
                ignoreCase = true
            )
        ) {

            // BATE NO SUPABASE
            val result = profileRepository.searchUsers(query)
            result.onSuccess { listUser ->
                _uiState.update { it.copy(users = listUser, isLoading = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message, isLoading = false) }
            }
        } else {
            // BATE NO SPOTIFY
            val result = repository.searchAlbums(query)
            result.onSuccess { listaCompleta ->
                _uiState.update { it.copy(albuns = listaCompleta, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = "Erro desconhecido ao buscar",
                        isLoading = false
                    )
                }
            }
        }
    }
}


