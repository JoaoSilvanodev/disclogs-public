package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.SupabaseDataSource;
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
public final class AppModule_ProvideSupabaseDataSourceFactory implements Factory<SupabaseDataSource> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private AppModule_ProvideSupabaseDataSourceFactory(
      Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public SupabaseDataSource get() {
    return provideSupabaseDataSource(supabaseClientProvider.get());
  }

  public static AppModule_ProvideSupabaseDataSourceFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AppModule_ProvideSupabaseDataSourceFactory(supabaseClientProvider);
  }

  public static SupabaseDataSource provideSupabaseDataSource(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSupabaseDataSource(supabaseClient));
  }
}
