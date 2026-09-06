package com.clogs.disclogs.data.remote.lastfm

import kotlinx.serialization.Serializable

@Serializable
data class LastFmAlbumInfoResponse(
    val album: LastFmAlbumDetailsDto? = null
)

@Serializable
data class LastFmAlbumDetailsDto(
    val name: String,
    val artist: String,
    val playcount: String? = null,
    val wiki: LastFmWikiDto? = null,
    val tags: LastFmTagsDto? = null
)

@Serializable
data class LastFmWikiDto(
    val published: String? = null,
    val summary: String? = null,
    val content: String? = null
)

@Serializable
data class LastFmTagsDto(
    val tag: List<LastFmTagDto> = emptyList()
)

@Serializable
data class LastFmTagDto(
    val name: String
)