package com.clogs.disclogs.data.repository;

import com.clogs.disclogs.data.remote.SupabaseDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AlbumRepositoryImpl_Factory implements Factory<AlbumRepositoryImpl> {
  private final Provider<SpotifyRemoteDataSource> spotifyDataSourceProvider;

  private final Provider<SupabaseDataSource> supabaseDataSourceProvider;

  private final Provider<SupabaseClient> supabaseClientProvider;

  private AlbumRepositoryImpl_Factory(Provider<SpotifyRemoteDataSource> spotifyDataSourceProvider,
      Provider<SupabaseDataSource> supabaseDataSourceProvider,
      Provider<SupabaseClient> supabaseClientProvider) {
    this.spotifyDataSourceProvider = spotifyDataSourceProvider;
    this.supabaseDataSourceProvider = supabaseDataSourceProvider;
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AlbumRepositoryImpl get() {
    return newInstance(spotifyDataSourceProvider.get(), supabaseDataSourceProvider.get(), supabaseClientProvider.get());
  }

  public static AlbumRepositoryImpl_Factory create(
      Provider<SpotifyRemoteDataSource> spotifyDataSourceProvider,
      Provider<SupabaseDataSource> supabaseDataSourceProvider,
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AlbumRepositoryImpl_Factory(spotifyDataSourceProvider, supabaseDataSourceProvider, supabaseClientProvider);
  }

  public static AlbumRepositoryImpl newInstance(SpotifyRemoteDataSource spotifyDataSource,
      SupabaseDataSource supabaseDataSource, SupabaseClient supabaseClient) {
    return new AlbumRepositoryImpl(spotifyDataSource, supabaseDataSource, supabaseClient);
  }
}
