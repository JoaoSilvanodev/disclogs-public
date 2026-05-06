package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.SupabaseDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import com.clogs.disclogs.data.repository.AlbumRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideAlbumRepositoryFactory implements Factory<AlbumRepository> {
  private final Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider;

  private final Provider<SupabaseDataSource> supabaseDataSourceProvider;

  private final Provider<SupabaseClient> supabaseClientProvider;

  private AppModule_ProvideAlbumRepositoryFactory(
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider,
      Provider<SupabaseDataSource> supabaseDataSourceProvider,
      Provider<SupabaseClient> supabaseClientProvider) {
    this.spotifyRemoteDataSourceProvider = spotifyRemoteDataSourceProvider;
    this.supabaseDataSourceProvider = supabaseDataSourceProvider;
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AlbumRepository get() {
    return provideAlbumRepository(spotifyRemoteDataSourceProvider.get(), supabaseDataSourceProvider.get(), supabaseClientProvider.get());
  }

  public static AppModule_ProvideAlbumRepositoryFactory create(
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider,
      Provider<SupabaseDataSource> supabaseDataSourceProvider,
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AppModule_ProvideAlbumRepositoryFactory(spotifyRemoteDataSourceProvider, supabaseDataSourceProvider, supabaseClientProvider);
  }

  public static AlbumRepository provideAlbumRepository(
      SpotifyRemoteDataSource spotifyRemoteDataSource, SupabaseDataSource supabaseDataSource,
      SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAlbumRepository(spotifyRemoteDataSource, supabaseDataSource, supabaseClient));
  }
}
