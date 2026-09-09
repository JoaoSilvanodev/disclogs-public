package com.clogs.disclogs.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Profiles(
    val id: String,
    val fullName: String? = null,
    val username: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val top4: List<Album>? = null,
    val fcmToken: String? = null
)

@Serializable
data class Review(
    val id: Long? = null,
    val userId: String = "",
    val albumId: String = "",
    val albumTitle: String = "",
    val coverUrl: String = "",
    val comment: String? = null,
    val rating: Double? = null,
    val isFavorite: Boolean = false,
    val datePrecision: String = "EXACT",
    val createdAt: String = System.currentTimeMillis().toString(),
    val isLogged: Boolean = false,
    val listenDate: Long? = null,

    )

@Serializable
data class TrendingAlbums(
    val albumId: String,
    val averageRating: Double,
    val totalReviews: Int
)

// lista de IDs de quem o usuário segue
@Serializable
data class FollowingId(
    val followingId: String
)


@Serializable
data class FriendActivity(
    val id: Long,
    val userId: String,
    val albumId: String,
    val rating: Float? = null,
    val comment: String? = null,
    val createdAt: String,
    val profiles: FriendProfile? = null
)

@Serializable
data class FriendProfile(
    val username: String,
    val fullName: String? = null,
    val avatarUrl: String? = null
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
    val albumId: String,
    val rating: Double? = null,
    val comment: String? = null,
    val createdAt: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val profiles: ProfileBasicInfo? = null
)

// Uma classe só para pegar os dados do dono da ‘review’
@Serializable
data class ProfileBasicInfo(
    val username: String? = null,
    val fullName: String? = null,
    val avatarUrl: String? = null
)

@Serializable
data class UserList(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String? = null,
    val createdAt: String? = null,
    val albums: List<Album> = emptyList(),
    val isPrivate: Boolean = false,
    val tags: List<String?> = emptyList()
)
