package com.example.advancedinventory.presentation.component


import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TransactionMasuk(
    kategori: String,
    jumlah: Int,
    tanggal: Long,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = "Tanggal: ${tanggal.toFormattedDate()}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = kategori,
            color = Color.Green,
            style = MaterialTheme.typography.bodyMedium
        )

    }
    Text(
        text = "Jumlah: +$jumlah",
        color = Color.Green,
        style = MaterialTheme.typography.bodyMedium
    )
    Log.d("TransactionMasuk", "Kategori: $kategori, Jumlah: $jumlah, Tanggal: $tanggal")
}

@Composable
fun TransactionKeluar(
    kategori: String,
    jumlah: Int,
    tanggal: Long,
    modifier: Modifier = Modifier
) {
    Log.d("TransactionKeluar", "Kategori: $kategori, Jumlah: $jumlah, Tanggal: $tanggal")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = "Tanggal: ${tanggal.toFormattedDate()}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = kategori,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
        )

    }
    Text(
        text = "Jumlah: -$jumlah",
        color = Color.Red,
        style = MaterialTheme.typography.bodyMedium
    )
}

// Fungsi ekstensi untuk memformat tanggal dari Long ke string
fun Long.toFormattedDate(): String {
    val sdf = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(this))
}
