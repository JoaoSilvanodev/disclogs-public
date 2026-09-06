package com.clogs.disclogs.data.repository

import com.clogs.disclogs.data.model.Album
import com.clogs.disclogs.data.model.Profiles

// ‘interface’ que detém os métodos de autenticação
interface AuthRepository {

    suspend fun registerUser(
        email: String,
        password: String,
        nomeCompleto: String,
        nick: String
    ): Result<Unit>

    suspend fun loginWithEmail(email: String, password: String): Result<Unit>

    suspend fun loginWithGoogle(idToken: String): Result<Unit>

    suspend fun updateEmail(newEmail: String): Result<Unit>

    suspend fun updatePassword(newPassword: String): Result<Unit>

    suspend fun loginWithSpotify(): Result<Unit>



    fun isUserLoggedIn(): Boolean

    suspend fun logout(): Result<Unit>

}