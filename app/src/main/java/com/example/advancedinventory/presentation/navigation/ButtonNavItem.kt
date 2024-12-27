package com.example.advancedinventory.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
var bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = "home_screen"
    ),
    BottomNavItem(
        title = "Supplier",
        icon = Icons.Default.List,
        route = "supplier_screen"
    ),
    BottomNavItem(
        title = "Barang",
        icon = Icons.Default.ShoppingCart,
        route = "barang_screen"

    ),
    BottomNavItem(
        title = "Profile",
        icon = Icons.Default.Person,
        route = "profile_screen"
    )
)
