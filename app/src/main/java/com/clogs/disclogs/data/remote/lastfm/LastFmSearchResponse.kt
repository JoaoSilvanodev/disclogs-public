package com.clogs.disclogs.data.remote.lastfm

import com.google.gson.annotations.SerializedName


// Example of a response from api doc
/*
* <results for="believe">
  <opensearch:Query role="request" searchTerms="believe" startPage="1"/>
  <opensearch:totalResults>734</opensearch:totalResults>
  <opensearch:startIndex>0</opensearch:startIndex>
  <opensearch:itemsPerPage>20</opensearch:itemsPerPage>
  <albummatches>
    <album>
      <name>Make Believe</name>
      <artist>Weezer</artist>
      <id>2025180</id>
      <url>http://www.last.fm/music/Weezer/Make+Believe</url>
      <image size="small">http://userserve-ak.last.fm/serve/34/8673675.jpg</image>
      <image size="medium">http://userserve-ak.last.fm/serve/64/8673675.jpg</image>
      <image size="large">http://userserve-ak.last.fm/serve/126/8673675.jpg</image>
      <streamable>0</streamable>
    </album>
    ...
  </albummatches>
</results>
* */


data class LastFmSearchResponse(
    val results: LastFmSearchResultsDto?
)

data class LastFmSearchResultsDto(
    @SerializedName("albummatches") val albumMatches: LastFmAlbumMatchesDto?
)

data class LastFmAlbumMatchesDto(
    @SerializedName("album") val album: List<LastFmAlbumDto>?
)

data class LastFmAlbumDto(
    val name: String?,
    val artist: String?,
    val id: String?,
    val url: String?,
    val image: List<LastFmImageDto>?
)

data class LastFmImageDto(
    @SerializedName("#text")
    val url: String?,
    val size: String?
)