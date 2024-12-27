package com.example.advancedinventory.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.ui.theme.OrangePrimary
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel

) {
    val totalItems by viewModel.totalItems.collectAsState()
    val totalSuppliers by viewModel.totalSuppliers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTotalItems()
        viewModel.fetchTotalSuppliers()
    }

    // Desain layar
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 32.dp)
            // Latar belakang putih

    ) {
        Box(
            modifier = Modifier
                .size(200.dp) // Ukuran box
                .clip(RoundedCornerShape(16.dp)) // Bentuk rounded
                .background(OrangePrimary)
                .clickable {
                    navController.navigate(Screen.BarangScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }, // Warna biru untuk box
            contentAlignment = Alignment.Center // Konten di tengah box
        ) {
            Text(
                text = "Total Barang: $totalItems",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(200.dp) // Ukuran box
                .clip(RoundedCornerShape(16.dp)) // Bentuk rounded
                .background(OrangePrimary)
                .clickable {
                    navController.navigate(Screen.SupplierScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }, // Warna biru untuk box
            contentAlignment = Alignment.Center // Konten di tengah box
        ) {
            Text(
                text = "Total Supplier: $totalSuppliers",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }

    
}