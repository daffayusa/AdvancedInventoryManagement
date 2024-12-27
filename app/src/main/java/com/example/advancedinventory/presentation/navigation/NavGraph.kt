package com.example.advancedinventory.presentation.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.advancedinventory.presentation.screen.auth.AuthViewModel
import com.example.advancedinventory.presentation.screen.auth.login.LoginScreen
import com.example.advancedinventory.presentation.screen.auth.profile.ProfileScreen
import com.example.advancedinventory.presentation.screen.auth.register.RegisterScreen
import com.example.advancedinventory.presentation.screen.barang.AddBarangScreen
import com.example.advancedinventory.presentation.screen.barang.BarangScreen
import com.example.advancedinventory.presentation.screen.barang.BarangViewModel
import com.example.advancedinventory.presentation.screen.barang.DetailBarangScreen

import com.example.advancedinventory.presentation.screen.home.HomeScreen
import com.example.advancedinventory.presentation.screen.home.HomeViewModel
import com.example.advancedinventory.presentation.screen.supplier.AddSupplierScreen
import com.example.advancedinventory.presentation.screen.supplier.DetailSupplierScreen
import com.example.advancedinventory.presentation.screen.supplier.EditSupplierScreen
import com.example.advancedinventory.presentation.screen.supplier.SupplierScreen
import com.example.advancedinventory.presentation.screen.supplier.SupplierViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavHostController // Gunakan navController dari MainApp
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController, // Jangan buat navController baru
            startDestination = Screen.LoginScreen.route,
            modifier = modifier
        ) {
            composable(Screen.LoginScreen.route) {
                val authViewModel = hiltViewModel<AuthViewModel>()
                Log.d("NavGraph", "Navigated to LoginScreen")
                LoginScreen(authViewModel = authViewModel, navController = navController)
            }
            composable(Screen.RegisterScreen.route) {
                val authViewModel = hiltViewModel<AuthViewModel>()
                RegisterScreen(authViewModel = authViewModel, navController = navController)
            }
            composable(Screen.BarangScreen.route) {
                val BarangViewModel = hiltViewModel<BarangViewModel>()
                Log.d("NavGraph", "Navigated to BarangScreen")
                BarangScreen(viewModel = BarangViewModel, navController = navController, modifier = modifier)
            }
            composable(Screen.AddbarangScreen.route) {
                val BarangViewModel = hiltViewModel<BarangViewModel>()
                AddBarangScreen(
                    viewModel = BarangViewModel,
                    onBack = { navController.popBackStack() },
                    navController = navController,
                    modifier = modifier
                )
            }
            composable(Screen.AddSupplierScreen.route) {
                val supplierViewModel = hiltViewModel<SupplierViewModel>()
                AddSupplierScreen(
                    supplierViewModel = supplierViewModel,
                    onBack = { navController.popBackStack() },
                    navController = navController,
                )
            }
            composable(
                route = "${Screen.DetailBarangScreen.route}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                val barangViewModel = hiltViewModel<BarangViewModel>()
                DetailBarangScreen(
                    itemId = itemId,
                    viewModel = barangViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
            composable(Screen.SupplierScreen.route){
                val supplierViewModel = hiltViewModel<SupplierViewModel>()
                Log.d("NavGraph", "Navigated to SupplierScreen")
                SupplierScreen(
                    viewModel = supplierViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
            composable(
                route = "${Screen.DetailSupplierScreen.route}/{supplierId}",
                arguments = listOf(navArgument("supplierId") { type = NavType.StringType })
            ) { backStackEntry ->
                val supplierId = backStackEntry.arguments?.getString("supplierId") ?: ""
                DetailSupplierScreen(supplierId = supplierId, navController = navController)
            }
            composable(Screen.HomeScreen.route) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(navController = navController, viewModel = homeViewModel, modifier = modifier)

            }
            composable(
                route = "${Screen.EditSupplierScreen.route}/{supplierId}",
                arguments = listOf(navArgument("supplierId") { type = NavType.StringType })
            ) { backStackEntry ->

                val supplierId = backStackEntry.arguments?.getString("supplierId") ?: ""
                val supplierViewModel = hiltViewModel<SupplierViewModel>()
                EditSupplierScreen(
                    supplierId = supplierId,
                    viewModel = supplierViewModel,
                    navController = navController
                )
            }
            composable(Screen.ProfileScreen.route) {
                val authViewModel = hiltViewModel<AuthViewModel>()
                ProfileScreen(viewModel = authViewModel, navController = navController)
            }

        }
    }
}