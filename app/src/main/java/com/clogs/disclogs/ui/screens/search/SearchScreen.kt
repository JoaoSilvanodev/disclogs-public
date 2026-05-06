package com.clogs.disclogs.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.ui.components.AlbumListItem
import com.clogs.disclogs.ui.components.RecentAlbumItem
import com.clogs.disclogs.ui.theme.DisclogsTheme


@Composable
fun SearchScreen(
    onNavigateToArtist: (String) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToUser: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()


    SearchScreenContent(
        uiState = uiState,
        onQueryChanged = { viewModel.search(it) },
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToArtist = onNavigateToArtist,
        onNavigateToUser = onNavigateToUser,
        onFilterChanged = { viewModel.onFilterChange(it) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onNavigateToArtist: (String) -> Unit,
    onNavigateToUser: (String) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onFilterChanged: (String) -> Unit,
    onBackClick: () -> Unit

) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.search_screen),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onQueryChanged,
                label = { Text(text = stringResource(R.string.search_placeholder), maxLines = 1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val filter = listOf(
                    stringResource(id = R.string.filter_album),
                    stringResource(id = R.string.filter_artist),
                    stringResource(id = R.string.filter_user)
                )
                filter.forEach { filtro ->
                    FilterChip(
                        selected = uiState.selectedFilter == filtro,
                        onClick = { onFilterChanged(filtro) },
                        label = { Text(filtro) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        border = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.searchQuery.isBlank()) {
                Text(
                    text = stringResource(R.string.title_recent_searches),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.recentSearches) { album ->
                        RecentAlbumItem(
                            album = album,
                            onAlbumClick = onNavigateToDetails
                        )
                    }
                }
            } else {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else if (uiState.errorMessage != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.error_message,
                                uiState.errorMessage
                            ),
                            color = Color.Red
                        )
                    }
                } else {

                    val filterAlbumStr = stringResource(id = R.string.filter_album)
                    val filterArtistStr = stringResource(id = R.string.filter_artist)
                    val filterUserStr = stringResource(id = R.string.filter_user)

                    val filteredList = when (uiState.selectedFilter) {
                        filterAlbumStr -> uiState.albuns.filter { it.type.lowercase() == "album" }
                        filterArtistStr -> uiState.albuns.filter { it.type.lowercase() == "artist" }
                        filterUserStr -> uiState.albuns.filter { it.type.lowercase() == "usuários" }
                        else -> uiState.albuns // Se nada selecionado, mostra tudo
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Text(
                                text = "Resultado",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                            )
                        }

                        if (uiState.selectedFilter == filterUserStr) {
                            items(uiState.users) { user ->
                                UserListItem(
                                    user = user,
                                    onClick = { onNavigateToUser(user.id) }
                                )
                            }
                        } else {
                            val filteredList = when (uiState.selectedFilter) {
                                filterAlbumStr -> uiState.albuns.filter { it.type.lowercase() == "album" }
                                filterArtistStr -> uiState.albuns.filter { it.type.lowercase() == "artist" }
                                else -> uiState.albuns
                            }
                            items(filteredList) { item ->
                                if (item.type.lowercase() == "artist") {
                                    AlbumListItem(
                                        album = item,
                                        onAlbumClick = { onNavigateToArtist(item.id) }
                                    )
                                } else {
                                    AlbumListItem(
                                        album = item,
                                        onAlbumClick = { onNavigateToDetails(item.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: Profiles, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatarUrl ?: R.drawable.profile,
            contentDescription = "Avatar de ${user.username}",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.fullName ?: user.username,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "@${user.username}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val sampleAlbums = listOf(
        Album(
            id = "1",
            title = "The Dark Side of the Moon",
            artist = "Pink Floyd",
            type = "album"
        ),
        Album(id = "2", title = "In Rainbows", artist = "Radiohead", type = "album"),
        Album(id = "3", title = "Daft Punk", artist = "Daft Punk", type = "artist")
    )
    DisclogsTheme {
        SearchScreenContent(
            uiState = SearchUiState(
                searchQuery = "Radiohead",
                albuns = sampleAlbums,
                recentSearches = sampleAlbums
            ),
            onQueryChanged = {},
            onNavigateToDetails = {},
            onNavigateToArtist = {},
            onFilterChanged = { },
            onBackClick = { },
            onNavigateToUser = { }
        )
    }
}
