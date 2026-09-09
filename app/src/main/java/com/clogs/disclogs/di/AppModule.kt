package com.clogs.disclogs.di

import com.clogs.disclogs.BuildConfig
import com.clogs.disclogs.data.remote.firebase.FirebaseAlbumDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseAuthDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseListDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseProfileDataSource
import com.clogs.disclogs.data.remote.firebase.FirebaseReviewDataSource
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
    fun provideFirebaseAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseAuthDataSource {
        return FirebaseAuthDataSource(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseProfileDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseProfileDataSource {
        return FirebaseProfileDataSource(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAlbumDataSource(
        firestore: FirebaseFirestore
    ): FirebaseAlbumDataSource {
        return FirebaseAlbumDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseReviewDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        albumDataSource: FirebaseAlbumDataSource
    ): FirebaseReviewDataSource {
        return FirebaseReviewDataSource(auth, firestore, albumDataSource)
    }

    @Provides
    @Singleton
    fun provideFirebaseListDataSource(
        firestore: FirebaseFirestore
    ): FirebaseListDataSource {
        return FirebaseListDataSource(firestore)
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
        lastfmRemoteDataSource: LastfmRemoteDataSource,
        authDataSource: FirebaseAuthDataSource,
        albumDataSource: FirebaseAlbumDataSource,
        reviewDataSource: FirebaseReviewDataSource,
        discogsRemoteDataSource: DiscogsRemoteDataSource
    ): AlbumRepository {
        return AlbumRepositoryImpl(
            spotifyRemoteDataSource,
            lastfmRemoteDataSource,
            authDataSource,
            albumDataSource,
            reviewDataSource,
            discogsRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: FirebaseAuthDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(authDataSource)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileDataSource: FirebaseProfileDataSource,
        reviewDataSource: FirebaseReviewDataSource
    ): ProfileRepository {
        return ProfileRepositoryImpl(profileDataSource, reviewDataSource)
    }

    @Provides
    @Singleton
    fun provideListRepository(
        listDataSource: FirebaseListDataSource,
        albumDataSource: FirebaseAlbumDataSource,
        spotifyRemoteDataSource: SpotifyRemoteDataSource
    ): ListRepository {
        return ListRepositoryImpl(
            listDataSource,
            albumDataSource,
            spotifyRemoteDataSource
        )
    }
}
