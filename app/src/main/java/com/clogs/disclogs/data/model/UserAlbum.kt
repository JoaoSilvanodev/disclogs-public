package com.clogs.disclogs.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAlbum(
    val id: String = "",
    val albumId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val isFavorite: Boolean = false
)
