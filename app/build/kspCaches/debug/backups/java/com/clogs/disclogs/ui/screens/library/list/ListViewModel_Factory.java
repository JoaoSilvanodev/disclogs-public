package com.clogs.disclogs.ui.screens.library.list;

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
public final class ListViewModel_Factory implements Factory<ListViewModel> {
  private final Provider<ListRepository> listRepositoryProvider;

  private ListViewModel_Factory(Provider<ListRepository> listRepositoryProvider) {
    this.listRepositoryProvider = listRepositoryProvider;
  }

  @Override
  public ListViewModel get() {
    return newInstance(listRepositoryProvider.get());
  }

  public static ListViewModel_Factory create(Provider<ListRepository> listRepositoryProvider) {
    return new ListViewModel_Factory(listRepositoryProvider);
  }

  public static ListViewModel newInstance(ListRepository listRepository) {
    return new ListViewModel(listRepository);
  }
}
