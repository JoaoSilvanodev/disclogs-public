package com.clogs.disclogs.data.remote.firebase

import com.clogs.disclogs.data.model.UserList
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseListDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createList(list: UserList): Result<Unit> {
        return try {
            val docref = if (list.id.isNotBlank()) {
                firestore.collection("lists").document(list.id)
            } else {
                firestore.collection("lists").document()
            }
            val listSave = list.copy(id = docref.id)
            docref.set(listSave).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllLists(userId: String): Result<List<UserList>> {
        return try {
            val querySnapshot = firestore.collection("lists")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val lists = querySnapshot.toObjects(UserList::class.java)
            Result.success(lists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveList(list: UserList): Result<Unit> {
        return try {
            firestore.collection("lists").document(list.id).set(list).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getListById(listId: String): Result<UserList?> {
        return try {
            val doc = firestore.collection("lists").document(listId).get().await()
            val list = doc.toObject(UserList::class.java)
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateList(list: UserList): Result<Unit> {
        return try {
            firestore.collection("lists").document(list.id).set(list).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteList(listId: String): Result<Unit> {
        return try {
            firestore.collection("lists").document(listId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAlbumFromList(listId: String, albumId: String): Result<Unit> {
        return try {
            firestore.collection("lists").document(listId)
                .update("albums", FieldValue.arrayRemove(albumId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
