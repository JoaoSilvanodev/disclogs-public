package com.clogs.disclogs.ui.screens.search

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles

data class SearchUiState(
    val isLoading: Boolean = false,
    val albuns: List<Album> = emptyList(),
    val users: List<Profiles> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedFilter: String = "Álbum",
    val recentSearches: List<Album> = emptyList()
)
