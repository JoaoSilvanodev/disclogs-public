package com.clogs.disclogs.data.model

import kotlinx.serialization.Serializable


@Serializable
data class DiscogsSearchResponse(
    val results: List<DiscogsResultDto> = emptyList()
)

@Serializable
data class DiscogsResultDto(
    val title: String,
    val artist: String,
    val year: String? = null,
    val country: String? = null,
    val format: List<String> = emptyList(),
    val label: List<String> = emptyList(),
    val genre: List<String> = emptyList(),
    val style: List<String> = emptyList(),
    val catno: String? = null,
    val id: Int
)