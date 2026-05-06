package com.clogs.disclogs


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.clogs.disclogs.data.repository.ProfileRepository
import com.clogs.disclogs.ui.components.ViewAllComponent
import com.clogs.disclogs.ui.components.bottomnav.Constans
import com.clogs.disclogs.ui.screens.auth.AuthScreen
import com.clogs.disclogs.ui.screens.auth.AuthViewModel
import com.clogs.disclogs.ui.screens.details.AlbumDetailScreen
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel
import com.clogs.disclogs.ui.screens.details.ArtistScreen
import com.clogs.disclogs.ui.screens.home.HomeScreen
import com.clogs.disclogs.ui.screens.home.HomeViewModel
import com.clogs.disclogs.ui.screens.library.LibraryScreen
import com.clogs.disclogs.ui.screens.profile.ProfileRoute
import com.clogs.disclogs.ui.screens.search.SearchScreen
import com.clogs.disclogs.ui.screens.search.SearchViewModel
import com.clogs.disclogs.ui.screens.settings.SettingsScreen
import com.clogs.disclogs.ui.theme.DisclogsRed
import com.clogs.disclogs.ui.theme.DisclogsTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabaseClient: SupabaseClient

    @Inject
    lateinit var profileRepository: ProfileRepository
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            captureAndSaveFcmToken()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel()

        supabaseClient.handleDeeplinks(intent)

        setContent {
            DisclogsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisclogsApp()
                }
            }

        }
    }

    fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                captureAndSaveFcmToken()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            captureAndSaveFcmToken()
        }
    }

    private fun captureAndSaveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val token = task.result

            lifecycleScope.launch {
                profileRepository.updateFcmToken(token)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        supabaseClient.handleDeeplinks(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notificações do Disclogs"
            val descriptionText = "Avisos de novos seguidores e interações"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("DISCLOGS_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun DisclogsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "auth") {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { padding ->
        NavHostContainer(
            navController = navController,
            paddingValues = padding
        )
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel = hiltViewModel()

) {

    val sessionStatus by authViewModel.sessionStatus.collectAsState()

    when (sessionStatus) {
        is SessionStatus.Initializing -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        else -> {
            val startDest = if (sessionStatus is SessionStatus.Authenticated) "home" else "auth"



            NavHost(
                navController = navController,
                startDestination = "auth",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("auth") {

                    val authViewModel = hiltViewModel<AuthViewModel>()
                    val sessionStatus by authViewModel.sessionStatus.collectAsState()

                    val context = LocalContext.current
                    val mainActivity = context as? MainActivity


                    LaunchedEffect(sessionStatus) {

                        if (sessionStatus is SessionStatus.Authenticated) {

                            try {
                                mainActivity?.askNotificationPermission()

                                navController.navigate("home") {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    AuthScreen(authViewModel)
                }

                composable("home") {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(
                        homeViewModel,
                        onNavigateToDetails = { albumId ->
                            navController.navigate("details/$albumId")
                        },
                        onNavigateToAll = { type ->
                            navController.navigate("view_all/$type")
                        }
                    )
                }

                composable("search") {
                    val searchViewModel: SearchViewModel = hiltViewModel()
                    SearchScreen(
                        onNavigateToDetails = { albumId ->
                            navController.navigate("details/$albumId")
                        },
                        onBackClick = { navController.popBackStack() },
                        onNavigateToArtist = { artistId ->
                            navController.navigate("artist/$artistId")
                        },
                        onNavigateToUser = { userId ->
                            navController.navigate("user_profile/${userId}")
                        }
                    )
                }

                composable("library") {
                    LibraryScreen(
                        onNavigateToDetails = { albumId -> navController.navigate("details/${albumId}") },
                    )
                }

                composable("profile") {
                    ProfileRoute(
                        viewModel = hiltViewModel(),
                        targetUserId = null,
                        onSettingsClick = { navController.navigate("settings") },
                        onAlbumClick = { albumId ->
                            navController.navigate("details/$albumId")
                        }

                    )
                }

                composable("user_profile/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")

                    ProfileRoute(
                        viewModel = hiltViewModel(),
                        targetUserId = userId,
                        onSettingsClick = {},
                        onAlbumClick = { albumId ->
                            navController.navigate("details/${albumId}")
                        }
                    )
                }

                composable("details/{albumId}") { backStackEntry ->
                    val albumId = backStackEntry.arguments?.getString("albumId") ?: ""

                    val detailsViewModel: AlbumDetailViewModel = hiltViewModel()
                    AlbumDetailScreen(
                        albumId = albumId,
                        viewModel = detailsViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("artist/{artistId}") { backStackEntry ->
                    val artistId = backStackEntry.arguments?.getString("artistId") ?: ""

                    ArtistScreen(
                        artistId = artistId,
                        viewModel = hiltViewModel(),
                        onBackClick = { navController.popBackStack() },
                        onNavigateToDetails = { albumId -> navController.navigate("details/${albumId}") },
                    )
                }


                composable("settings") {
                    SettingsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("view_all/{type}") { backStackEntry ->
                    val type = backStackEntry.arguments?.getString("type") ?: "trending"

                    val viewModel: HomeViewModel = hiltViewModel()
                    val state by viewModel.uiState.collectAsState()

                    val (title, list) = when (type) {
                        "trending" -> Pair("Em Alta", state.trendingAlbums)
                        "friends" -> Pair("Amigos Ouvindo", state.friendAlbum)
                        else -> Pair("Álbuns", emptyList())
                    }
                    ViewAllComponent(
                        title = title,
                        albumList = list,
                        onAlbumClick = { albumId -> navController.navigate("details/$albumId") },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constans.BottomNavItems.forEach { item ->

            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) DisclogsRed else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) DisclogsRed else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                },

                alwaysShowLabel = false,

                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}


@Preview(showBackground = false, showSystemUi = false)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    DisclogsTheme {
        BottomNavigationBar(navController = navController)
    }
}
