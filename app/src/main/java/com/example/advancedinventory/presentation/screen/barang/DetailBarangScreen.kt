package com.example.advancedinventory.presentation.screen.barang

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedinventory.presentation.component.DetailItem
import com.example.advancedinventory.presentation.component.ItemCard
import com.example.advancedinventory.presentation.component.TransactionKeluar
import com.example.advancedinventory.presentation.component.TransactionMasuk
import com.example.advancedinventory.ui.theme.OrangePrimary
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DetailBarangScreen(
    itemId: String,
    viewModel: BarangViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {

    // Memanggil fetchItemDetails dan fetchTransactions untuk mendapatkan data barang dan transaksi
    LaunchedEffect(itemId) {
        viewModel.fetchItemDetails(itemId)
        viewModel.fetchTransactions(itemId)
    }


    // Mengambil data item dan transaksi dari state
    val item by viewModel.selectedItem.collectAsState()
    val supplierName by viewModel.supplierName.collectAsState()
    val transactions by viewModel.transactions.collectAsState() // Ambil transaksi
    val isLoading by viewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf("Masuk") }


    // Menampilkan indikator loading jika data masih kosong
    if (isLoading) {
        CircularProgressIndicator()
    } else if (item == null) {
        Text("Barang tidak ditemukan")
    } else {
        // Menampilkan DetailItem dengan data yang telah diambil
        Column(
            modifier = Modifier.padding(PaddingValues(top = 60.dp)) .background(color = OrangePrimary)
        ) {
            DetailItem(
                name = item!!.nama,
                stok = item!!.stok,
                harga = item!!.harga,
                deskripsi = item!!.deskripsi,
                desc = item!!.kategori,
                image = item!!.imageUrl ?: "",
                supplier = supplierName ?: ""
            )

            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .background(color = Color.White)
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth() .fillMaxHeight()

            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Spacer(modifier = Modifier.width(64.dp))
                    Button(
                        onClick = {
                            // Hapus barang dan kembali ke halaman sebelumnya
                            viewModel.deleteItem(itemId)
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Hapus")
                    }
                }
                Text(
                    text = "Riwayat Transaksi",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(62.dp))
                    Button(
                        onClick = {
                            transactionType = "Masuk"
                            showDialog = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Item")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Log.d("DetailBarangScreen", "Transactions: ${transactions.size}")
                if (transactions.isEmpty()) {
                    Text("No transactions found")
                }else{
                    TransContent(viewModel= viewModel)
                }

            }
        }
    }
    if (showDialog) {
        TransactionInputDialog(
            transactionType = transactionType,
            onDismiss = { showDialog = false },
            onConfirm = { category, jumlah, tanggal ->
                showDialog = false
                viewModel.addTransaction(itemId,kategori = category, jumlah = jumlah, tanggal = tanggal)
                viewModel.fetchTransactions(itemId)
            }
        )
    }
}

@Composable
fun TransactionInputDialog(
    transactionType: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Long) -> Unit // Long untuk tanggal
) {
    var jumlahInput by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedDateInMillis by remember { mutableStateOf(0L) } // Variabel untuk tanggal dalam bentuk Long
    var transactionCategory by remember { mutableStateOf(transactionType) }

    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Transaksi $transactionCategory") },
        text = {
            Column {
                // Pilih kategori transaksi (Masuk atau Keluar)
                Text("Pilih kategori transaksi:")
                Row {
                    RadioButton(
                        selected = transactionCategory == "Masuk",
                        onClick = { transactionCategory = "Masuk" }
                    )
                    Text("Masuk")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = transactionCategory == "Keluar",
                        onClick = { transactionCategory = "Keluar" }
                    )
                    Text("Keluar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Masukkan jumlah barang
                Text("Masukkan jumlah barang:")
                OutlinedTextField(
                    value = jumlahInput,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            jumlahInput = it
                        }
                    },
                    label = { Text("Jumlah") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Pilih tanggal transaksi
                Text("Pilih tanggal transaksi:")
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Tanggal") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    calendar.set(year, month, dayOfMonth)
                                    selectedDateInMillis = calendar.timeInMillis // Simpan tanggal dalam Long
                                    selectedDate = dateFormatter.format(calendar.time) // Format menjadi string
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.show()
                        }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Pick Date")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Pastikan jumlahInput tidak kosong dan selectedDateInMillis sudah dipilih
                    if (jumlahInput.isNotEmpty() && selectedDateInMillis != 0L) {
                        onConfirm(transactionCategory, jumlahInput.toInt(), selectedDateInMillis)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun TransContent(
    viewModel: BarangViewModel,
    modifier: Modifier = Modifier
) {
    // Ambil transaksi dari StateFlow
    val transactions = viewModel.transactions.collectAsState().value
    LaunchedEffect(transactions) {
        Log.d("DetailBarangScreen", "Fetched transactions: ${transactions.size}")

    }

    // Periksa apakah transaksi ada, jika tidak tampilkan pesan kosong atau "No transactions"
    if (transactions.isEmpty()) {
        Text("No transactions available")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.background(color = Color.White)
        ) {
            items(transactions) { transaction ->
                Log.d("DetailBarangScreen", "Transaction: $transaction")
                Log.d("TransactionCategory", "Category: ${transaction.kategori}")
                val formattedDate = transaction.tanggal?.toDate()?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                } ?: "Unknown Date"
                // Pastikan tipe yang benar digunakan (kategori: in/out)
                if (transaction.kategori == "Masuk") {
                    TransactionMasuk(
                        kategori = transaction.kategori,
                        jumlah = transaction.jumlah,
                        tanggal = transaction.tanggal?.toDate()?.time ?: 0L
                    )
                } else if (transaction.kategori == "Keluar") {
                    TransactionKeluar(
                        kategori = transaction.kategori,
                        jumlah = transaction.jumlah,
                        tanggal = transaction.tanggal?.toDate()?.time ?: 0L
                    )
                }

            }
        }
    }
}