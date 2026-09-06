package com.clogs.disclogs.data.repository;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private AuthRepositoryImpl_Factory(Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(firebaseDataSourceProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    return new AuthRepositoryImpl_Factory(firebaseDataSourceProvider);
  }

  public static AuthRepositoryImpl newInstance(FirebaseDataSource firebaseDataSource) {
    return new AuthRepositoryImpl(firebaseDataSource);
  }
}
