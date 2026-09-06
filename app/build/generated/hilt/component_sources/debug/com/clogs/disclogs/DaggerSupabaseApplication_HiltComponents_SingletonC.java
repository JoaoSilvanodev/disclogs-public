package com.clogs.disclogs;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource;
import com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource;
import com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource;
import com.clogs.disclogs.data.repository.AlbumRepository;
import com.clogs.disclogs.data.repository.AuthRepository;
import com.clogs.disclogs.data.repository.ListRepository;
import com.clogs.disclogs.data.repository.ProfileRepository;
import com.clogs.disclogs.di.AppModule_ProvideAlbumRepositoryFactory;
import com.clogs.disclogs.di.AppModule_ProvideAuthRepositoryFactory;
import com.clogs.disclogs.di.AppModule_ProvideDiscogsDataSourceFactory;
import com.clogs.disclogs.di.AppModule_ProvideFirebaseAuthFactory;
import com.clogs.disclogs.di.AppModule_ProvideFirebaseDataSourceFactory;
import com.clogs.disclogs.di.AppModule_ProvideFirebaseFirestoreFactory;
import com.clogs.disclogs.di.AppModule_ProvideLastFmDataSourceFactory;
import com.clogs.disclogs.di.AppModule_ProvideListRepositoryFactory;
import com.clogs.disclogs.di.AppModule_ProvideProfileRepositoryFactory;
import com.clogs.disclogs.di.AppModule_ProvideSpotifyDataSourceFactory;
import com.clogs.disclogs.ui.screens.auth.AuthViewModel;
import com.clogs.disclogs.ui.screens.auth.AuthViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.auth.AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.auth.AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel;
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.details.ArtistViewModel;
import com.clogs.disclogs.ui.screens.details.ArtistViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.details.ArtistViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.details.ArtistViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.home.HomeViewModel;
import com.clogs.disclogs.ui.screens.home.HomeViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.library.LibraryViewModel;
import com.clogs.disclogs.ui.screens.library.LibraryViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.library.LibraryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.library.LibraryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.library.list.ListViewModel;
import com.clogs.disclogs.ui.screens.library.list.ListViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.library.list.ListViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.library.list.ListViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.profile.ProfileViewModel;
import com.clogs.disclogs.ui.screens.profile.ProfileViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.profile.ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.profile.ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.search.SearchViewModel;
import com.clogs.disclogs.ui.screens.search.SearchViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.search.SearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.search.SearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.clogs.disclogs.ui.screens.settings.SettingsViewModel;
import com.clogs.disclogs.ui.screens.settings.SettingsViewModel_HiltModules;
import com.clogs.disclogs.ui.screens.settings.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.clogs.disclogs.ui.screens.settings.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerSupabaseApplication_HiltComponents_SingletonC {
  private DaggerSupabaseApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static SupabaseApplication_HiltComponents.SingletonC create() {
    return new Builder().build();
  }

  public static final class Builder {
    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SupabaseApplication_HiltComponents.SingletonC build() {
      return new SingletonCImpl();
    }
  }

  private static final class ActivityRetainedCBuilder implements SupabaseApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SupabaseApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SupabaseApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SupabaseApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SupabaseApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SupabaseApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SupabaseApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SupabaseApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SupabaseApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SupabaseApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    FragmentCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SupabaseApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SupabaseApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    ActivityCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    ImmutableMap keySetMapOfClassOfObjectAndBooleanBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, Boolean>builderWithExpectedSize(9);
      mapBuilder.put(AlbumDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AlbumDetailViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ArtistViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ArtistViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AuthViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(LibraryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, LibraryViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ListViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ListViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProfileViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(SearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SearchViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide());
      return mapBuilder.build();
    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(keySetMapOfClassOfObjectAndBooleanBuilder());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectProfileRepository(instance, singletonCImpl.provideProfileRepositoryProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends SupabaseApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    Provider<AlbumDetailViewModel> albumDetailViewModelProvider;

    Provider<ArtistViewModel> artistViewModelProvider;

    Provider<AuthViewModel> authViewModelProvider;

    Provider<HomeViewModel> homeViewModelProvider;

    Provider<LibraryViewModel> libraryViewModelProvider;

    Provider<ListViewModel> listViewModelProvider;

    Provider<ProfileViewModel> profileViewModelProvider;

    Provider<SearchViewModel> searchViewModelProvider;

    Provider<SettingsViewModel> settingsViewModelProvider;

    ViewModelCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        SavedStateHandle savedStateHandleParam, ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    ImmutableMap hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(9);
      mapBuilder.put(AlbumDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (albumDetailViewModelProvider)));
      mapBuilder.put(ArtistViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (artistViewModelProvider)));
      mapBuilder.put(AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (authViewModelProvider)));
      mapBuilder.put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (homeViewModelProvider)));
      mapBuilder.put(LibraryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (libraryViewModelProvider)));
      mapBuilder.put(ListViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (listViewModelProvider)));
      mapBuilder.put(ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (profileViewModelProvider)));
      mapBuilder.put(SearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (searchViewModelProvider)));
      mapBuilder.put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (settingsViewModelProvider)));
      return mapBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.albumDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.artistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.libraryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.listViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel
          return (T) new AlbumDetailViewModel(singletonCImpl.provideAlbumRepositoryProvider.get());

          case 1: // com.clogs.disclogs.ui.screens.details.ArtistViewModel
          return (T) new ArtistViewModel(singletonCImpl.provideAlbumRepositoryProvider.get());

          case 2: // com.clogs.disclogs.ui.screens.auth.AuthViewModel
          return (T) new AuthViewModel(singletonCImpl.provideAuthRepositoryProvider.get());

          case 3: // com.clogs.disclogs.ui.screens.home.HomeViewModel
          return (T) new HomeViewModel(singletonCImpl.provideAlbumRepositoryProvider.get(), singletonCImpl.provideProfileRepositoryProvider.get());

          case 4: // com.clogs.disclogs.ui.screens.library.LibraryViewModel
          return (T) new LibraryViewModel(singletonCImpl.provideAlbumRepositoryProvider.get(), singletonCImpl.provideListRepositoryProvider.get());

          case 5: // com.clogs.disclogs.ui.screens.library.list.ListViewModel
          return (T) new ListViewModel(singletonCImpl.provideListRepositoryProvider.get());

          case 6: // com.clogs.disclogs.ui.screens.profile.ProfileViewModel
          return (T) new ProfileViewModel(singletonCImpl.provideProfileRepositoryProvider.get());

          case 7: // com.clogs.disclogs.ui.screens.search.SearchViewModel
          return (T) new SearchViewModel(singletonCImpl.provideAlbumRepositoryProvider.get(), singletonCImpl.provideProfileRepositoryProvider.get());

          case 8: // com.clogs.disclogs.ui.screens.settings.SettingsViewModel
          return (T) new SettingsViewModel(singletonCImpl.provideProfileRepositoryProvider.get(), singletonCImpl.provideAlbumRepositoryProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SupabaseApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SupabaseApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends SupabaseApplication_HiltComponents.SingletonC {
    private final SingletonCImpl singletonCImpl = this;

    Provider<FirebaseAuth> provideFirebaseAuthProvider;

    Provider<FirebaseFirestore> provideFirebaseFirestoreProvider;

    Provider<FirebaseDataSource> provideFirebaseDataSourceProvider;

    Provider<ProfileRepository> provideProfileRepositoryProvider;

    Provider<SpotifyRemoteDataSource> provideSpotifyDataSourceProvider;

    Provider<LastfmRemoteDataSource> provideLastFmDataSourceProvider;

    Provider<DiscogsRemoteDataSource> provideDiscogsDataSourceProvider;

    Provider<AlbumRepository> provideAlbumRepositoryProvider;

    Provider<AuthRepository> provideAuthRepositoryProvider;

    Provider<ListRepository> provideListRepositoryProvider;

    SingletonCImpl() {

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 2));
      this.provideFirebaseFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 3));
      this.provideFirebaseDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseDataSource>(singletonCImpl, 1));
      this.provideProfileRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProfileRepository>(singletonCImpl, 0));
      this.provideSpotifyDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<SpotifyRemoteDataSource>(singletonCImpl, 5));
      this.provideLastFmDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LastfmRemoteDataSource>(singletonCImpl, 6));
      this.provideDiscogsDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<DiscogsRemoteDataSource>(singletonCImpl, 7));
      this.provideAlbumRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AlbumRepository>(singletonCImpl, 4));
      this.provideAuthRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 8));
      this.provideListRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ListRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectSupabaseApplication(SupabaseApplication supabaseApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.clogs.disclogs.data.repository.ProfileRepository
          return (T) AppModule_ProvideProfileRepositoryFactory.provideProfileRepository(singletonCImpl.provideFirebaseDataSourceProvider.get());

          case 1: // com.clogs.disclogs.data.remote.FirebaseDataSource
          return (T) AppModule_ProvideFirebaseDataSourceFactory.provideFirebaseDataSource(singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 2: // com.google.firebase.auth.FirebaseAuth
          return (T) AppModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 3: // com.google.firebase.firestore.FirebaseFirestore
          return (T) AppModule_ProvideFirebaseFirestoreFactory.provideFirebaseFirestore();

          case 4: // com.clogs.disclogs.data.repository.AlbumRepository
          return (T) AppModule_ProvideAlbumRepositoryFactory.provideAlbumRepository(singletonCImpl.provideSpotifyDataSourceProvider.get(), singletonCImpl.provideFirebaseDataSourceProvider.get(), singletonCImpl.provideLastFmDataSourceProvider.get(), singletonCImpl.provideDiscogsDataSourceProvider.get());

          case 5: // com.clogs.disclogs.data.remote.spotify.SpotifyRemoteDataSource
          return (T) AppModule_ProvideSpotifyDataSourceFactory.provideSpotifyDataSource();

          case 6: // com.clogs.disclogs.data.remote.lastfm.LastfmRemoteDataSource
          return (T) AppModule_ProvideLastFmDataSourceFactory.provideLastFmDataSource();

          case 7: // com.clogs.disclogs.data.remote.discogs.DiscogsRemoteDataSource
          return (T) AppModule_ProvideDiscogsDataSourceFactory.provideDiscogsDataSource();

          case 8: // com.clogs.disclogs.data.repository.AuthRepository
          return (T) AppModule_ProvideAuthRepositoryFactory.provideAuthRepository(singletonCImpl.provideFirebaseDataSourceProvider.get());

          case 9: // com.clogs.disclogs.data.repository.ListRepository
          return (T) AppModule_ProvideListRepositoryFactory.provideListRepository(singletonCImpl.provideFirebaseDataSourceProvider.get(), singletonCImpl.provideSpotifyDataSourceProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
