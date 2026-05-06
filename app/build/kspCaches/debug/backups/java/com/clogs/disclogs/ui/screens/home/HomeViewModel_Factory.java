package com.clogs.disclogs.ui.screens.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AlbumRepository> repositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private HomeViewModel_Factory(Provider<AlbumRepository> repositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AlbumRepository> repositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new HomeViewModel_Factory(repositoryProvider, profileRepositoryProvider);
  }

  public static HomeViewModel newInstance(AlbumRepository repository,
      ProfileRepository profileRepository) {
    return new HomeViewModel(repository, profileRepository);
  }
}
