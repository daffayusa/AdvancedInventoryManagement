package com.example.advancedinventory.presentation.screen.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.advancedinventory.presentation.component.SupplierCard
import com.example.advancedinventory.presentation.navigation.Screen

@Composable
fun SupplierScreen(
    viewModel: SupplierViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Meminta data supplier dengan StateFlow
    LaunchedEffect(Unit) {
        viewModel.fetchSuppliers()
    }

    val suppliers by viewModel.suppliers.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White) .padding(top = 48.dp, bottom = 48.dp)) {
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (suppliers.isEmpty()) {
                Text("Tidak ada data supplier", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suppliers) { supplier ->
                    SupplierCard(
                        nama = supplier.nama,
                        kontak = supplier.kontak,
                        onItemClick = {
                            // Aksi saat card supplier di-klik
                            navController.navigate("${Screen.DetailSupplierScreen.route}/${supplier.supplierId?.id}")
                        }
                    )
                }
            }
        }
    }
}
