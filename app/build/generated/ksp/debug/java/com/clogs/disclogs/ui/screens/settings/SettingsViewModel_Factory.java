package com.clogs.disclogs.ui.screens.settings;

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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<AlbumRepository> albumRepositoryProvider;

  private SettingsViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<AlbumRepository> albumRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.albumRepositoryProvider = albumRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(profileRepositoryProvider.get(), albumRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<AlbumRepository> albumRepositoryProvider) {
    return new SettingsViewModel_Factory(profileRepositoryProvider, albumRepositoryProvider);
  }

  public static SettingsViewModel newInstance(ProfileRepository profileRepository,
      AlbumRepository albumRepository) {
    return new SettingsViewModel(profileRepository, albumRepository);
  }
}
