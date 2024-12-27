package com.example.advancedinventory.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("registrasi_screen")
    object HomeScreen : Screen("home_screen")
    object BarangScreen : Screen("barang_screen")
    object AddbarangScreen: Screen("addbarang_screen")
    object SupplierScreen: Screen("supplier_screen")
    object AddSupplierScreen: Screen("addsupplier_screen")
    object DetailSupplierScreen: Screen("detailsupplier_screen")
    object DetailBarangScreen: Screen("detailbarang_screen")
    object EditSupplierScreen : Screen("edit_supplier_screen")
    object ProfileScreen : Screen("profile_screen")
    object EditBarangScreen : Screen("edit_barang_screen")

}