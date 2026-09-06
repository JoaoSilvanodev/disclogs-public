package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.ListItem
import com.clogs.disclogs.data.model.UserLists
import com.clogs.disclogs.data.remote.FirebaseDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyApiService
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ListRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val spotifyRemoteDataSource: SpotifyRemoteDataSource

) : ListRepository {
    override suspend fun createList(
        name: String,
        description: String?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlbumToList(
        listId: String,
        albumId: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getListDetails(listId: String): Result<UserLists> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserLists(): Result<List<UserLists>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListAlbums(listId: String): Result<List<Album>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteList(listId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlbumFromList(
        listId: String,
        albumId: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


}


