package com.clogs.disclogs.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.ui.theme.DisclogsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top4BottomSheetContent(
    initialSelection: List<Album>,
    searchResults: List<Album>,
    isSearching: Boolean,
    onSearch: (String) -> Unit,
    onSaveClick: (List<Album>) -> Unit,
    onCancelClick: () -> Unit
) {
    // 1. O pulo do gato do Compose! Se a initialSelection mudar vindo da ViewModel,
    // o remember normal não avisa a tela. Usamos uma variável de estado simples.
    var selectedAlbums by remember { mutableStateOf(initialSelection) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f) // Um pouco mais alto pra garantir o teclado e a busca
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 32.dp)
    ) {
        // --- CABEÇALHO ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SELECIONE SEU TOP 4",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${selectedAlbums.size}/4",
                color = if (selectedAlbums.size == 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- OS 4 SLOTS FIXOS (Visualização e Remoção) ---
        // Aqui está o segredo: Não usamos LazyRow, porque são sempre exatamente 4 slots!
        // Usamos uma Row normal com Arrangement.spacedBy para ficarem perfeitamente divididos.
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Renderiza os 4 espaços (preenchidos ou vazios)
            for (i in 0 until 4) {
                if (i < selectedAlbums.size) {
                    // Slot Preenchido com Álbum
                    val album = selectedAlbums[i]
                    Box(modifier = Modifier
                        .weight(1f) // Divide o espaço igualmente
                        .aspectRatio(1f) // Quadrado perfeito
                        .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = album.coverUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Botão "X" vermelho de remover com estilo nativo
                        IconButton(
                            onClick = {
                                // Remove e atualiza o estado
                                selectedAlbums = selectedAlbums.filter { it.id != album.id }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(14.dp)
                                .background(MaterialTheme.colorScheme.error, CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remover",
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                } else {
                    // Slot Vazio (Placeholder)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f) // Quadrado perfeito
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(16.dp))

        // --- BARRA DE BUSCA (Ajustada com as cores certas) ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if(it.length > 2) {
                    onSearch(it)
                }
            },
            placeholder = { Text("Buscar álbuns...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
            leadingIcon = { Icon(Icons.Default.Search, tint = MaterialTheme.colorScheme.onSurface, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- RESULTADOS DA BUSCA ---
        if (isSearching) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(searchResults) { album ->
                    val isSelected = selectedAlbums.any { it.id == album.id }

                    SelectableAlbumItem(
                        album = album,
                        isSelected = isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedAlbums = selectedAlbums.filter { it.id != album.id }
                            } else if (selectedAlbums.size < 4) {
                                selectedAlbums = selectedAlbums + album
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÕES DE AÇÃO ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }

            Button(
                // Aqui enviamos a lista certinha pro viewModel quando clicar
                onClick = { onSaveClick(selectedAlbums) },
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun SelectableAlbumItem(
    album: Album,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() }
    ) {
        Box {
            AsyncImage(
                model = album.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = album.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        Text(
            text = album.artist,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Top4BottomSheetContentPreview() {
    val sampleAlbums = listOf(
        Album(id = "1", title = "The Dark Side of the Moon", artist = "Pink Floyd", coverUrl = ""),
        Album(id = "2", title = "Abbey Road", artist = "The Beatles", coverUrl = ""),
        Album(id = "3", title = "Thriller", artist = "Michael Jackson", coverUrl = ""),
        Album(id = "4", title = "Back in Black", artist = "AC/DC", coverUrl = "")
    )
    DisclogsTheme {
        Top4BottomSheetContent(
            initialSelection = sampleAlbums.take(2),
            searchResults = sampleAlbums,
            isSearching = false,
            onSearch = {},
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}
