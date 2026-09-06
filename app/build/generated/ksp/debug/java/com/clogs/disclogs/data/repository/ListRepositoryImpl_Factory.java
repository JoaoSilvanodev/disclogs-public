package com.clogs.disclogs.data.repository;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
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
public final class ListRepositoryImpl_Factory implements Factory<ListRepositoryImpl> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private final Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider;

  private ListRepositoryImpl_Factory(Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
    this.spotifyRemoteDataSourceProvider = spotifyRemoteDataSourceProvider;
  }

  @Override
  public ListRepositoryImpl get() {
    return newInstance(firebaseDataSourceProvider.get(), spotifyRemoteDataSourceProvider.get());
  }

  public static ListRepositoryImpl_Factory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider) {
    return new ListRepositoryImpl_Factory(firebaseDataSourceProvider, spotifyRemoteDataSourceProvider);
  }

  public static ListRepositoryImpl newInstance(FirebaseDataSource firebaseDataSource,
      SpotifyRemoteDataSource spotifyRemoteDataSource) {
    return new ListRepositoryImpl(firebaseDataSource, spotifyRemoteDataSource);
  }
}
