package com.clogs.disclogs.data.remote.spotify

import com.google.gson.annotations.SerializedName

/*
* Mapear exatamente a estrutura que o Spotify nos devolve.
* Repare na anotação @SerializedName:
* ela serve para traduzir nomes feios da API
* (como access_token) para o padrão bonito do Kotlin (accessToken).
* */



data class SpotifyTokenResponse(
    @SerializedName("access_token") val accessToken: String
)

data class SpotifySearchResponses(
    @SerializedName("albums") val albuns: SpotifyAlbuns? = null,
    val artists: SpotifyArtistsPaging? = null,
    val tracks: SpotifyTracksPaging? = null
)

data class SpotifyAlbuns(
    @SerializedName("items")
    val itens: List<SpotifyAlbumDto>,
    val total: Int
)

data class SpotifyArtistsPaging(
    val items: List<SpotifyArtistItemDto>
)

data class SpotifyTracksPaging(
    val items: List<SpotifyTrackItemDto>
)

data class SpotifyAlbumDto(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtists>,
    val images: List<SpotifyImage>,
    @SerializedName("release_date") val year: String,
    @SerializedName("total_tracks") val totalTracks: String,
    @SerializedName("album_type") val albumType: String? = null
)

data class SpotifyArtistItemDto(
    val id: String,
    val name: String,
    val images: List<SpotifyImage>?
)

data class SpotifyTrackItemDto(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtists>,
    val album: SpotifyAlbumDto
)

data class SpotifyArtists(
    val name: String
)

data class SpotifyImage(
    val url: String
)

