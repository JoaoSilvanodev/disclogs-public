package com.clogs.disclogs.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profiles(
    val id: String,
    @SerialName("full_name") val fullName: String? = null,
    val username: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val bio: String? = null,
    @SerialName("top_4_albums") val top4: List<Album>? = null,
    @SerialName("fcm_token") val fcmToken: String? = null
)

@Serializable
data class Review(
    val id: Long? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("album_id") val albumId: String,
    val rating: Double? = null,
    val comment: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("is_favorite") val isFavorite: Boolean = false,
    @SerialName("is_logged") val isLogged: Boolean = false,
    @SerialName("listen_date") val listenDate: Long? = null,
    @SerialName("date_precision") val datePrecision: String = "EXACT"
)

@Serializable
data class TrendingAlbums(
    @SerialName("album_id") val albumId: String,
    @SerialName("total_reviews") val totalReview: Int
)

@Serializable
data class ReviewRow(
    @SerialName("album_id") val albumId: String
)

// lista de IDs de quem o usuário segue
@Serializable
data class FollowingId(
    @SerialName("following_id") val followingId: String
)


//
@Serializable
data class FriendActivity(
    val id: Long,
    @SerialName("user_id") val userId: String,
    @SerialName("album_id") val albumId: String,
    val rating: Float? = null,
    @SerialName("created_at") val createdAt: String,
    val profiles: FriendProfile? = null
)

@Serializable
data class FriendProfile(
    val username: String,
    @SerialName("avatar_url") val avatarUrl: String? = null
)

@Serializable
data class RatingStats(
    val average: Double = 0.0,
    val totCount: Int = 0,
    val distribution: List<Int> = List(10) { 0 }
)


// Esta classe é para leitura do Feed relacionado às ‘reviews’
@Serializable
data class CommunityActivity(
    val id: Long,
    @SerialName("album_id") val albumId: String,
    val rating: Double? = null,
    val comment: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("likes_count") val likesCount: Int = 0,
    @SerialName("comments_count") val commentsCount: Int = 0,
    val profiles: ProfileBasicInfo? = null
)

// Uma classe só para pegar os dados do dono da ‘review’
@Serializable
data class ProfileBasicInfo(
    val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null
)