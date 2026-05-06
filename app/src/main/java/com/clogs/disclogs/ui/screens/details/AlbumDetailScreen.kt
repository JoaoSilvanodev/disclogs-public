package com.clogs.disclogs.ui.screens.details

import android.widget.Toast
import androidx.compose.foundation.background
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
            Toast.makeText(context, "Review salva com sucesso!", Toast.LENGTH_SHORT).show()
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
                        "DETALHES",
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
                            contentDescription = "Voltar"
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
                        contentDescription = "Capa do album ${state.albumTitle}",
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
                            text = if (state.inLibrary) "REVIEWED / LOG AGAIN" else "ADD TO LIBRARY",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // 1. ÁREA DE Notas E Histograma LARGO
                    Text(
                        text = "RATINGS",
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
                        text = "ABOUT",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "An uncompromising record that defines the current mood. The production is flawless, heavy yet spacious, drawing from classical literature influences to weave a narrative of isolation and reflection. Recorded in the depths of winter, the soundscapes feel as vast and solitary as a Norwegian fjord.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 3. INFORMAÇÕES DE LANÇAMENTO (RELEASE INFO)
                    Text(
                        text = "RELEASE INFO",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "LABEL",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Three Blind Mice",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "FORMAT",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Vinyl, LP, Album",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CATALOG",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "TBM-CD-2524",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "COPYRIGHT",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "℗ 1974 TBM Records",
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
                        text = "REVIEWS",
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
                            text = "Ainda não há reviews para este álbum.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Seja o primeiro a avaliar!",
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
