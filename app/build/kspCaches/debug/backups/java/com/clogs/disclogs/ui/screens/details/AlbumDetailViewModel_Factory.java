package com.clogs.disclogs.ui.screens.details;

import com.clogs.disclogs.data.repository.AlbumRepository;
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
public final class AlbumDetailViewModel_Factory implements Factory<AlbumDetailViewModel> {
  private final Provider<AlbumRepository> repositoryProvider;

  private AlbumDetailViewModel_Factory(Provider<AlbumRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AlbumDetailViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AlbumDetailViewModel_Factory create(Provider<AlbumRepository> repositoryProvider) {
    return new AlbumDetailViewModel_Factory(repositoryProvider);
  }

  public static AlbumDetailViewModel newInstance(AlbumRepository repository) {
    return new AlbumDetailViewModel(repository);
  }
}
