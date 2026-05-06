package com.clogs.disclogs.ui.screens.details

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.ui.theme.DisclogsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheetContent(
    state: AlbumDetailUiState,
    onSaveClick: (Double, String, Long?, String, Boolean) -> Unit
) {
    var userRating by remember { mutableDoubleStateOf(state.userRating) }
    var reviewText by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }


    // Controles de Data
    var showDateOptions by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var exactDateKnown by remember { mutableStateOf(true) }
    var yearOnly by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    var rowWidth by remember { mutableIntStateOf(0) }

    // ===============================================================
    // COLUNA PRINCIPAL (Começa aqui e engloba TUDO até o calendário)
    // ===============================================================
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .padding(top = 16.dp)
            .imePadding() // Teclado empurra essa coluna
            .verticalScroll(rememberScrollState()) // E ela ganha rolagem!

    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = state.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = state.albumTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = state.artistName,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .onSizeChanged { rowWidth = it.width }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            if (rowWidth > 0) {
                                val rating = (offset.x / rowWidth) * 5f
                                userRating = ((rating * 2).roundToInt() / 2.0).coerceIn(0.0, 5.0)
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (rowWidth > 0) {
                                val rating = (change.position.x / rowWidth) * 5f
                                userRating = ((rating * 2).roundToInt() / 2.0).coerceIn(0.0, 5.0)
                            }
                        }
                    }
            ) {
                for (i in 1..5) {
                    val icon = when {
                        userRating >= i -> Icons.Default.Star
                        userRating >= i - 0.5 -> Icons.AutoMirrored.Filled.StarHalf
                        else -> Icons.Outlined.Star
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = "Nota $i",
                        tint = if (userRating >= i - 0.5)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "RATING",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Favorito
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favoritar",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.2f
                    ),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { isFavorite = !isFavorite }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Favorite",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }

            // Diário
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AddBox,
                    contentDescription = "Adicionar ao diário",
                    tint = if (showDateOptions || selectedDateMillis != null || yearOnly.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.2f
                    ),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            showDateOptions = !showDateOptions
                        }
                )
                Spacer(modifier = Modifier.height(4.dp))
                val diaryText = when {
                    !exactDateKnown && yearOnly.length == 4 -> yearOnly
                    selectedDateMillis != null -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis!!))
                    else -> stringResource(R.string.diary)
                }
                Text(
                    text = diaryText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }

            // Listas
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Listas",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { /* Abre bottom sheet de listas */ }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lists",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        if (showDateOptions) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = exactDateKnown,
                        onCheckedChange = { exactDateKnown = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (exactDateKnown) "Lembro a data exata" else "Lembro apenas o ano",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (exactDateKnown) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val dateText = selectedDateMillis?.let {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Selecionar data no calendário"
                        Text(dateText)
                    }
                } else {
                    OutlinedTextField(
                        value = yearOnly,
                        onValueChange = {
                            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                yearOnly = it
                            }
                        },
                        label = { Text("Ano") },
                        placeholder = { Text("Ex: 2014") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(24.dp))

        // TEXT AREA (Escrever a Review)
        Text(
            text = "Write a Review",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            placeholder = {
                Text(
                    "The textures in this record feel like rain on obsidian...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÃO DE SALVAR
        Button(
            onClick = {
                var finalMillis = selectedDateMillis
                var precision = "EXACT"

                if (!exactDateKnown && yearOnly.length == 4) {
                    val calendar = java.util.Calendar.getInstance()
                    calendar.set(yearOnly.toInt(), java.util.Calendar.JANUARY, 1, 0, 0, 0)
                    finalMillis = calendar.timeInMillis
                    precision = "YEAR"
                }

                // MUDANÇA AQUI: Passando finalMillis em vez de selectedDateMillis
                onSaveClick(
                    userRating,
                    reviewText,
                    finalMillis,
                    precision,
                    isFavorite
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "SAVE REVIEW",
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } // ===================== FIM DA COLUNA PRINCIPAL =====================


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("Ok", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewBottomSheetContentPreview() {
    DisclogsTheme {
        ReviewBottomSheetContent(
            state = AlbumDetailUiState(
                albumTitle = "Misty",
                artistName = "Tsuyoshi Yamamoto Trio",
                coverUrl = "",
                userRating = 4.5
            ),
            onSaveClick = { _, _, _, _, _ -> }
        )
    }
}