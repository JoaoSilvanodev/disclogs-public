package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.FriendActivity
import com.clogs.disclogs.data.model.Profiles

interface ProfileRepository {

    suspend fun getCurrentUser(): Result<Profiles>

    suspend fun updateProfile(
        fullName: String? = null,
        username: String? = null,
        top4AlbumIds: List<Album>? = null
    ): Result<Unit>

    suspend fun getReviewsCount(): Result<Int>

    suspend fun searchUsers(query: String) : Result<List<Profiles>>

    suspend fun checkIfFollowing(targetUserId: String): Result<Boolean>

    suspend fun followUser(targetUserId: String): Result<Unit>

    suspend fun unfollowUser(targetUserId: String): Result<Unit>

    suspend fun getUserProfile(userId: String): Result<Profiles>

    suspend fun updateFcmToken(token: String): Result<Unit>

    suspend fun getFriendActivity(): Result<List<FriendActivity>>
}