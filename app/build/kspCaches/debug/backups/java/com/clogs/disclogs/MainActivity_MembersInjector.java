package com.clogs.disclogs;

import com.clogs.disclogs.data.repository.ProfileRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private MainActivity_MembersInjector(Provider<SupabaseClient> supabaseClientProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectSupabaseClient(instance, supabaseClientProvider.get());
    injectProfileRepository(instance, profileRepositoryProvider.get());
  }

  public static MembersInjector<MainActivity> create(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new MainActivity_MembersInjector(supabaseClientProvider, profileRepositoryProvider);
  }

  @InjectedFieldSignature("com.clogs.disclogs.MainActivity.supabaseClient")
  public static void injectSupabaseClient(MainActivity instance, SupabaseClient supabaseClient) {
    instance.supabaseClient = supabaseClient;
  }

  @InjectedFieldSignature("com.clogs.disclogs.MainActivity.profileRepository")
  public static void injectProfileRepository(MainActivity instance,
      ProfileRepository profileRepository) {
    instance.profileRepository = profileRepository;
  }
}
