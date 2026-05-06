package com.clogs.disclogs.di

import com.clogs.disclogs.BuildConfig
import com.clogs.disclogs.data.remote.SupabaseDataSource
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.AlbumRepositoryImpl
import com.clogs.disclogs.data.repository.AuthRepository
import com.clogs.disclogs.data.repository.AuthRepositoryImpl
import com.clogs.disclogs.data.repository.ProfileRepository
import com.clogs.disclogs.data.repository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Receita do supabase
    @Provides // Aqui está a receita para fabricar um...
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                scheme = "disclogs"
                host = "login"
            }
            install(Storage)
            install(ComposeAuth)

            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    @Provides // Aqui está a receita para fabricar um...
    @Singleton
    fun provideSupabaseDataSource(supabaseClient: SupabaseClient): SupabaseDataSource {
        return SupabaseDataSource(supabaseClient)
    }


    @Provides // Aqui está a receita para fabricar um...
    @Singleton
    fun provideSpotifyDataSource(): SpotifyRemoteDataSource {
        return SpotifyRemoteDataSource(NetworkModule.spotifyApi)
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        spotifyRemoteDataSource: SpotifyRemoteDataSource,
        supabaseDataSource: SupabaseDataSource,
        supabaseClient: SupabaseClient
    ): AlbumRepository {
        return AlbumRepositoryImpl(spotifyRemoteDataSource, supabaseDataSource, supabaseClient)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseClient: SupabaseClient
    ): AuthRepository {
        return AuthRepositoryImpl(supabaseClient)
    }


    @Provides
    @Singleton
    fun provideProfileRepository(supabaseClient: SupabaseClient): ProfileRepository {
        return ProfileRepositoryImpl(supabaseClient)
    }
}
