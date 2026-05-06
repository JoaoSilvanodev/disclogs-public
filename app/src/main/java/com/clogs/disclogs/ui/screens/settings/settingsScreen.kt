package com.clogs.disclogs.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.clogs.disclogs.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTop4Sheet by remember { mutableStateOf(false) }

    var pushEnabled by remember { mutableStateOf(true) }
    var emailDigestEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.settings_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.settings_subtitle),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_profile), Icons.Default.Person)

                ClickableItem(
                    title = stringResource(R.string.settings_fav_albums_title),
                    subtitle = stringResource(R.string.settings_fav_albums_subtitle),
                    onClick = { showTop4Sheet = true }
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_edit_profile_title),
                    subtitle = stringResource(R.string.settings_edit_profile_subtitle),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_account), Icons.Default.Person)

                ClickableItem(
                    title = stringResource(R.string.settings_email_title),
                    subtitle = stringResource(R.string.settings_email_subtitle),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_password_title),
                    subtitle = stringResource(R.string.settings_password_subtitle),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_social_links_title),
                    subtitle = stringResource(R.string.settings_social_links_subtitle),
                    trailingIcon = Icons.Default.Link,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_notifications), Icons.Default.Notifications)
                SettingsToggleItem(
                    title = stringResource(R.string.settings_push_notifications_title),
                    subtitle = stringResource(R.string.settings_push_notifications_subtitle),
                    isChecked = pushEnabled,
                    onCheckedChange = { pushEnabled = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                SettingsToggleItem(
                    title = stringResource(R.string.settings_email_digest_title),
                    subtitle = stringResource(R.string.settings_email_digest_subtitle),
                    isChecked = emailDigestEnabled,
                    onCheckedChange = { emailDigestEnabled = it }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_appearance), Icons.Default.Palette)

                ClickableItem(
                    title = stringResource(R.string.settings_theme_title),
                    subtitle = stringResource(R.string.settings_theme_subtitle),
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_language_title),
                    subtitle = stringResource(R.string.settings_language_subtitle),
                    trailingIcon = Icons.Default.Language,
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_privacy), Icons.Default.Security)

                ClickableItem(
                    title = stringResource(R.string.settings_visibility_title),
                    subtitle = stringResource(R.string.settings_visibility_subtitle),
                    trailingIcon = Icons.Default.RemoveRedEye,
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_blocked_users_title),
                    subtitle = stringResource(R.string.settings_blocked_users_subtitle),
                    trailingIcon = Icons.Default.Block,
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(stringResource(R.string.settings_section_about), Icons.Default.Info)

                ClickableItem(
                    title = stringResource(R.string.settings_version_title),
                    subtitle = "2.4.0-nocturne",
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableItem(
                    title = stringResource(R.string.settings_terms_title),
                    subtitle = stringResource(R.string.settings_terms_subtitle),
                    trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* onLogout() */ }
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.settings_logout_description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.settings_logout),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        if (showTop4Sheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showTop4Sheet = false
                    viewModel.clearSearch()
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)) }
            ) {
                Top4BottomSheetContent(
                    initialSelection = state.top4Ids,
                    searchResults = state.searchResults,
                    isSearching = state.isSearching,
                    onSearch = { query ->
                        viewModel.searchAlbums(query)
                    },
                    onSaveClick = { novosDiscos ->
                        viewModel.updateTop4(novosDiscos)
                        showTop4Sheet = false
                    },
                    onCancelClick = {
                        showTop4Sheet = false
                        viewModel.clearSearch()
                    }
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title.uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

    }
}

@Composable
fun ClickableItem(
    title: String,
    subtitle: String,
    trailingIcon: ImageVector? = Icons.Default.ChevronRight,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                uncheckedTrackColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
