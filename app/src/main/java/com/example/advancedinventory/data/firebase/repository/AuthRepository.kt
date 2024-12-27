package com.example.advancedinventory.data.firebase.repository

import android.util.Log
import com.example.advancedinventory.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser = firebaseAuth.currentUser
    suspend fun loginFirebase(email: String, password: String): Resource<FirebaseUser>{
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            com.example.advancedinventory.util.Resource.Success(authResult.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("AuthRepositoryLogin", "$email,login: ${e.message}")
            com.example.advancedinventory.util.Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun signupFirebase(email: String,password: String): Resource<FirebaseUser>{
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(email).build()
            )
            Resource.Success(authResult.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("AuthRepository", "$email,signup: ${e.message}")

            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun logoutFirebase(){
        firebaseAuth.signOut()
    }

}
