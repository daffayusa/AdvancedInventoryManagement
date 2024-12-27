package com.example.advancedinventory.data.firebase.repository

import android.net.Uri
import android.util.Log
import com.example.advancedinventory.data.model.Inventory
import com.example.advancedinventory.data.model.Supplier
import com.example.advancedinventory.data.model.Transaction
import com.example.advancedinventory.data.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.tasks.await
import java.util.UUID

//class FirestoreRepository(private val db: FirebaseFirestore) {
//    //User
//    suspend fun createUser(userId: String, user: User) {
//        db.collection("users").document(userId).set(user).await()
//    }
//
//    suspend fun getUser(userId: String): User? {
//        return db.collection("users").document(userId).get().await().toObject(User::class.java)
//    }
//
//    // Supplier Operations
//    suspend fun createSupplier(supplier: Supplier): String {
//        val docRef = db.collection("suppliers").add(supplier).await()
//        return docRef.id
//    }
//
//    suspend fun getSuppliers(): List<Supplier> {
//        val snapshot: QuerySnapshot = db.collection("suppliers").get().await()
//        return snapshot.toObjects(Supplier::class.java)
//    }
//
//    suspend fun getSupplier(supplierId: String): Supplier? {
//        return db.collection("suppliers").document(supplierId).get().await().toObject(Supplier::class.java)
//    }
//    // Inventory Operations
//    suspend fun createItem(item: Inventory): String {
//        // Ambil referensi ke Supplier berdasarkan supplierId (yang sudah menjadi DocumentReference)
//        val supplierRef = item.supplierId  // supplierId sekarang sudah bertipe DocumentReference
//
//        val itemWithSupplierRef = item.copy(supplierId = supplierRef)
//
//        // Simpan item ke Firestore
//        val docRef = db.collection("inventory").add(itemWithSupplierRef).await()
//        return docRef.id
//    }
//
//    suspend fun getItems(): List<Inventory> {
//        val snapshot: QuerySnapshot = db.collection("inventory").get().await()
//        Log.d("FirestoreRepository", "Fetched data: ${snapshot.documents}")
//
//        return snapshot.documents.mapNotNull { document ->
//            val inventory = document.toObject(Inventory::class.java)
//
//            // Mengambil supplierId sebagai DocumentReference
//            val supplierRef = document.getDocumentReference("supplierId")
//
//            // Pastikan inventory tidak null sebelum melakukan copy
//            inventory?.copy(supplierId = supplierRef)
//        }
//    }
//
//    suspend fun getItem(itemId: String): Inventory? {
//        val document = db.collection("inventory").document(itemId).get().await()
//
//        val inventory = document.toObject(Inventory::class.java)
//        val supplierRef = document.getDocumentReference("supplierId")
//
//        // Pastikan inventory tidak null sebelum melakukan copy
//        return inventory?.copy(supplierId = supplierRef)
//    }
//
//    // Transaction Operations
//    suspend fun createTransaction(transaction: Transaction): String {
//        val docRef = db.collection("trans").add(transaction).await()
//        return docRef.id
//    }
//
//    suspend fun getTransactions(): List<Transaction> {
//        val snapshot: QuerySnapshot = db.collection("trans").get().await()
//        return snapshot.toObjects(Transaction::class.java)
//    }
//
//    suspend fun getTransaction(transactionId: String): Transaction? {
//        return db.collection("trans").document(transactionId).get().await().toObject(Transaction::class.java)
//    }
//
//}

class FirestoreRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) {


    // Perbarui metode di Repository untuk memastikan supplierId disertakan sebagai DocumentReference
    suspend fun createItem(inventory: Inventory, imageUri: Uri?): String {
        val imageUrl = imageUri?.let { uploadImageToStorage(it) }
        val inventoryWithImage = inventory.copy(imageUrl = imageUrl ?: "")
        val docRef = db.collection("inventory").add(inventoryWithImage).await()
        return docRef.id
    }

    suspend fun getItems(): List<Inventory> {
        val snapshot = db.collection("inventory").get().await()
        return snapshot.documents.mapNotNull { doc ->
            val inventory = doc.toObject(Inventory::class.java)
            inventory?.copy(itemId = doc.reference.id) // Menambahkan itemId berdasarkan ID dokumen
        }
    }
    fun getTotalItems(): Flow<Int> = flow {
        val collection = db.collection("inventory")
        val totalItems = collection.get().await().size()
        emit(totalItems)
    }.catch { e ->
        Log.e("BarangRepository", "Error fetching total items: ${e.message}")
        emit(0) // Default value in case of an error
    }


    suspend fun getItem(itemId: String): Inventory? {
        return db.collection("inventory").document(itemId).get().await().toObject(Inventory::class.java)
    }

    suspend fun getSupplierDocumentReference(supplierId: String): DocumentReference {
        return db.collection("suppliers").document(supplierId)
    }
    suspend fun deleteItem(itemId: String) {
        db.collection("inventory").document(itemId).delete().await()
    }


    // Supplier Operations
    suspend fun createSupplier(supplier: Supplier): String {
        val docRef = db.collection("suppliers").add(supplier).await()
        return docRef.id // Mengembalikan ID supplier yang baru dibuat
    }

    suspend fun getSuppliers(): List<Supplier> {
        val snapshot = db.collection("suppliers").get().await()
        return snapshot.documents.map { doc ->
            doc.toObject(Supplier::class.java)?.copy(supplierId = doc.reference) // Set supplierId dengan DocumentReference
        }.filterNotNull()
    }
    // Mendapatkan detail supplier berdasarkan DocumentReference
    suspend fun getSupplierDetails(supplierRef: DocumentReference): Supplier? {
        return supplierRef.get().await().toObject(Supplier::class.java)
    }
    suspend fun updateSupplier(supplierId: String, nama: String, kontak: String, alamat: GeoPoint) {
        val updates = mapOf(
            "nama" to nama,
            "kontak" to kontak,
            "alamat" to alamat
        )
        db.collection("suppliers").document(supplierId).update(updates).await()
    }

    suspend fun getSupplier(supplierId: String): Supplier? {
        return db.collection("suppliers").document(supplierId).get().await().toObject(Supplier::class.java)
    }
    fun getTotalSuppliers(): Flow<Int> = flow {
        val collection = db.collection("suppliers")
        val totalItems = collection.get().await().size()
        emit(totalItems)
    }.catch { e ->
        Log.e("SupplierRepository", "Error fetching total items: ${e.message}")
        emit(0) // Default value in case of an error
    }

    // Transaction Operations
    suspend fun getTransactions(): List<Transaction> {
        val snapshot = db.collection("trans").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Transaction::class.java) }
    }

    suspend fun getTransaction(transId: String): Transaction? {
        return db.collection("trans").document(transId).get().await().toObject(Transaction::class.java)
    }

    suspend fun getItemDocumentReference(itemId: String): DocumentReference {
        return db.collection("inventory").document(itemId)
    }
    // Mengambil transaksi berdasarkan itemId
    suspend fun getTransactionsByItem(itemId: String): List<Transaction> {
        return try {
            val itemRef = db.collection("inventory").document(itemId) // Ambil referensi dokumen
            Log.d("FirestoreRepository", "Fetching transactions for item: $itemId")
            val querySnapshot = db.collection("trans")
                .whereEqualTo("itemId", itemRef) // Gunakan referensi dokumen di sini
                .get()
                .await()
            Log.d("FirestoreRepository", "Fetched transactions: ${querySnapshot.size()}")
            querySnapshot.documents.map { document ->
                document.toObject(Transaction::class.java)!!
            }

        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error fetching transactions: ${e.message}")
            emptyList()
        }
    }
    // Tambahkan transaksi baru ke Firestore
    suspend fun createTransaction(transaction: Transaction) {
        val docRef = db.collection("trans").add(transaction).await()
        Log.d("FirestoreRepository", "Transaction added with ID: ${docRef.id}")
    }

    // Perbarui stok barang
    suspend fun updateItemStock(itemId: String, newStock: Int) {
        db.collection("inventory")
            .document(itemId)
            .update("stok", newStock)
            .await()
    }

    // Upload Image to Firebase Storage
    private suspend fun uploadImageToStorage(imageUri: Uri): String {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storage.reference.child("inventory_images/$fileName")
        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }
    suspend fun deleteSupplier(supplierId: String) {
        db.collection("suppliers").document(supplierId).delete().await()
    }

}
