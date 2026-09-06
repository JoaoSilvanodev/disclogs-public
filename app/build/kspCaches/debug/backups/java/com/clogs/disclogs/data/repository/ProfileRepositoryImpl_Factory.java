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
public final class ProfileRepositoryImpl_Factory implements Factory<ProfileRepositoryImpl> {
  private final Provider<FirebaseDataSource> firebaseDataSourceProvider;

  private ProfileRepositoryImpl_Factory(Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    this.firebaseDataSourceProvider = firebaseDataSourceProvider;
  }

  @Override
  public ProfileRepositoryImpl get() {
    return newInstance(firebaseDataSourceProvider.get());
  }

  public static ProfileRepositoryImpl_Factory create(
      Provider<FirebaseDataSource> firebaseDataSourceProvider) {
    return new ProfileRepositoryImpl_Factory(firebaseDataSourceProvider);
  }

  public static ProfileRepositoryImpl newInstance(FirebaseDataSource firebaseDataSource) {
    return new ProfileRepositoryImpl(firebaseDataSource);
  }
}
