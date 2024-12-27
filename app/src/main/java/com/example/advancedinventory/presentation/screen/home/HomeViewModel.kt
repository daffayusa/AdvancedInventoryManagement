package com.example.advancedinventory.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedinventory.data.firebase.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirestoreRepository

): ViewModel() {
    private val _totalSuppliers = MutableStateFlow(0)
    val totalSuppliers: StateFlow<Int> = _totalSuppliers.asStateFlow()

    fun fetchTotalSuppliers() {
        viewModelScope.launch {
            repository.getTotalSuppliers()
                .collect { total ->
                    _totalSuppliers.value = total
                }
        }
    }

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()

    fun fetchTotalItems() {
        viewModelScope.launch {
            repository.getTotalItems()
                .collect { total ->
                    _totalItems.value = total
                }
        }
    }
}