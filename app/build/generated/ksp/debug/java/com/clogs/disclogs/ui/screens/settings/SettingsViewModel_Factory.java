package com.clogs.disclogs.ui.screens.settings;

import com.clogs.disclogs.data.repository.AlbumRepository;
import com.clogs.disclogs.data.repository.AuthRepository;
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

  private final Provider<AuthRepository> authRepositoryProvider;

  private SettingsViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<AlbumRepository> albumRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.albumRepositoryProvider = albumRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(profileRepositoryProvider.get(), albumRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<AlbumRepository> albumRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SettingsViewModel_Factory(profileRepositoryProvider, albumRepositoryProvider, authRepositoryProvider);
  }

  public static SettingsViewModel newInstance(ProfileRepository profileRepository,
      AlbumRepository albumRepository, AuthRepository authRepository) {
    return new SettingsViewModel(profileRepository, albumRepository, authRepository);
  }
}
