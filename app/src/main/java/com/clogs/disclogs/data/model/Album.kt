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
    val type: String = "album",

    val averageRating: Double = 0.0,
    val totalRating: Double = 0.0,
    val userRating: Double = 0.0,
    val weeklyCount: Int = 0,
    val totalReviews: Int = 0,
    val lastReviewedAt: Long = 0,

    val wikiSummary: String? = null,
    val genres: List<String> = emptyList(),
    val lastFmUrl: String? = null,

    val recordLabel: String? = null,
    val catalogNumber: String? = null,
    val physicalFormat: String? = null,

)
