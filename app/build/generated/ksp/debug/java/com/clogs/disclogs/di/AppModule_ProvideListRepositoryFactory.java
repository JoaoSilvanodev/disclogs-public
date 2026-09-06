package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import com.clogs.disclogs.data.repository.ListRepository;
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
public final class AppModule_ProvideListRepositoryFactory implements Factory<ListRepository> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private final Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider;

  private AppModule_ProvideListRepositoryFactory(
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
    this.spotifyRemoteDataSourceProvider = spotifyRemoteDataSourceProvider;
  }

  @Override
  public ListRepository get() {
    return provideListRepository(firebaseDataSourceProvider.get(), spotifyRemoteDataSourceProvider.get());
  }

  public static AppModule_ProvideListRepositoryFactory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider,
      Provider<SpotifyRemoteDataSource> spotifyRemoteDataSourceProvider) {
    return new AppModule_ProvideListRepositoryFactory(firebaseDataSourceProvider, spotifyRemoteDataSourceProvider);
  }

  public static ListRepository provideListRepository(FirebaseDataSource firebaseDataSource,
      SpotifyRemoteDataSource spotifyRemoteDataSource) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideListRepository(firebaseDataSource, spotifyRemoteDataSource));
  }
}
