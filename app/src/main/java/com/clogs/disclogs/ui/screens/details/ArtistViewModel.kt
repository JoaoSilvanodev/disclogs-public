package com.clogs.disclogs.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Em ArtistViewModel.kt

data class ArtistUiState(
    val isLoading: Boolean = true,
    val artistId: String = "",
    val artistName: String = "",
    val artistImageUrl: String = "",
    val tags: String = "",
    val listeners: String = "",
    val bio: String = "",
    val allReleases: List<Album> = emptyList(),
    val displayedReleases: List<Album> = emptyList(),
    val currentFilter: ReleaseFilter = ReleaseFilter.ALL,
    val currentSort: SortOrder = SortOrder.NEWEST_FIRST,
    val errorMessage: String? = null
)

enum class ReleaseFilter { ALL, ALBUM, SINGLE }

enum class SortOrder {
    NEWEST_FIRST, OLDEST_FIRST, ALPHABETICAL
}


@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    fun loadArtist(artistId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, artistId = artistId) }

            try {
                // Busca os detalhes reais do Artista
                val artistResult = repository.getArtistDetails(artistId)
                val artistInfo = artistResult.getOrNull()

                // Busca a discografia real
                val releasesResult = repository.getArtistReleases(artistId)
                val releasesList = releasesResult.getOrNull() ?: emptyList()

                if (artistInfo != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            artistName = artistInfo.title, // Pegamos o nome adaptado
                            artistImageUrl = artistInfo.coverUrl,

                            // Mock temporário para os dados que o endpoint básico de artista não traz
                            tags = "ARTIST",
                            listeners = "Spotify Artist",
                            bio = "Música e discografia disponíveis no Spotify.",

                            allReleases = releasesList,
                            displayedReleases = releasesList // Inicialmente mostra tudo
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Erro ao carregar artista")
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    // LÓGICA DO FILTRO
    fun setFilter(filter: ReleaseFilter) {
        val filteredList = when (filter) {
            ReleaseFilter.ALL -> _uiState.value.allReleases

            // ignoreCase = true evita bugs caso a API mande 'Album', 'album' ou 'ALBUM'
            ReleaseFilter.ALBUM -> _uiState.value.allReleases.filter {
                it.type.equals("album", ignoreCase = true)
            }

            ReleaseFilter.SINGLE -> _uiState.value.allReleases.filter {
                it.type.equals("single", ignoreCase = true)
            }
        }

        _uiState.update {
            it.copy(currentFilter = filter, displayedReleases = filteredList)
        }
    }

    // função de Ordenação
    fun setOrder( sortOrder: SortOrder) {

        _uiState.update { state ->
            state.copy(
                currentSort = sortOrder,
                displayedReleases = applySorting(state.displayedReleases, sortOrder)
            )
        }
    }
    // Função auxiliar que faz a matemática da ordenação
    private fun applySorting(list: List<Album>, sortOrder: SortOrder): List<Album> {

        return when (sortOrder) {

            SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.releaseYear } // ordena pelo ano mais novo primeiro
            SortOrder.OLDEST_FIRST -> list.sortedBy { it.releaseYear } // ordena por ano crescente
            SortOrder.ALPHABETICAL -> list.sortedBy { it.title } // ordena por titulo
        }
    }
}
