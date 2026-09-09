package com.clogs.disclogs.data.repository


import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.CommunityActivity
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.remote.firebase.FirebaseProfileDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseReviewDataSource
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: FirebaseProfileDataSource,
    private val reviewDataSource: FirebaseReviewDataSource
) : ProfileRepository {


    override suspend fun getCurrentUser(): Result<Profiles?> {
        val userId = profileDataSource.getCurrentUser() ?: throw Exception("Usuário não encontrado")
        return profileDataSource.getProfile(userId)
    }

    override suspend fun updateProfile(
        fullName: String?,
        username: String?,
        top4: List<Album>?
    ): Result<Unit> {
        return profileDataSource.updateProfile(fullName, username, top4)
    }

    override suspend fun getReviewsCount(): Result<Int> {
        return reviewDataSource.getReviewsCount()
    }
    override suspend fun searchUsers(query: String): Result<List<Profiles>> {
        return profileDataSource.searchUsers(query)
    }
    override suspend fun checkIfFollowing(targetUserId: String): Result<Boolean> {
        return profileDataSource.checkIfFollowing(targetUserId)
    }
    override suspend fun followUser(targetUserId: String): Result<Unit> {
        return profileDataSource.followUser(targetUserId)
    }
    override suspend fun unfollowUser(targetUserId: String): Result<Unit> {
        return profileDataSource.unfollowUser(targetUserId)
    }
    override suspend fun getUserProfile(userId: String): Result<Profiles> {
        return profileDataSource.getUserProfile(userId)
    }
    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return profileDataSource.updateFcmToken(token)
    }
    override suspend fun getFriendActivity(): Result<List<CommunityActivity>> {
        return reviewDataSource.getFriendActivity()
    }

    override suspend fun getFollowersCount(userId: String): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowingCount(userId: String): Result<Int> {
        TODO("Not yet implemented")
    }

}