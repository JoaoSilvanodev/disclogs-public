package com.clogs.disclogs.di

import com.clogs.disclogs.BuildConfig
import com.clogs.disclogs.data.remote.FirebaseDataSource
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.AlbumRepositoryImpl
import com.clogs.disclogs.data.repository.AuthRepository
import com.clogs.disclogs.data.repository.AuthRepositoryImpl
import com.clogs.disclogs.data.repository.ListRepository
import com.clogs.disclogs.data.repository.ListRepositoryImpl
import com.clogs.disclogs.data.repository.ProfileRepository
import com.clogs.disclogs.data.repository.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseDataSource {
        return FirebaseDataSource(auth, firestore)
    }


    @Provides
    @Singleton
    fun provideSpotifyDataSource(): SpotifyRemoteDataSource {
        return SpotifyRemoteDataSource(NetworkModule.spotifyApi)
    }

    @Provides
    @Singleton
    fun provideLastFmDataSource(): LastfmRemoteDataSource {
        return LastfmRemoteDataSource(
            apiService = NetworkModule.lastFmApi,
            apiKey = BuildConfig.LASTFM_KEY
        )
    }

    @Provides
    @Singleton
    fun provideDiscogsDataSource(): DiscogsRemoteDataSource {
        return DiscogsRemoteDataSource(
            apiService = NetworkModule.discogsApi,
            authToken = BuildConfig.DISCOGS_TOKEN
        )
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        spotifyRemoteDataSource: SpotifyRemoteDataSource,
        firebaseDataSource: FirebaseDataSource,
        lastfmRemoteDataSource: LastfmRemoteDataSource,
        discogsRemoteDataSource: DiscogsRemoteDataSource
    ): AlbumRepository {
        return AlbumRepositoryImpl(
            spotifyRemoteDataSource,
            lastfmRemoteDataSource,
            firebaseDataSource,
            discogsRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseDataSource: FirebaseDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseDataSource)
    }


    @Provides
    @Singleton
    fun provideProfileRepository(firebaseDataSource: FirebaseDataSource): ProfileRepository {
        return ProfileRepositoryImpl(firebaseDataSource)
    }

    @Provides
    @Singleton
    fun provideListRepository(
        firebaseDataSource: FirebaseDataSource,
        spotifyRemoteDataSource: SpotifyRemoteDataSource
    ): ListRepository {
        return ListRepositoryImpl(
            firebaseDataSource,
            spotifyRemoteDataSource
        )
    }
}
