package com.clogs.disclogs.di;

import com.clogs.disclogs.data.repository.ProfileRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideProfileRepositoryFactory implements Factory<ProfileRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private AppModule_ProvideProfileRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public ProfileRepository get() {
    return provideProfileRepository(supabaseClientProvider.get());
  }

  public static AppModule_ProvideProfileRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AppModule_ProvideProfileRepositoryFactory(supabaseClientProvider);
  }

  public static ProfileRepository provideProfileRepository(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProfileRepository(supabaseClient));
  }
}
