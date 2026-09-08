package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserList
import com.clogs.disclogs.data.remote.FirebaseDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val spotifyRemoteDataSource: SpotifyRemoteDataSource

) : ListRepository {
    override suspend fun createList(list: UserList): Result<Unit> {

        return try {
            val result = firebaseDataSource.createList(list)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addAlbumToList(listId: String, album: Album): Result<Unit> {

        return try {
            val list = firebaseDataSource.getListById(listId).getOrNull()
                ?: return Result.failure(Exception("List not found"))

            val checkAlbum = firebaseDataSource.getAlbumById(album.id)

            if (checkAlbum == null) {
                firebaseDataSource.saveAlbumLocaly(album)
            }

            // check if album is already in the list
            if (list.albums.any { it.id == album.id }) {
                return Result.failure(Exception("Album already in the list"))
            } else {
                val updatedList = list.copy(albums = list.albums + album)
                return firebaseDataSource.updateList(updatedList)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeAlbumFromList(
        listId: String,
        albumId: String
    ): Result<Unit> {
        return try {
            val list = firebaseDataSource.getListById(listId).getOrNull()
                ?: return Result.failure(Exception("List not found"))
            val updatedList = list.copy(albums = list.albums.filter { it.id != albumId })
            firebaseDataSource.updateList(updatedList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllLists(userId: String): Result<List<UserList>> {
        return firebaseDataSource.getAllLists(userId)
    }

    override suspend fun getListById(listId: String): Result<UserList?> {
        return firebaseDataSource.getListById(listId)
    }

    override suspend fun updateList(
        listId: String,
        name: String,
        description: String?,
        tags: List<String?>,
        isPrivate: Boolean
    ): Result<Unit> {
        return try {
            val list = firebaseDataSource.getListById(listId).getOrNull()
                ?: return Result.failure(Exception("List not found"))
            val updated = list.copy(
                name = name,
                description = description,
                tags = tags,
                isPrivate = isPrivate
            )
            firebaseDataSource.updateList(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteList(listId: String): Result<Unit> {
        return firebaseDataSource.deleteList(listId)
    }
}


