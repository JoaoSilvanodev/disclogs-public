package com.clogs.disclogs.di;

import com.clogs.disclogs.data.repository.AuthRepository;
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
public final class AppModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private AppModule_ProvideAuthRepositoryFactory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(supabaseClientProvider.get());
  }

  public static AppModule_ProvideAuthRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AppModule_ProvideAuthRepositoryFactory(supabaseClientProvider);
  }

  public static AuthRepository provideAuthRepository(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAuthRepository(supabaseClient));
  }
}
