package com.clogs.disclogs.ui.screens.details


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.ui.theme.DisclogsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    artistId: String,
    viewModel: ArtistViewModel,
    onBackClick: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadArtist(artistId)
    }

    ArtistScreenContent(
        state = state,
        onBackClick = onBackClick,
        onSortChange = { sortOrder -> viewModel.setOrder(sortOrder) },
        onNavigateToDetails = onNavigateToDetails,
        onFilterClick = { viewModel.setFilter(it) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreenContent(
    state: ArtistUiState,
    onBackClick: () -> Unit,
    onSortChange: (SortOrder) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onFilterClick: (ReleaseFilter) -> Unit
) {

    var filterMenuExpanded by remember { mutableStateOf(false) }
    val currentFilterLabel = when (state.currentSort) {
        SortOrder.NEWEST_FIRST -> "Recentes"
        SortOrder.OLDEST_FIRST -> "Antigos"
        SortOrder.ALPHABETICAL -> "Alfabética"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, topBar = {
            TopAppBar(
                title = { }, // Título vazio, pois o nome já está gigante na imagem
                colors = topAppBarColors(
                    containerColor = Color.Transparent, // TopBar transparente sobre a imagem!
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ), navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                })
        }) { paddingValues ->
        // O padding do Scaffold foi removido de propósito (passando 0.dp pro LazyColumn)
        // para que a imagem do artista "vaze" por baixo da TopBar e encoste no topo da tela.
        LazyColumn(

            modifier = Modifier.fillMaxSize()
        ) {

            // ==========================================
            // ITEM 1: HERO IMAGE & NOME
            // ==========================================
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp) // Um pouco mais alto que o do álbum
                ) {
                    AsyncImage(
                        model = state.artistImageUrl,
                        contentDescription = "Foto de ${state.artistName}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Degradê Escuro
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.background
                                    ), startY = 200f
                                )
                            )
                    )

                    // Textos ancorados na base da imagem
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = state.tags,
                            color = MaterialTheme.colorScheme.error, // O vermelho do mock
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.artistName,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 48.sp,
                            lineHeight = 48.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "1926 – 1991", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            // TODO: Trocar por um ícone de fone de ouvido se tiver
                            Text(
                                text = "🎧 ${state.listeners}", color = Color.Gray, fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // ==========================================
            // ITEM 2: BIO & FILTROS
            // ==========================================
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = state.bio,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "READ FULL BIO",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* Abre popup com bio completa */ })

                    Spacer(modifier = Modifier.height(32.dp))

                    // Filtros (ALL | ALBUMS | SINGLES)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FilterChip(
                            selected = state.currentFilter == ReleaseFilter.ALL,
                            onClick = { onFilterClick(ReleaseFilter.ALL) },
                            label = { Text("ALL") })
                        FilterChip(
                            selected = state.currentFilter == ReleaseFilter.ALBUM,
                            onClick = { onFilterClick(ReleaseFilter.ALBUM) },
                            label = { Text("ALBUMS") })
                        FilterChip(
                            selected = state.currentFilter == ReleaseFilter.SINGLE,
                            onClick = { onFilterClick(ReleaseFilter.SINGLE) },
                            label = { Text("SINGLES") })
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Top Albums",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ==========================================
            // ITEM 3: GRADE (A MÁGICA DO CHUNKED)
            // ==========================================
            // Separa a lista em grupos de 2. Isso cria "linhas" de grade nativa mente
            val top4 = state.allReleases.take(4) // Pega só os 4 primeiros pro destaque

            items(top4.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (album in rowItems) {
                        // Card do Álbum (ocupa metade da tela)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigateToDetails(album.id) }) {
                            AsyncImage(
                                model = album.coverUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = album.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = album.releaseYear, color = Color.Gray, fontSize = 12.sp)

                            // Mock Estrelas
                            Row {
                                for (i in 1..5) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                    // Se a última linha tiver só 1 álbum, coloca um espaço vazio pra alinhar
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // ==========================================
            // ITEM 4: DISCOGRAPHY (LISTA HORIZONTAL)
            // ==========================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Discografia",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { filterMenuExpanded = true }
                                .padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Ordenar",
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

                            DropdownMenu(
                                expanded = filterMenuExpanded,
                                onDismissRequest = { filterMenuExpanded = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                            ) {
                                DropdownMenuItem(text = {
                                    Text(
                                        "Recentes", color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, onClick = {
                                    filterMenuExpanded = false
                                    onSortChange(SortOrder.NEWEST_FIRST)
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        "Antigos", color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, onClick = {
                                    filterMenuExpanded = false
                                    onSortChange(SortOrder.OLDEST_FIRST)
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        "Alfabética", color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, onClick = {
                                    filterMenuExpanded = false
                                    onSortChange(SortOrder.ALPHABETICAL)
                                })

                            }

                        }
                    }
                }
            }
            items(state.displayedReleases) { album ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetails(album.id) }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = album.coverUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = album.title, fontWeight = FontWeight.Bold)
                        Text(text = album.releaseYear, color = Color.Gray, fontSize = 12.sp)
                    }
                    // Rating/Coração lateral
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = album.averageRating.toString(), fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) } // Respiro final
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistScreenPreview() {
    val mockReleases = listOf(
        Album(
            id = "1",
            title = "Kind of Blue",
            artist = "Miles Davis",
            releaseYear = "1959",
            coverUrl = "",
            type = "ALBUM"
        ), Album(
            id = "2",
            title = "Bitches Brew",
            artist = "Miles Davis",
            releaseYear = "1970",
            coverUrl = "",
            type = "ALBUM"
        ), Album(
            id = "3",
            title = "Blue in Green (Take 1)",
            artist = "Miles Davis",
            releaseYear = "1959",
            coverUrl = "",
            type = "SINGLE"
        )
    )

    val state = ArtistUiState(
        isLoading = false,
        artistName = "Miles Davis",
        artistImageUrl = "",
        tags = "JAZZ LEGEND",
        listeners = "4.2M Listeners",
        bio = "Miles Dewey Davis III was an American trumpeter...",
        allReleases = mockReleases,
        displayedReleases = mockReleases
    )

    DisclogsTheme {
        ArtistScreenContent(
            state = state, onBackClick = {}, onFilterClick = {}, onSortChange = {},
            onNavigateToDetails = { }
        )
    }
}