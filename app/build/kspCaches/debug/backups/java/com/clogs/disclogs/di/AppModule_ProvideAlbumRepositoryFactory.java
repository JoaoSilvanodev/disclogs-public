package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource;
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import com.clogs.disclogs.data.repository.AlbumRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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

  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private final Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider;

  private final Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider;

  private AppModule_ProvideAlbumRepositoryFactory(
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider,
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider,
      Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider) {
    this.spotifyRemoteDataSourceProvider = spotifyRemoteDataSourceProvider;
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
    this.lastfmRemoteDataSourceProvider = lastfmRemoteDataSourceProvider;
    this.discogsRemoteDataSourceProvider = discogsRemoteDataSourceProvider;
  }

  @Override
  public AlbumRepository get() {
    return provideAlbumRepository(spotifyRemoteDataSourceProvider.get(), firebaseDataSourceProvider.get(), lastfmRemoteDataSourceProvider.get(), discogsRemoteDataSourceProvider.get());
  }

  public static AppModule_ProvideAlbumRepositoryFactory create(
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider,
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider,
      Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider) {
    return new AppModule_ProvideAlbumRepositoryFactory(spotifyRemoteDataSourceProvider, firebaseDataSourceProvider, lastfmRemoteDataSourceProvider, discogsRemoteDataSourceProvider);
  }

  public static AlbumRepository provideAlbumRepository(
      SpotifyRemoteDataSource spotifyRemoteDataSource, FirebaseDataSource firebaseDataSource,
      LastfmRemoteDataSource lastfmRemoteDataSource,
      DiscogsRemoteDataSource discogsRemoteDataSource) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAlbumRepository(spotifyRemoteDataSource, firebaseDataSource, lastfmRemoteDataSource, discogsRemoteDataSource));
  }
}
