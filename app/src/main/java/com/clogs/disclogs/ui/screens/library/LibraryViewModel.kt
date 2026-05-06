package com.clogs.disclogs.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.reflect.loadServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.System.loadLibrary
import javax.inject.Inject


enum class SortType {
    RECENT,
    ALPHABETICAL,
    RATING
}

// 1. Define o estado específico da tela de Biblioteca
data class LibraryUiState(

    val isLoading: Boolean = true,
    val myAlbums: List<Album> = emptyList(),
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
class LibraryViewModel @Inject constructor(private val repository: AlbumRepository) : ViewModel() {

    // Apenas a ViewModel pode alterar o valor dessa variável
    private val _uiState = MutableStateFlow(LibraryUiState())

    // 4. A Tela (LibraryScreen) vai observar essa variável, mas não pode modificá-la
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    // 5. Bloco executado automaticamente assim que a ViewModel inicia quando a library é aberta
    init {
        loadLibrary()
    }

    private fun loadLibrary() {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            val result = repository.getLibraryAlbums()

            result.onSuccess { albums ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        myAlbums = albums
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }

    }
}