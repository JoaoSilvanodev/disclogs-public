package com.clogs.disclogs.ui.screens.library.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserLists
import com.clogs.disclogs.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListUiState(
    val isLoading: Boolean = false,
    val listDetails: UserLists? = null,
    val albums: List<Album> = emptyList(),
    val errorMessage: String? = null
)


@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    fun loadList(listId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val detailsResult =
                listRepository.getListDetails(listId) // Carrega os detalhes da lista
            val albumResult = listRepository.getListAlbums(listId) // Carrega os álbuns da lista

            if (albumResult.isSuccess && albumResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        listDetails = detailsResult.getOrNull(),
                        albums = albumResult.getOrNull() ?: emptyList()
                    )
                }
            } else {
                val error = detailsResult.exceptionOrNull()?.message
                    ?: albumResult.exceptionOrNull()?.message
                    ?: "Erro desconhecido"

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar a lista $error"

                    )
                }
            }
        }
    }
}

