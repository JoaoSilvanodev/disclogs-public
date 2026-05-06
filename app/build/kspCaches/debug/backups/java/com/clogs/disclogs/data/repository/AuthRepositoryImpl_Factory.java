package com.clogs.disclogs.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<SupabaseClient> supabaseProvider;

  private AuthRepositoryImpl_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(supabaseProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new AuthRepositoryImpl_Factory(supabaseProvider);
  }

  public static AuthRepositoryImpl newInstance(SupabaseClient supabase) {
    return new AuthRepositoryImpl(supabase);
  }
}
