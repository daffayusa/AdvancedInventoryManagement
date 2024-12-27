package com.example.advancedinventory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.advancedinventory.presentation.component.BottomNavItem
import com.example.advancedinventory.presentation.component.TopBarComponent
import com.example.advancedinventory.presentation.component.TopBarComponentProfile
import com.example.advancedinventory.presentation.navigation.BottomNavItem
import com.example.advancedinventory.presentation.navigation.NavGraph
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.presentation.navigation.bottomNavItems
import com.example.advancedinventory.presentation.screen.auth.AuthViewModel
import com.example.advancedinventory.ui.theme.OrangePrimary

@Composable
fun MainApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: Screen.LoginScreen.route
    Log.d("MainApp", "Current destination: $currentDestination")

    val userName by authViewModel.userName.collectAsState()
    Scaffold(
        topBar = {
            Log.d("Scaffold", "TopBar executed with route: $currentDestination")
            when (currentDestination) {
                Screen.BarangScreen.route -> TopBarComponentProfile(
                    userName = userName, // Untuk debug sementara
                    navController = navController
                )
                Screen.AddbarangScreen.route -> TopBarComponent(
                    title = "Tambah Barang",
                    navController = navController
                )
                Screen.AddSupplierScreen.route -> TopBarComponent(
                    title = "Tambah Supplier",
                    navController = navController
                )
                Screen.SupplierScreen.route -> TopBarComponentProfile(
                    userName = userName, // Untuk debug sementara
                    navController = navController
                )
                Screen.DetailBarangScreen.route -> TopBarComponent(
                    title = "Detail Barang",
                    navController = navController
                )
                Screen.HomeScreen.route -> TopBarComponentProfile(
                    userName = userName, // Untuk debug sementara
                    navController = navController
                )
                Screen.ProfileScreen.route -> TopBarComponent(
                    title = "Profile",
                    navController = navController
                )

            }
            when{
                currentDestination.startsWith(Screen.DetailBarangScreen.route) -> TopBarComponent(
                    title = "Detail Barang",
                    navController = navController
                )
                currentDestination.startsWith(Screen.DetailSupplierScreen.route) -> TopBarComponent(
                    title = "Detail Supplier",
                    navController = navController
                )
            }
        },
        bottomBar = {
//            Log.d("Scaffold", "BottomBar executed with route: $currentDestination")
//            when (currentDestination) {
//                Screen.BarangScreen.route -> BottomNavItem(
//                    items = bottomNavItems,
//                    navController = navController
//                )
//                Screen.SupplierScreen.route -> BottomNavItem(
//                    items = bottomNavItems,
//                    navController = navController
//                )
//                Screen.HomeScreen.route -> BottomNavItem(
//                    items = bottomNavItems,
//                    navController = navController
//                )
//
//            }
            val showBottomBar = currentDestination in listOf(
                Screen.HomeScreen.route,
                Screen.BarangScreen.route,
                Screen.SupplierScreen.route,
                Screen.ProfileScreen.route
            )
            if (showBottomBar) {
                BottomNavItem(
                    items = bottomNavItems,
                    navController = navController
                )
            }
        },
        floatingActionButton = {
            Log.d("Scaffold", "FAB executed with route: $currentDestination")
            when (currentDestination) {
                Screen.BarangScreen.route -> FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddbarangScreen.route) },
                    containerColor = OrangePrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
                Screen.SupplierScreen.route -> FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddSupplierScreen.route) },
                    containerColor = OrangePrimary
                ){
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        NavGraph(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding() / 2, // Mengurangi padding top
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                bottom = paddingValues.calculateBottomPadding() /2
            )
                .background(color = Color.White),
            authViewModel = authViewModel,
            navController = navController
        )
    }
}
