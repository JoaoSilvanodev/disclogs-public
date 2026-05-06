package com.clogs.disclogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.ProfileBasicInfo
import com.clogs.disclogs.ui.theme.DisclogsTheme

@Composable
fun ActivityItem(
    activity: CommunityActivity,
    onLikeClick: (Long) -> Unit = {},
    onReplyClick: (Long) -> Unit = {},
) {

    val userName = activity.profiles?.fullName
        ?: activity.profiles?.username ?: "Usuário Oculto"


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReplyClick(activity.id) }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = activity.profiles?.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = userName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                if ((activity.rating != null) && (activity.rating > 0)) {

                    Row() {
                        for (i in 1..5) {
                            val icon = when {
                                activity.rating!! >= i -> Icons.Default.Star
                                activity.rating >= i - 0.5 -> Icons.AutoMirrored.Filled.StarHalf
                                else -> Icons.Outlined.StarOutline
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (activity.rating >= i - 0.5) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.2f
                                ),
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (!activity.comment.isNullOrEmpty()) {
            activity.comment?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontStyle = FontStyle.Normal
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {

            // Botão de Like
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onLikeClick(activity.id) }
                    .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Curtir",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = activity.likesCount.toString(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onReplyClick(activity.id) }
                    .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Responder",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = activity.commentsCount.toString(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    DisclogsTheme {
        ActivityItem(
            activity = CommunityActivity(
                id = 1,
                albumId = "Misty - Tsuyoshi Yamamoto Trio",
                rating = 4.5,
                comment = "One of the best jazz piano albums I've ever heard. The recording quality is absolutely superb, every note is crystal clear.",
                createdAt = "2023-10-27T10:00:00Z",
                likesCount = 12,
                commentsCount = 3,
                profiles = ProfileBasicInfo(
                    username = "johndoe",
                    fullName = "John Doe",
                    avatarUrl = null
                )
            )
        )
    }
}




