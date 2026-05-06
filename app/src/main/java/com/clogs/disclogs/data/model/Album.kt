package com.clogs.disclogs.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val coverUrl: String = "",
    val releaseYear: String = "",
    val totalTracks: String = "",
    val averageRating: Double = 0.0,
    val totalRating: Double = 0.0,
    val userRating: Double = 0.0,
    val type: String = "album"
)
