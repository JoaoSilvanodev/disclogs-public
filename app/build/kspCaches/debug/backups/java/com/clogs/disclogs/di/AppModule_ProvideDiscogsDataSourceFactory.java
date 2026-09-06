package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource;
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
public final class AppModule_ProvideDiscogsDataSourceFactory implements Factory<DiscogsRemoteDataSource> {
  @Override
  public DiscogsRemoteDataSource get() {
    return provideDiscogsDataSource();
  }

  public static AppModule_ProvideDiscogsDataSourceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DiscogsRemoteDataSource provideDiscogsDataSource() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDiscogsDataSource());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideDiscogsDataSourceFactory INSTANCE = new AppModule_ProvideDiscogsDataSourceFactory();
  }
}
