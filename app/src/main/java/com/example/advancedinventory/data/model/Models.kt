package com.example.advancedinventory.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class User(
    val email: String = "",
    val createdAt: Timestamp? = null
)

data class Supplier(
    val supplierId: DocumentReference? = null,
    val nama: String = "",
    val kontak: String = "",
    val alamat: GeoPoint? = null,
)

data class Inventory(
    val itemId: String = "",
    val nama: String = "",
    val deskripsi: String = "",
    val stok: Int = 0,
    val harga: Int = 0,
    val kategori: String = "",
    val supplierId: DocumentReference? = null,  // Ubah menjadi DocumentReference
    val imageUrl: String? = null,
)

data class Transaction(
    val itemId: DocumentReference? = null,
    val jumlah: Int = 0,
    val kategori: String = "", // Either "in" or "out"
    val tanggal: Timestamp? = null,
)
