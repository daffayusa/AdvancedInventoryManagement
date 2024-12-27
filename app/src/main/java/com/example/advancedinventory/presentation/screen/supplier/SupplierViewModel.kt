package com.example.advancedinventory.presentation.screen.supplier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedinventory.data.firebase.repository.FirestoreRepository
import com.example.advancedinventory.data.model.Supplier
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(private val repository: FirestoreRepository) : ViewModel() {
    // Menggunakan StateFlow untuk menyimpan daftar supplier
    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers

    // Menggunakan StateFlow untuk status loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _supplierDetail = MutableStateFlow<Supplier?>(null)
    val supplierDetail: StateFlow<Supplier?> = _supplierDetail

    // Fungsi untuk memperbarui status loading
    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    // Fungsi untuk menambah supplier
    fun addSupplier(nama: String, kontak: String, alamat: GeoPoint?) {
        viewModelScope.launch {
            _loading.value = true
            val newSupplier = Supplier(
                nama = nama,
                kontak = kontak,
                alamat = alamat
            )
            val supplierId = repository.createSupplier(newSupplier)
            _loading.value = false
        }
    }
    // Fungsi untuk memuat data supplier
    fun fetchSuppliers() {
        viewModelScope.launch {
            _loading.value = true
            val suppliersList = repository.getSuppliers() // Ambil data supplier dari repository
            _suppliers.value = suppliersList
            _loading.value = false
        }
    }
    // Fetch supplier details based on DocumentReference
    fun fetchSupplierDetail(supplierRef: DocumentReference) {
        viewModelScope.launch {
            _loading.value = true
            val supplier = repository.getSupplierDetails(supplierRef) // Get supplier details using DocumentReference
            _supplierDetail.value = supplier
            _loading.value = false
        }
    }


    fun deleteSupplier(supplierId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteSupplier(supplierId)
                onComplete(true)
            } catch (e: Exception) {
                Log.e("SupplierViewModel", "Error deleting supplier: ${e.message}")
                onComplete(false)
            }
        }
    }
    fun updateSupplier(supplierId: String, nama: String, kontak: String, alamat: GeoPoint?) {
        viewModelScope.launch {
            setLoading(true)
            val supplierRef = repository.getSupplierDocumentReference(supplierId)
            supplierRef.update(
                mapOf(
                    "nama" to nama,
                    "kontak" to kontak,
                    "alamat" to alamat
                )
            ).addOnCompleteListener {
                setLoading(false)
            }.addOnFailureListener {
                setLoading(false)
            }
        }
    }



}
