package com.clogs.disclogs.ui.screens.search;

import com.clogs.disclogs.data.repository.AlbumRepository;
import com.clogs.disclogs.data.repository.ProfileRepository;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<AlbumRepository> repositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private SearchViewModel_Factory(Provider<AlbumRepository> repositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(repositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<AlbumRepository> repositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new SearchViewModel_Factory(repositoryProvider, profileRepositoryProvider);
  }

  public static SearchViewModel newInstance(AlbumRepository repository,
      ProfileRepository profileRepository) {
    return new SearchViewModel(repository, profileRepository);
  }
}
