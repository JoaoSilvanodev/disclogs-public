package com.clogs.disclogs.data.repository;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource;
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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

  private final Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider;

  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private final Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider;

  private AlbumRepositoryImpl_Factory(Provider<SpotifyRemoteDataSource> spotifyDataSourceProvider,
      Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider,
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider) {
    this.spotifyDataSourceProvider = spotifyDataSourceProvider;
    this.lastfmRemoteDataSourceProvider = lastfmRemoteDataSourceProvider;
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
    this.discogsRemoteDataSourceProvider = discogsRemoteDataSourceProvider;
  }

  @Override
  public AlbumRepositoryImpl get() {
    return newInstance(spotifyDataSourceProvider.get(), lastfmRemoteDataSourceProvider.get(), firebaseDataSourceProvider.get(), discogsRemoteDataSourceProvider.get());
  }

  public static AlbumRepositoryImpl_Factory create(
      Provider<SpotifyRemoteDataSource> spotifyDataSourceProvider,
      Provider<LastfmRemoteDataSource> lastfmRemoteDataSourceProvider,
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<DiscogsRemoteDataSource> discogsRemoteDataSourceProvider) {
    return new AlbumRepositoryImpl_Factory(spotifyDataSourceProvider, lastfmRemoteDataSourceProvider, firebaseDataSourceProvider, discogsRemoteDataSourceProvider);
  }

  public static AlbumRepositoryImpl newInstance(SpotifyRemoteDataSource spotifyDataSource,
      LastfmRemoteDataSource lastfmRemoteDataSource, FirebaseDataSource firebaseDataSource,
      DiscogsRemoteDataSource discogsRemoteDataSource) {
    return new AlbumRepositoryImpl(spotifyDataSource, lastfmRemoteDataSource, firebaseDataSource, discogsRemoteDataSource);
  }
}
