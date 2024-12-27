package com.example.advancedinventory.presentation.screen.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedinventory.data.firebase.repository.AuthRepository
import com.example.advancedinventory.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val context: Application
): AndroidViewModel(context){
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: MutableStateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _userName= MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        _userName.value = currentUser?.displayName
    }


    fun login(email: String, password: String) = viewModelScope.launch {
        val result = repository.loginFirebase(email, password)
        _loginFlow.value = result
        if (result is Resource.Success) {
            _userName.value = result.data?.displayName
        }
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        val result = repository.signupFirebase(email, password)
        _signupFlow.value = result
        Log.d("AuthViewModel", "$email, signUp: ${result.message}")
        _userName.value = result.data?.displayName
    }

    fun clearLoginFlow() {
        _loginFlow.value = null
    }
    fun logout() {
        repository.logoutFirebase()

        _userName.value = null
    }
}