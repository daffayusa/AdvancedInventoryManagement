package com.example.advancedinventory.presentation.screen.supplier

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedinventory.presentation.component.LargeBtn
import com.example.advancedinventory.presentation.component.NormalTextField
import com.example.advancedinventory.presentation.screen.maps.MapsActivity
import com.example.advancedinventory.ui.theme.OrangePrimary
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

@Composable
fun AddSupplierScreen(
    supplierViewModel: SupplierViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val namaState = remember { mutableStateOf("") }
    val kontakState = remember { mutableStateOf("") }
    val alamatState = remember { mutableStateOf<GeoPoint?>(null) }
    val isLoading by supplierViewModel.loading.collectAsState(false)
    val latLngState = remember { mutableStateOf<LatLng?>(null) }



    // Fungsi untuk menangani hasil dari MapsActivity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val latitude = data.getDoubleExtra("latitude", 0.0)
                val longitude = data.getDoubleExtra("longitude", 0.0)
                val geoPoint = GeoPoint(latitude, longitude)
                alamatState.value = geoPoint
            }
        }
    }

    Column(modifier = Modifier.padding(top = 64.dp, start = 16.dp, end = 16.dp) .background(Color.White) .fillMaxHeight()) {
        NormalTextField(
            value = namaState.value,
            label = "Nama Supplier",
            onValueChange = { namaState.value = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        NormalTextField(
            value = kontakState.value,
            label = "Kontak",
            onValueChange = { kontakState.value = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        LargeBtn(
            text = "Pilih Alamat",
            onClick = {
                val intent = Intent(context, MapsActivity::class.java)
                launcher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Alamat: ${alamatState.value?.latitude ?: "Belum dipilih"}, ${alamatState.value?.longitude ?: "Belum dipilih"}")
        Spacer(modifier = Modifier.height(16.dp))
        LargeBtn(
            text = "Tambah Supplier",
            onClick = {
                // Menambahkan supplier
                supplierViewModel.addSupplier(namaState.value, kontakState.value, alamatState.value)
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = namaState.value.isNotEmpty() && kontakState.value.isNotEmpty() && alamatState.value != null
        )


        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
