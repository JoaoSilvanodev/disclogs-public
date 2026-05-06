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
public final class ArtistViewModel_Factory implements Factory<ArtistViewModel> {
  private final Provider<AlbumRepository> repositoryProvider;

  private ArtistViewModel_Factory(Provider<AlbumRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ArtistViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ArtistViewModel_Factory create(Provider<AlbumRepository> repositoryProvider) {
    return new ArtistViewModel_Factory(repositoryProvider);
  }

  public static ArtistViewModel newInstance(AlbumRepository repository) {
    return new ArtistViewModel(repository);
  }
}
