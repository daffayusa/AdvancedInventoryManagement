package com.example.advancedinventory.presentation.screen.barang

import com.example.advancedinventory.data.firebase.repository.FirestoreRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.advancedinventory.data.model.Inventory
import com.example.advancedinventory.data.model.Supplier
import com.example.advancedinventory.data.model.Transaction
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val storage: FirebaseStorage
): ViewModel() {
    private val _items = MutableStateFlow<List<Inventory>>(emptyList())
    val items: StateFlow<List<Inventory>> get() = _items

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> get() = _suppliers

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus: StateFlow<String?> get() = _uploadStatus

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

//    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
//    val transactions: StateFlow<List<Transaction>> get() = _transactions

    // Fetch all items with supplier references
    fun fetchItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedItems = repository.getItems()
                _items.value = fetchedItems
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("BarangViewModel", "Error fetching items: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add a new inventory item
    fun addItem(item: Inventory, imageUri: Uri?, supplierId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Mendapatkan referensi supplier
                val supplierRef = repository.getSupplierDocumentReference(supplierId)

                // Membuat inventory dengan supplierId yang terhubung
                val newItem = item.copy(supplierId = supplierRef)

                if (imageUri != null) {
                    val imageUrl = uploadImageToStorage(imageUri)
                    val updatedItem = newItem.copy(imageUrl = imageUrl)
                    val itemId = repository.createItem(updatedItem, imageUri)
                } else {
                    val itemId = repository.createItem(newItem, null)
                }

                // Refresh daftar items setelah menambahkan item baru
                fetchItems()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("BarangViewModel", "Error adding item: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Upload an image to Firebase Storage
    private suspend fun uploadImageToStorage(imageUri: Uri): String {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storage.reference.child("inventory_images/$fileName")
        ref.putFile(imageUri).await()
         return ref.downloadUrl.await().toString()
    }

    fun fetchSuppliers() {
        viewModelScope.launch {
            try {
                val fetchedSuppliers = repository.getSuppliers() // Fungsi ini mengambil data supplier dari Firestore
                _suppliers.value = fetchedSuppliers
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Error fetching suppliers: ${e.message}")
            }
        }
    }
    private val _selectedItem = MutableStateFlow<Inventory?>(null)
    val selectedItem: StateFlow<Inventory?> get() = _selectedItem

    private val _supplierName = MutableStateFlow<String?>(null)
    val supplierName: StateFlow<String?> get() = _supplierName

    // Fungsi untuk mengambil data detail item
    fun fetchItemDetails(itemId: String) {
        viewModelScope.launch {
            try {
                val fetchedItem = repository.getItem(itemId)
                _selectedItem.value = fetchedItem
                fetchedItem?.supplierId?.let { supplierRef ->
                    val supplier = repository.getSupplier(supplierRef.id)
                    _supplierName.value = supplier?.nama
                }
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Error fetching item details: ${e.message}")
            }
        }
    }

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> get() = _transactions

    fun fetchTransactions(itemId: String) {
        viewModelScope.launch {
            try {
                val fetchedTransactions = repository.getTransactionsByItem(itemId)
                Log.d("BarangViewModel", "Fetched transactions: ${fetchedTransactions.size}")
                if (fetchedTransactions.isNotEmpty()) {
                    _transactions.value = fetchedTransactions
                } else {
                    _errorMessage.value = "No transactions fetched"
                    Log.d("BarangViewModel", "No transactions fetched for itemId: $itemId")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("BarangViewModel", "Error fetching transactions: ${e.message}")
            }
        }
    }


    // Fungsi untuk menambahkan transaksi baru
    fun addTransaction(itemId: String, kategori: String, jumlah: Int, tanggal: Long) {
        viewModelScope.launch {
            try {
                val transaction = Transaction(
                    itemId = repository.getItemDocumentReference(itemId),
                    kategori = kategori,
                    jumlah = jumlah,
                    tanggal = Timestamp(tanggal / 1000, 0) // Mengonversi tanggal ke Timestamp
                )
                repository.createTransaction(transaction)

                // Update stok barang setelah transaksi
                val item = repository.getItem(itemId)
                val updatedStock = if (kategori == "Masuk") {
                    (item?.stok ?: 0) + jumlah // Menambahkan jumlah ke stok jika kategori "in"
                } else {
                    (item?.stok ?: 0) - jumlah // Mengurangi jumlah dari stok jika kategori "out"
                }

                if (updatedStock >= 0) {
                    repository.updateItemStock(itemId, updatedStock)
                    fetchItemDetails(itemId) // Refresh data barang
                } else {
                    Log.e("BarangViewModel", "Stok tidak mencukupi untuk transaksi keluar.")
                }
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Error adding transaction: ${e.message}")
            }
        }
    }
    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            try {
                repository.deleteItem(itemId)
                fetchItems()
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Error deleting item: ${e.message}")
            }
        }
    }




}

