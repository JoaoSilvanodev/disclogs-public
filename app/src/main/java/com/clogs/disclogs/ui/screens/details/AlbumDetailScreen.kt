package com.clogs.disclogs.ui.screens.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.ui.components.ActivityItem
import com.clogs.disclogs.ui.components.RatingHistogramSection

import com.clogs.disclogs.ui.theme.DisclogsTheme

@Composable
fun AlbumDetailScreen(
    albumId: String,
    viewModel: AlbumDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    // O LaunchedEffect roda esse bloco de código UMA ÚNICA VEZ quando a tela abre
    LaunchedEffect(albumId) {
        viewModel.loadAlbumDetail(albumId)
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(context, context.getString(R.string.review_saved_success), Toast.LENGTH_SHORT).show()
        }
    }

    AlbumDetailContent(
        state = state,
        onBackClick = onBackClick,
        onSaveReview = { rating, text, dateMillis, precision, isFav ->
            viewModel.saveReview(rating, text, dateMillis, precision, isFav)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailContent(
    state: AlbumDetailUiState,
    onBackClick: () -> Unit,
    onSaveReview: (Double, String, Long?, String, Boolean) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()



    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.album_details),
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
                            contentDescription = stringResource(R.string.cd_back_button)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            //! ITEM 1: A Capa Gigante (Mantido igual)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = state.coverUrl,
                        contentDescription = stringResource(R.string.cd_album_cover, state.albumTitle),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                                        MaterialTheme.colorScheme.background
                                    ),
                                    startY = 100f
                                )
                            )
                    )
                }
            }

            //! ITEM 2: Cabeçalho, Botão e Notas
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    // Título, Artista e Infos Básicas
                    Text(
                        text = state.albumTitle,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.artistName,
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${state.releaseYear} · ${stringResource(R.string.tracks)}: ${state.totalTracks}",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botão Principal
                    Button(
                        onClick = { showBottomSheet = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.inLibrary)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = if (state.inLibrary)
                                Icons.Default.Star
                            else
                                Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (state.inLibrary) stringResource(R.string.album_reviewed_log_again) else stringResource(R.string.album_add_to_library),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // 1. ÁREA DE Notas E Histograma LARGO
                    Text(
                        text = stringResource(R.string.album_ratings),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    RatingHistogramSection(
                        userRating = state.userRating,
                        inLib = state.inLibrary,
                        avgRating = state.avgRating,
                        distribution = state.ratingStats.distribution
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(24.dp))

                    // 2. RESUMO DO ÁLBUM (ABOUT)
                    Text(
                        text = stringResource(R.string.album_about),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    state.wikiSummary?.let { text ->

                        var isExpanded by remember { mutableStateOf(false) }
                        Text(
                            text = text,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                            overflow = TextOverflow.Ellipsis
                        )

                        if  (text.length > 150) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isExpanded) stringResource(R.string.album_read_less) else stringResource(R.string.album_read_more),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable {
                                    isExpanded = !isExpanded
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.album_genres),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.genre.joinToString(" • ") { it.uppercase() },
                        color = MaterialTheme.colorScheme.primary, // Usa a cor principal do seu app
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(24.dp))


                    // 3. INFORMAÇÕES DE LANÇAMENTO (RELEASE INFO)
                    Text(
                        text = stringResource(R.string.album_release_info),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.album_label),
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = state.recordLabel ?: "N/A",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )


                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.album_format),
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = state.physicalFormat ?: stringResource(R.string.album_digital),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )
                        }


                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.album_catalog),
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = state.catalogNumber ?: "-",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )


                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.album_copyright),
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            val yearText =
                                if (state.releaseYear.isNotBlank()) "℗ ${state.releaseYear}" else "-"
                            Text(
                                yearText,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }


            if (state.albumReviews.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.album_reviews),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Listagem das reviews dinâmicas
                items(state.albumReviews) { activity ->
                    ActivityItem(activity = activity)
                }
            } else {
                // Caso não tenha reviews, podemos mostrar um convite
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.album_no_reviews),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.album_be_first_to_review),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                ReviewBottomSheetContent(
                    state = state,
                    onSaveClick = { rating, text, dateMillis, precision, isFav ->
                        onSaveReview(rating, text, dateMillis, precision, isFav)
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumDetailScreenPreview() {
    DisclogsTheme {
        AlbumDetailContent(
            state = AlbumDetailUiState(
                isLoading = false,
                albumTitle = "Misty",
                artistName = "Tsuyoshi Yamamoto Trio",
                coverUrl = "",
                releaseYear = "1974"
            ),
            onSaveReview = { _, _, _, _, _ -> },
            onBackClick = { }
        )
    }
}
