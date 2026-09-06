package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.repository.ProfileRepository;
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
public final class AppModule_ProvideProfileRepositoryFactory implements Factory<ProfileRepository> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private AppModule_ProvideProfileRepositoryFactory(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
  }

  @Override
  public ProfileRepository get() {
    return provideProfileRepository(firebaseDataSourceProvider.get());
  }

  public static AppModule_ProvideProfileRepositoryFactory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    return new AppModule_ProvideProfileRepositoryFactory(firebaseDataSourceProvider);
  }

  public static ProfileRepository provideProfileRepository(FirebaseDataSource firebaseDataSource) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProfileRepository(firebaseDataSource));
  }
}
