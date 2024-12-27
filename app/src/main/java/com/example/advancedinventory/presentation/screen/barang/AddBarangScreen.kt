@file:Suppress("UNUSED_EXPRESSION")

package com.example.advancedinventory.presentation.screen.barang

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp



import com.example.advancedinventory.data.model.Inventory
import com.example.advancedinventory.data.model.Supplier
import com.example.advancedinventory.presentation.component.LargeBtn
import com.example.advancedinventory.presentation.component.NormalTextField
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.ui.theme.BlackPrimary
import com.example.advancedinventory.ui.theme.OrangePrimary
import com.google.firebase.firestore.DocumentReference
import java.io.ByteArrayOutputStream
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddBarangScreen(
    viewModel: BarangViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,

) {
    val context = LocalContext.current

    // State untuk field input
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var selectedSupplierId by remember { mutableStateOf<DocumentReference?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // State untuk suppliers
    val suppliers by viewModel.suppliers.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSuppliers()
        Log.d("AddBarangScreen", "Suppliers loaded: $suppliers")
    }
    LaunchedEffect(viewModel.uploadStatus.collectAsState().value) {
        if (viewModel.uploadStatus.value != null) {
            viewModel.fetchItems() // Refresh items setelah data berhasil diunggah
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                imageUri = uri // Simpan URI langsung
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                val uri = bitmapToUri(context, bitmap) // Konversi bitmap ke URI
                imageUri = uri
            }
        }
    )
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangePrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                modifier = Modifier.background(OrangePrimary) . padding(top = 48.dp)
            ) {
                UploadImageBox(
                    onGallerySelected = { launcher.launch("image/*") },
                    onCameraSelected = { cameraLauncher.launch() },
                    imagePath = imageUri?.toString(),
                )
            }
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .background(color = Color.White)
                    .padding(start = 16.dp, end = 16.dp) .fillMaxHeight()


            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(weight = 1f, fill = false)
                        .background(color = Color.White)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    NormalTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = "Nama Barang",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NormalTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        label = "Deskripsi",
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NormalTextField(
                        value = stok,
                        onValueChange = { stok = it },
                        label = "Stok",

                        )
                    Spacer(modifier = Modifier.height(12.dp))
                    NormalTextField(
                        value = harga,
                        onValueChange = { harga = it },
                        label = "Harga",

                        )
                    Spacer(modifier = Modifier.height(12.dp))
                    FormKategori(
                        kategori = kategori,
                        onKategoriSelected = { kategori = it }

                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DropdownSupplier(
                        suppliers = suppliers,
                        selectedSupplierId = selectedSupplierId,
                        onSupplierSelected = {
                            Log.d("AddBarangScreen", "Supplier selected: $it")
                            selectedSupplierId = it
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                }
                Spacer(modifier = Modifier.height(8.dp))
                LargeBtn(
                    text = "Simpan",
                    modifier = Modifier,
                    onClick = {
                        val errorMessages = mutableListOf<String>()

                        // Memeriksa apakah ada data yang kosong
                        if (nama.isEmpty()) errorMessages.add("Nama Barang")
                        if (deskripsi.isEmpty()) errorMessages.add("Deskripsi")
                        if (stok.isEmpty()) errorMessages.add("Stok")
                        if (harga.isEmpty()) errorMessages.add("Harga")
                        if (kategori.isEmpty()) errorMessages.add("Kategori")
                        if (selectedSupplierId == null) errorMessages.add("Supplier")

                        // Menampilkan Toast jika ada data yang kosong
                        if (errorMessages.isNotEmpty()) {
                            val errorMessage = "Harap isi data: ${errorMessages.joinToString(", ")}"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            // Jika semua data sudah diisi
                            val item = Inventory(
                                nama = nama,
                                deskripsi = deskripsi,
                                stok = stok.toInt(),
                                harga = harga.toInt(),
                                kategori = kategori,
                                supplierId = selectedSupplierId // SupplierID disimpan sebagai DocumentReference
                            )
                            viewModel.addItem(
                                item,
                                imageUri,
                                selectedSupplierId!!.id
                            ) // Menggunakan ID dari DocumentReference
                            navController.navigate(Screen.BarangScreen.route)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))

            }






        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSupplier(
    suppliers: List<Supplier>, // Daftar supplier yang diterima dari ViewModel
    selectedSupplierId: DocumentReference?, // Supplier yang terpilih
    onSupplierSelected: (DocumentReference?) -> Unit // Callback untuk mengirim supplier yang dipilih
) {

    var expanded by remember { mutableStateOf(false) } // Untuk mengontrol status dropdown

    // Menentukan nama supplier yang dipilih untuk ditampilkan di TextField
    val selectedSupplierName = suppliers.firstOrNull { it.supplierId?.id == selectedSupplierId?.id }?.nama ?: "Pilih Supplier"

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Input field untuk memilih supplier
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedSupplierName,
                onValueChange = { }, // Tidak mengubah nilai langsung, hanya untuk tampilan
                label = { Text("Pilih Supplier") },
                readOnly = true, // Field ini hanya untuk tampilan, tidak bisa diketik manual
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Menambahkan anchor untuk dropdown menu
                    .clickable { expanded = !expanded }
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent, RoundedCornerShape(10.dp)),
                textStyle = TextStyle(
                    color = BlackPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangePrimary,
                    unfocusedBorderColor = BlackPrimary,
                    cursorColor = OrangePrimary,
                    focusedLabelColor = BlackPrimary,

                    )
            // Klik untuk toggle dropdown
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                suppliers.forEach { supplier ->
                    DropdownMenuItem(
                        text = { Text(supplier.nama) },
                        onClick = {
                            Log.d("DropdownSupplier", "Selected Supplier: ${supplier.nama}")
                            onSupplierSelected(supplier.supplierId) // Pilih supplier
                            expanded = false // Tutup dropdown setelah memilih
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormKategori(
    kategori: String,
    onKategoriSelected: (String) -> Unit // Callback untuk mengirim kategori yang dipilih
) {

    var expanded by remember { mutableStateOf(false) } // Untuk mengontrol status dropdown
    val kategoriItem = listOf("Makanan", "Minuman", "Alat Tulis") // List kategori

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Input field untuk kategori
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = kategori,
                onValueChange = { },
                label = { Text("Pilih Kategori") },
                readOnly = true, // Field ini hanya untuk tampilan, tidak bisa diketik manual
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Menambahkan anchor untuk dropdown menu
                    .clickable { expanded = !expanded }
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent, RoundedCornerShape(10.dp)),
                textStyle = TextStyle(
                    color = BlackPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangePrimary,
                    unfocusedBorderColor = BlackPrimary,
                    cursorColor = OrangePrimary,
                    focusedLabelColor = BlackPrimary,

                    )// Klik untuk toggle dropdown
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kategoriItem.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onKategoriSelected(label) // Pilih kategori
                            expanded = false // Tutup dropdown setelah memilih
                        }
                    )
                }
            }
        }
    }
}

// Fungsi untuk mengubah Bitmap ke URI
private fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path)
}
@Composable
fun UploadImageBox(
    onGallerySelected: () -> Unit,
    onCameraSelected: () -> Unit,
    imagePath: String? // Tetap gunakan sebagai nullable string
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        if (imagePath != null) {
            // Menampilkan gambar dari URI
            Image(
                painter = rememberAsyncImagePainter(imagePath),
                contentDescription = "Gambar yang dipilih",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Tampilan default jika belum ada gambar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Unggah Gambar",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "Unggah Foto",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }

    // Dialog untuk memilih sumber gambar
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Pilih Sumber Gambar") },
            text = { Text("Silakan pilih untuk mengambil gambar dari galeri atau kamera.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onGallerySelected()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangePrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Galeri")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onCameraSelected()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangePrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Kamera")
                }
            }
        )
    }
}