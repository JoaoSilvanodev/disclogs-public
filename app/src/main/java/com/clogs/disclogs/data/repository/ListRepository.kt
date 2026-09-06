package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.UserLists

interface ListRepository {

    suspend fun createList(name: String, description: String? = null): Result<Unit>
    suspend fun addAlbumToList(listId: String, albumId: String): Result<Unit>

    suspend fun getListDetails(listId: String): Result<UserLists>

    suspend fun getUserLists(): Result<List<UserLists>>
    suspend fun getListAlbums(listId: String): Result<List<Album>> // retorna so ID's dos albuns em uma lista

    suspend fun deleteList(listId: String): Result<Unit>
    suspend fun deleteAlbumFromList(listId: String, albumId: String): Result<Unit>
}