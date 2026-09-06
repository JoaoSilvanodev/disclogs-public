package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.remote.FirebaseDataSource
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource

) : ProfileRepository {


    override suspend fun getCurrentUser(): Result<Profiles?> {
        val userId = firebaseDataSource.getCurrentUser() ?: throw Exception("Usuário não encontrado")
        return firebaseDataSource.getProfile(userId)
    }

    override suspend fun updateProfile(
        fullName: String?,
        username: String?,
        top4: List<Album>?
    ): Result<Unit> {
        return firebaseDataSource.updateProfile(fullName, username, top4)
    }

    override suspend fun getReviewsCount(): Result<Int> {
        return firebaseDataSource.getReviewsCount()
    }
    override suspend fun searchUsers(query: String): Result<List<Profiles>> {
        return firebaseDataSource.searchUsers(query)
    }
    override suspend fun checkIfFollowing(targetUserId: String): Result<Boolean> {
        return firebaseDataSource.checkIfFollowing(targetUserId)
    }
    override suspend fun followUser(targetUserId: String): Result<Unit> {
        return firebaseDataSource.followUser(targetUserId)
    }
    override suspend fun unfollowUser(targetUserId: String): Result<Unit> {
        return firebaseDataSource.unfollowUser(targetUserId)
    }
    override suspend fun getUserProfile(userId: String): Result<Profiles> {
        return firebaseDataSource.getUserProfile(userId)
    }
    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return firebaseDataSource.updateFcmToken(token)
    }
    override suspend fun getFriendActivity(): Result<List<CommunityActivity>> {
        return firebaseDataSource.getFriendActivity()
    }

    override suspend fun getFollowersCount(userId: String): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowingCount(userId: String): Result<Int> {
        TODO("Not yet implemented")
    }

}