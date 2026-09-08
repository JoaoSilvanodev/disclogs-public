package com.clogs.disclogs.ui.screens.library.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserList
import com.clogs.disclogs.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListUiState(
    val isLoading: Boolean = false,
    val listDetails: UserList? = null,
    val albums: List<Album> = emptyList(),
    val errorMessage: String? = null
)


@HiltViewModel
class ListViewModel @Inject constructor(private val listRepository: ListRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    fun loadList(listId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = listRepository.getListById(listId)

            result.onSuccess { list ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        listDetails = list,
                        albums = list?.albums ?: emptyList()
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

