package com.clogs.disclogs.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.clogs.disclogs.R
import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.ui.theme.DisclogsTheme


@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onSettingsClick: () -> Unit,
    targetUserId: String? = null,
    onAlbumClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(targetUserId) {
        viewModel.loadProfile(targetUserId)
    }


    ProfileScreen(
        state = state,
        modifier = modifier,
        onSettingsClick = onSettingsClick,
        onAlbumClick = onAlbumClick,
        onFollowClick = { viewModel.toggleFollow() },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    onFollowClick: () -> Unit,
    onAlbumClick: (String) -> Unit
) {
    val top4 = state.profile?.top4 ?: emptyList()


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.profile_title),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                },

                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),

                actions = {
                    if (state.isCurrentUser) {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = stringResource(R.string.settings_description)
                            )
                        }
                    }
                }
            )
        }

    ) { paddingValues ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {

                AsyncImage(
                    model = state.profile?.avatarUrl ?: R.drawable.profile,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.profile_collector_label),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = state.profile?.fullName ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 44.sp
                )

                Text(
                    text = "@${state.profile?.username ?: ""}",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )

                if (!state.isCurrentUser) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onFollowClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isFollowing)
                                MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.primary,

                            contentColor = if (state.isFollowing)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = if (state.isFollowing) "UNFOLLOW" else "FOLLOW",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = state.albumCount.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.profile_albums_count),
                            color = Color.Gray,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "0",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.profile_followers_count),
                            color = Color.Gray,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "0",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.profile_follows),
                            color = Color.Gray,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
            }
            item {
                Column {
                    /*Text(
                        text = stringResource(R.string.profile_curated_section),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )*/
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.profile_favorites),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 44.sp
                        )
                        Text(
                            modifier = Modifier.clickable { },
                            text = stringResource(R.string.profile_view_all),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    top4.forEach { album ->
                        Top4AlbumItem(
                            album = album,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onAlbumClick = {
                                onAlbumClick(album.id)
                            }
                        )
                    }
                    val placeholders = 4 - top4.size
                    repeat(placeholders) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val sampleProfile = Profiles(
        id = "1",
        fullName = "John Doe",
        username = "johndoe",
        avatarUrl = null,
        bio = "Vinyl collector and music enthusiast.",
        top4 = listOf(
            Album(id = "1", title = "Album 1", artist = "Artist 1"),
            Album(id = "2", title = "Album 2", artist = "Artist 2"),
            Album(id = "3", title = "Album 3", artist = "Artist 3"),
            Album(id = "4", title = "Album 4", artist = "Artist 4")
        )
    )
    val sampleState = ProfileUiState(
        isLoading = false,
        profile = sampleProfile,
        errorMessage = null,
        isCurrentUser = false
    )
    DisclogsTheme {
        ProfileScreen(
            state = sampleState,
            onSettingsClick = {},
            onAlbumClick = {},
            onFollowClick = {},
        )
    }
}
