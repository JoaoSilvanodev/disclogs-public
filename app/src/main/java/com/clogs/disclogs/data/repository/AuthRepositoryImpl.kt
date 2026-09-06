package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.remote.FirebaseDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : AuthRepository {

    override suspend fun registerUser(email: String,password: String,nomeCompleto: String,nick: String): Result<Unit> {
        val result = firebaseDataSource.signUp(email, password, nick)
        return if (result.isSuccess) Result.success(Unit) else Result.failure(result.exceptionOrNull() ?: Exception("Erro no cadastro"))
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        val result = firebaseDataSource.signIn(email, password)
        return if (result.isSuccess) Result.success(Unit) else Result.failure(result.exceptionOrNull() ?: Exception("Erro no login"))
    }


    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        val result = firebaseDataSource.signWithGoogle(idToken)
        return if (result.isSuccess)
            Result.success(Unit)
        else Result.failure(result.exceptionOrNull() ?:
        Exception("Erro no login"))
    }

    override suspend fun updateEmail(newEmail: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithSpotify(): Result<Unit> {
        TODO("Not yet implemented")
    }


    override fun isUserLoggedIn(): Boolean {
        return firebaseDataSource.getCurrentUser() != null
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }
}