package com.example.advancedinventory.presentation.screen.auth.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.advancedinventory.R
import com.example.advancedinventory.presentation.component.IconTextField
import com.example.advancedinventory.presentation.component.LargeBtn
import com.example.advancedinventory.presentation.component.PasswordTextField
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.presentation.screen.auth.AuthViewModel
import com.example.advancedinventory.ui.theme.BlackPrimary
import com.example.advancedinventory.ui.theme.GreyPrimary
import com.example.advancedinventory.util.Resource

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var confirmpassword by remember { mutableStateOf("") }
    var showPasswordMismatchDialog by remember { mutableStateOf(false) }

    val isFormValid =
        email.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()
    val isPasswordMatching = password == confirmpassword

    val signupFlow = authViewModel.signupFlow.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 76.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.multitasking),
            contentDescription = "Login",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Segera Daftarkan Diri Anda!",
            color = Color.Black,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = GreyPrimary,  blurRadius = 3f
                )
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        IconTextField(
            value = email,
            label = "Email",
            iconText = androidx.compose.ui.res.painterResource(id = com.example.advancedinventory.R.drawable.ic_email),
            onValueChange = { email = it },
            modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
        PasswordTextField(
            value = password,
            label = "Password",
            onValueChange = { password = it },
            iconText = painterResource(id = com.example.advancedinventory.R.drawable.ic_lock),

            )
        Spacer(modifier = Modifier.height(12.dp))
        PasswordTextField(
            value = confirmpassword,
            label = "Konfirmasi Password",
            onValueChange = { confirmpassword = it },
            iconText = painterResource(id = com.example.advancedinventory.R.drawable.ic_lock),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LargeBtn(
                text = "Daftar",
        onClick = {
            if (isPasswordMatching) {
                authViewModel.signUp(email, password)
                Toast.makeText(context, "Berhasil Daftar Akun", Toast.LENGTH_LONG).show()
                navController.navigate(Screen.LoginScreen.route)
            } else {
                showPasswordMismatchDialog = true
            }
        },
        enabled = isFormValid,
        modifier = Modifier,

        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sudah punya akun?",
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = Color(0xFF696B76)
            )
            Spacer(modifier = Modifier.width(2.dp))
            ClickableText(
                modifier = Modifier.padding(top = 2.dp),
                text = AnnotatedString("Masuk"),
                onClick = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                style = TextStyle(
                    fontWeight = FontWeight(700),
                    fontSize = 16.sp,

                    color = Color(0xFF696B76),

                    )
            )
        }
    }
    signupFlow.value.let {
        when (it) {
            is Resource.Error -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }

            is Resource.Idle -> {
                // Do nothing
            }

            null -> {
                // Do nothing
            }
        }
    }

    if (showPasswordMismatchDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordMismatchDialog = false },
            title = { Text("Error") },
            text = { Text("Password dan konfirmasi password tidak cocok") },
            confirmButton = {
                Button(onClick = { showPasswordMismatchDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}