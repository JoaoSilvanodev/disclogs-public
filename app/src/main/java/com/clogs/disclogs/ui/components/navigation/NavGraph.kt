package com.clogs.disclogs.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.clogs.disclogs.MainActivity
import com.clogs.disclogs.ui.components.GenericSearch
import com.clogs.disclogs.ui.screens.auth.AuthScreen
import com.clogs.disclogs.ui.screens.auth.AuthViewModel
import com.clogs.disclogs.ui.screens.details.AlbumDetailScreen
import com.clogs.disclogs.ui.screens.details.AlbumDetailViewModel
import com.clogs.disclogs.ui.screens.details.ArtistScreen
import com.clogs.disclogs.ui.screens.home.HomeScreen
import com.clogs.disclogs.ui.screens.home.HomeViewModel
import com.clogs.disclogs.ui.screens.library.LibraryScreen
import com.clogs.disclogs.ui.screens.library.LibraryViewModel
import com.clogs.disclogs.ui.screens.library.list.ListDetailScreen
import com.clogs.disclogs.ui.screens.profile.ProfileRoute
import com.clogs.disclogs.ui.screens.search.SearchScreen
import com.clogs.disclogs.ui.screens.search.SearchViewModel
import com.clogs.disclogs.ui.screens.settings.SettingsScreen
import com.clogs.disclogs.ui.screens.viewall.FriendsFeedScreen
import com.clogs.disclogs.ui.screens.viewall.TrendingRankingScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController, // Controlador de navegação
    paddingValues: PaddingValues, //  Valores de preenchimento para os elementos filhos
    authViewModel: AuthViewModel //  ViewModel para autenticação
) {
    val context = LocalContext.current // Obtém o contexto atual
    val mainActivity = context as? MainActivity // Cast para a classe MainActivity
    val auth = FirebaseAuth.getInstance()
    val uiState by authViewModel.uiState.collectAsState()

    // Monitora mudanças no estado de autenticação (Login/Logout)
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                // Se deslogou, volta para a tela de autenticação
                if (navController.currentDestination?.route != "auth") {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                // Se logou (ou já estava logado), pede permissão de notificação
                mainActivity?.askNotificationPermission()
            }
        }
        auth.addAuthStateListener(listener)
        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    // Navegação automática após login bem-sucedido na AuthScreen
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            if (navController.currentDestination?.route != "home") {
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }
    }

    val startDest = if (auth.currentUser != null) "home" else "auth"

    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("auth") {
            AuthScreen(hiltViewModel())
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

        composable(
            route = "add_to_list/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("id") ?: return@composable

            val libraryViewModel: LibraryViewModel = hiltViewModel()

            GenericSearch(
                onBackClick = { navController.popBackStack() },
                searchType = "Album",
                onAlbumClick = { albumId ->
                    libraryViewModel.addAlbumToList(albumId, listId)
                    navController.popBackStack()
                }
            )
        }



        composable(
            route = "list_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("id") ?: return@composable

            ListDetailScreen(
                listId = listId,
                onBackClick = { navController.popBackStack() },
                onAddAlbumClick = { listId ->
                    navController.navigate("add_to_list/$listId")
                },
                onAlbumClick = { albumId ->
                    navController.navigate("details/$albumId")
                }
            )
        }



        composable("library") {
            LibraryScreen(
                onNavigateToList = { listId ->
                    println("DEBUG: Navegando para lista $listId")
                    navController.navigate("list_detail/$listId") },
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

            if (type == "friends") {
                // Passa a lista combinada de FriendActivity + Album
                FriendsFeedScreen(
                    activities = state.friendListening,
                    onBackClick = { navController.popBackStack() },
                    onAlbumClick = { albumId -> navController.navigate("details/$albumId") }
                )
            } else {
                TrendingRankingScreen(
                    albums = state.trendingAlbums,
                    onBackClick = { navController.popBackStack() },
                    onAlbumClick = { albumId -> navController.navigate("details/$albumId") }
                )
            }
        }
    }
}