package com.example.advancedinventory.presentation.screen.supplier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedinventory.presentation.component.LargeBtn
import com.example.advancedinventory.presentation.component.LargeRedBtn
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.ui.theme.OrangePrimary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

@Composable
fun DetailSupplierScreen(
    supplierId: String,
    viewModel: SupplierViewModel = hiltViewModel(),
    navController: NavController
) {
    val supplierRef = FirebaseFirestore.getInstance().collection("suppliers").document(supplierId)
    val supplier by viewModel.supplierDetail.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Fetch supplier detail when this composable is launched
    LaunchedEffect(supplierId) {
        viewModel.fetchSupplierDetail(supplierRef)
    }

    // Access context inside the Composable
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Color.White) .padding(top = 64.dp, start = 16.dp, end = 16.dp)) {
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            supplier?.let { selectedSupplier ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = selectedSupplier.nama,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Kontak: ${selectedSupplier.kontak}")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (selectedSupplier.alamat != null) {
                        Text(text = "Alamat: ${selectedSupplier.alamat.latitude}, ${selectedSupplier.alamat.longitude}")
                        Spacer(modifier = Modifier.height(8.dp))
                        LargeBtn(
                            text = "Lihat di Maps",
                            onClick = {
                                openMap(selectedSupplier.alamat, context)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier= Modifier.height(16.dp))
                        // Tombol Edit Supplier
                        LargeBtn(
                            onClick = {
                                navController.navigate("edit_supplier_screen/$supplierId")

                            },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Edit Supplier"
                        )


                        Spacer(modifier = Modifier.height(8.dp))

                        // Tombol Hapus Supplier
                        LargeRedBtn(
                            onClick = {
                                viewModel.deleteSupplier(supplierId) { isDeleted ->
                                    if (isDeleted) {
                                        navController.navigate(Screen.SupplierScreen.route) {
                                            popUpTo(Screen.SupplierScreen.route) { inclusive = true }
                                        }
                                    } else {
                                        // Tampilkan pesan kesalahan jika diperlukan
                                        Log.e("DetailSupplierScreen", "Failed to delete supplier")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Hapus Supplier"
                        )

                    }
                }
            }
        }
    }
}

fun openMap(geoPoint: GeoPoint, context: Context) {
    val uri = Uri.parse("geo:0,0?q=${geoPoint.latitude},${geoPoint.longitude}(Lokasi+Supplier)")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Google Maps tidak tersedia", Toast.LENGTH_SHORT).show()
    }
}