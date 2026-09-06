package com.clogs.disclogs.ui.components.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.clogs.disclogs.ui.components.bottomnav.BottomBar

@Composable
fun DisclogsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomRoutes = listOf(
        "auth",
        "details/{albumId}",
        "artist/{artistId}",
        "settings",
        "user_profile/{userId}"
    )
    
    val showBottomBar = currentRoute?.let { route ->
        !hideBottomRoutes.contains(route)
    } ?: false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            paddingValues = paddingValues,
            authViewModel = hiltViewModel()
        )
    }
}