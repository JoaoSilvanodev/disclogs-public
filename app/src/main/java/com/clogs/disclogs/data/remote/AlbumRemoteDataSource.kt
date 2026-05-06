package com.clogs.disclogs.data.remote

import com.clogs.disclogs.data.model.UserAlbum

interface AlbumRemoteDataSource {


    // Método para obter os álbuns do usuário
    suspend fun getUserAlbums(userId: String) : List<UserAlbum>

    // Método para salvar um álbum do usuário
    suspend fun saveUserAlbum(userAlbum: UserAlbum)

    // Metodo para obter os albuns mais populares
    suspend fun getPopularAlbums() : List<UserAlbum>
}


