package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideSpotifyDataSourceFactory implements Factory<SpotifyRemoteDataSource> {
  @Override
  public SpotifyRemoteDataSource get() {
    return provideSpotifyDataSource();
  }

  public static AppModule_ProvideSpotifyDataSourceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SpotifyRemoteDataSource provideSpotifyDataSource() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSpotifyDataSource());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideSpotifyDataSourceFactory INSTANCE = new AppModule_ProvideSpotifyDataSourceFactory();
  }
}
