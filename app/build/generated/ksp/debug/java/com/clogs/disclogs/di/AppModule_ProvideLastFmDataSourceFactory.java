package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource;
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
public final class AppModule_ProvideLastFmDataSourceFactory implements Factory<LastfmRemoteDataSource> {
  @Override
  public LastfmRemoteDataSource get() {
    return provideLastFmDataSource();
  }

  public static AppModule_ProvideLastFmDataSourceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LastfmRemoteDataSource provideLastFmDataSource() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLastFmDataSource());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideLastFmDataSourceFactory INSTANCE = new AppModule_ProvideLastFmDataSourceFactory();
  }
}
