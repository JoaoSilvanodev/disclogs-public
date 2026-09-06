package com.clogs.disclogs.di;

import com.clogs.disclogs.data.remote.FirebaseDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class AppModule_ProvideFirebaseDataSourceFactory implements Factory<FirebaseDataSource> {
  private final Provider<FirebaseAuth> authProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private AppModule_ProvideFirebaseDataSourceFactory(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.authProvider = authProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FirebaseDataSource get() {
    return provideFirebaseDataSource(authProvider.get(), firestoreProvider.get());
  }

  public static AppModule_ProvideFirebaseDataSourceFactory create(
      Provider<FirebaseAuth> authProvider, Provider<FirebaseFirestore> firestoreProvider) {
    return new AppModule_ProvideFirebaseDataSourceFactory(authProvider, firestoreProvider);
  }

  public static FirebaseDataSource provideFirebaseDataSource(FirebaseAuth auth,
      FirebaseFirestore firestore) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFirebaseDataSource(auth, firestore));
  }
}
