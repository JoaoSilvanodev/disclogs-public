package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserList

interface ListRepository {

    suspend fun createList(list: UserList): Result<Unit>

    /*
    add an album to a list passing the listId, and the object album itself
    should verify on its implementation if the album is registered on the
    database, if not the instance of the album should be saved on the album
    collection.
     */
    suspend fun addAlbumToList(
        listId: String,
        album: Album
    ): Result<Unit>


    suspend fun removeAlbumFromList(
        listId: String,
        albumId: String
    ): Result<Unit>

    suspend fun getAllLists(
        userId: String
    ): Result<List<UserList>>

    suspend fun getListById(
        listId: String
    ): Result<UserList?>

    suspend fun updateList(
        listId: String,
        name: String,
        description: String?,
        tags: List<String?>,
        isPrivate: Boolean
    ): Result<Unit>

    suspend fun deleteList(
        listId: String
    ): Result<Unit>


}