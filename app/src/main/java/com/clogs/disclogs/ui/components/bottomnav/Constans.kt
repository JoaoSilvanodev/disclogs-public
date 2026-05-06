package com.clogs.disclogs.ui.components.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

object Constans {

    val BottomNavItems = listOf(
        BottomNavItem(
            label = "HOME",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "SEARCH",
            icon = Icons.Filled.Search,
            route = "search"
        ),
        BottomNavItem(
            label = "LIBRARY",
            icon = Icons.Filled.LibraryMusic,
            route = "library"
        ),
        BottomNavItem(
            label = "PROFILE",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}
