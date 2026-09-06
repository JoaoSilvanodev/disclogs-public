package com.clogs.disclogs.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserLists
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class SortType {
    RECENT,
    ALPHABETICAL,
    RATING
}

// 1. Define o estado específico da tela de Biblioteca
data class LibraryUiState(

    val isAlbumsLoading: Boolean = true,
    val isListsLoading: Boolean = true,
    val myAlbums: List<Album> = emptyList(),
    val userLists: List<UserLists> = emptyList(),
    val errorMessage: String? = null,
    val currentSortType: SortType = SortType.RECENT
)

/*
fun sortAlbums(sortType: SortType) {
    _uiState.update { it.copy(currentSort = sortType) }
    // Aplica a ordenação na lista 'myAlbums' e atualiza o estado
    val sortedList = when (sortType) {
        SortType.RECENT -> // Lógica para recentes (provavelmente ordem decrescente do ID ou data de adição)
        SortType.ALPHABETICAL -> _uiState.value.myAlbums.sortedBy { it.title }
        SortType.RATING -> // Lógica para nota (requer que 'Album' tenha o campo de userRating ou buscar da tabela de reviews)
    }
    _uiState.update { it.copy(myAlbums = sortedList) }
}
*/



@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val listRepository: ListRepository
) : ViewModel() {

    // Apenas a ViewModel pode alterar o valor dessa variável
    private val _uiState = MutableStateFlow(LibraryUiState())

    // 4. A Tela (LibraryScreen) vai observar essa variável, mas não pode modificá-la
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    // 5. Bloco executado automaticamente assim que a ViewModel inicia quando a library é aberta
    init {
        loadLibrary()
        loadLists()
    }

    private fun loadLibrary() {

        viewModelScope.launch {

            _uiState.update { it.copy(isAlbumsLoading = true) }

            val result = repository.getLibraryAlbums()

            result.onSuccess { albums ->

                _uiState.update {
                    it.copy(
                        isAlbumsLoading = false,
                        myAlbums = albums
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAlbumsLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }
    }

    private fun loadLists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isListsLoading = true) }

            val result = listRepository.getUserLists()

            result.onSuccess { lists ->
                _uiState.update {
                    it.copy(
                        isListsLoading = false,
                        userLists = lists
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isListsLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }
    }

    fun createNewList(name: String, description: String? = null) {
        if (name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isListsLoading = true) }

            val result = listRepository.createList(name, description)

            result.onSuccess {
                loadLists()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isListsLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }

    }

    fun addAlbumToList(albumId: String, listId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isListsLoading = true) }

            val result = listRepository.addAlbumToList(listId, albumId)

            result.onSuccess {
                _uiState.update { it.copy(isListsLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isListsLoading = false,
                        errorMessage = "Erro ao adicionar: ${error.message}"
                    )
                }
            }
        }
    }
}