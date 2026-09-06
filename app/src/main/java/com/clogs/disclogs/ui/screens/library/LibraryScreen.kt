package com.clogs.disclogs.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.ui.components.UserListItem
import com.clogs.disclogs.ui.components.placeholders.AlbumPlaceholder
import com.clogs.disclogs.ui.theme.DisclogsTheme

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit, 
    onNavigateToList: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var showCreateListDialog by remember { mutableStateOf(false) }

    LibraryContent(
        uiState = uiState,
        onSortChange = {},
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToList = onNavigateToList,
        onCreateListClick = { showCreateListDialog = true }
    )

    if (showCreateListDialog) {
        CreateListDialog(
            onDismiss = { showCreateListDialog = false },
            onConfirm = { name, description ->
                viewModel.createNewList(name, description)
                showCreateListDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    onSortChange: (String) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onCreateListClick: () -> Unit,
    onNavigateToList: (String) -> Unit
) {
    var abaSelecionada by remember { mutableIntStateOf(0) }

    // Controle do Menu de Filtro
    var filterMenuExpanded by remember { mutableStateOf(false) }
    val initialFilterLabel = stringResource(R.string.library_sort_recent)
    var currentFilterLabel by remember { mutableStateOf(initialFilterLabel) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {

            TopAppBar(
                // modifier = Modifier.height(48.dp),

                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        Text(
                            text = stringResource(R.string.library_title),
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                    }

                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            if (abaSelecionada == 2) {
                FloatingActionButton(
                    onClick = onCreateListClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Criar nova lista",
                        tint = Color.White
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val abas = listOf(
                stringResource(R.string.library_tab_albums),
                stringResource(R.string.library_tab_artists),
                stringResource(R.string.library_tab_lists)
            )

            ScrollableTabRow(
                selectedTabIndex = abaSelecionada,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 8.dp,
                divider = {}
            ) {
                abas.forEachIndexed { index, string ->
                    Tab(
                        selected = abaSelecionada == index,
                        onClick = { abaSelecionada = index },
                        text = {
                            Text(
                                text = string,
                                fontWeight = if (abaSelecionada == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (abaSelecionada == index) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    )
                }
            }
            Spacer(Modifier.height(32.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { filterMenuExpanded = true }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.cd_sort),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentFilterLabel,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = filterMenuExpanded,
                        onDismissRequest = { filterMenuExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(R.string.sort_recent),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                currentFilterLabel = "RECENTES"
                                filterMenuExpanded = false
                                onSortChange("RECENT")
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(R.string.sort_alphabetical),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                currentFilterLabel = "ALFABÉTICA"
                                filterMenuExpanded = false
                                onSortChange("ALPHABETICAL")
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(R.string.library_sort_label_personal_rating),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                currentFilterLabel = "MAIOR NOTA"
                                filterMenuExpanded = false
                                onSortChange("RATING")
                            }
                        )

                    }

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (abaSelecionada) {
                    0 -> AlbumsTabContent(uiState, onNavigateToDetails)
                    1 -> ArtistsTabContent()
                    2 -> ListsTabContent(
                        uiState,
                        onListClick = onNavigateToList
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumsTabContent(
    uiState: LibraryUiState,
    onNavigateToDetails: (String) -> Unit
) {
    when {
        uiState.isAlbumsLoading -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(6) {
                    AlbumPlaceholder()
                }
            }
        }

        uiState.errorMessage != null -> {
            Text(
                text = stringResource(R.string.error_message, uiState.errorMessage!!),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        uiState.myAlbums.isEmpty() -> {
            Text(
                text = stringResource(R.string.library_empty),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        else -> {
            // Grade Forçada
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp), // Aumentei o espaço vertical para caber as estrelas
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.myAlbums) { album ->
                    LibraryGridItem(
                        album = album,
                        userRating = album.userRating,
                        onNavigateToDetails = { onNavigateToDetails(album.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ListsTabContent(
    uiState: LibraryUiState,
    onListClick: (String) -> Unit
) {
    when {
        uiState.isListsLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.userLists.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.library_empty_lists),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
            ) {
                items(uiState.userLists) { list ->
                    UserListItem(
                        userList = list,
                        onClick = { onListClick(list.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryGridItem(
    album: Album,
    userRating: Double?, // Novo parâmetro para a nota
    onNavigateToDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDetails(album.id) }
    ) {
        AsyncImage(
            model = album.coverUrl,
            contentDescription = "Capa do álbum ${album.title}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Mantém a capa quadrada
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = album.title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = album.artist,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Área das Estrelas
        if (userRating != null && userRating > 0.0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 1..5) {
                    val icon = when {
                        userRating >= i -> Icons.Default.Star
                        userRating >= i - 0.5 -> Icons.AutoMirrored.Filled.StarHalf
                        else -> Icons.Outlined.StarOutline
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (userRating >= i - 0.5) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.2f
                        ),
                        modifier = Modifier.size(10.dp)
                    )
                }

            }
        } else {
            // Mantém o espaço para o grid não ficar desalinhado se um álbum não tiver nota
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
fun ArtistsTabContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Artistas em breve...", color = Color.Gray)
    }
}

@Composable
fun CreateListDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Lista", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da lista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição (Opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description) },
                enabled = name.isNotBlank()
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LibraryContentPreview() {
    DisclogsTheme {
        val sampleAlbums = listOf(
            Album(
                id = "1",
                title = "The Dark Side of the Moon",
                artist = "Pink Floyd",
                coverUrl = "",
                releaseYear = "1973"
            ),
            Album(
                id = "2",
                title = "To Pimp a Butterfly",
                artist = "Kendrick Lamar",
                coverUrl = "",
                releaseYear = "2015"
            ),
            Album(
                id = "3",
                title = "Abbey Road",
                artist = "The Beatles",
                coverUrl = "",
                releaseYear = "1969"
            ),
            Album(
                id = "4",
                title = "Random Access Memories",
                artist = "Daft Punk",
                coverUrl = "",
                releaseYear = "2013"
            )
        )
        val uiState = LibraryUiState(
            isAlbumsLoading = false,
            myAlbums = sampleAlbums,
            errorMessage = null,
            currentSortType = SortType.RECENT
        )
        LibraryContent(
            uiState = uiState,
            onSortChange = {},
            onNavigateToDetails = {},
            onCreateListClick = {},
            onNavigateToList = { }
        )
    }
}
