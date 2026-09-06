package com.clogs.disclogs.ui.screens.library;

import com.clogs.disclogs.data.repository.AlbumRepository;
import com.clogs.disclogs.data.repository.ListRepository;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<AlbumRepository> repositoryProvider;

  private final Provider<ListRepository> listRepositoryProvider;

  private LibraryViewModel_Factory(Provider<AlbumRepository> repositoryProvider,
      Provider<ListRepository> listRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.listRepositoryProvider = listRepositoryProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(repositoryProvider.get(), listRepositoryProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<AlbumRepository> repositoryProvider,
      Provider<ListRepository> listRepositoryProvider) {
    return new LibraryViewModel_Factory(repositoryProvider, listRepositoryProvider);
  }

  public static LibraryViewModel newInstance(AlbumRepository repository,
      ListRepository listRepository) {
    return new LibraryViewModel(repository, listRepository);
  }
}
