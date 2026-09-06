package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.repository.AuthRepository;
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
public final class AppModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private AppModule_ProvideAuthRepositoryFactory(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(firebaseDataSourceProvider.get());
  }

  public static AppModule_ProvideAuthRepositoryFactory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    return new AppModule_ProvideAuthRepositoryFactory(firebaseDataSourceProvider);
  }

  public static AuthRepository provideAuthRepository(FirebaseDataSource firebaseDataSource) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAuthRepository(firebaseDataSource));
  }
}
