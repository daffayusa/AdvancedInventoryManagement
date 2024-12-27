package com.example.advancedinventory.presentation.screen.barang

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedinventory.presentation.component.ItemCard
import com.example.advancedinventory.presentation.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangScreen(
    modifier: Modifier = Modifier,
    viewModel: BarangViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.fetchItems()
    }

    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (items.isEmpty()) {
                Text("Tidak ada data barang", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    ItemCard(
                        name = item.nama,
                        stok = item.stok,
                        harga = item.harga,
                        kategori = item.kategori,
                        image = item.imageUrl ?: "",
                        onClick = {
                            navController.navigate("${Screen.DetailBarangScreen.route}/${item.itemId}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Log.d("BarangScreen", "Image URL for item '${item.nama}': ${item.imageUrl}")
                }

            }
        }
    }
}