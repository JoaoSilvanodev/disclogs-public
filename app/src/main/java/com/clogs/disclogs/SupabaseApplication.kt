package com.clogs.disclogs

import android.app.Application
import com.clogs.disclogs.data.remote.SupabaseDataSource
import com.clogs.disclogs.di.NetworkModule
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
import com.clogs.disclogs.data.repository.AlbumRepository
import com.clogs.disclogs.data.repository.AlbumRepositoryImpl
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


@HiltAndroidApp
class SupabaseApplication : Application() {
}