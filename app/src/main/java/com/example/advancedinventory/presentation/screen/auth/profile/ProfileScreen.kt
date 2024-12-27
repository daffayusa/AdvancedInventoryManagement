package com.example.advancedinventory.presentation.screen.auth.profile

import androidx.activity.result.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock

import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.advancedinventory.R
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.presentation.screen.auth.AuthViewModel
import com.example.advancedinventory.presentation.screen.barang.UploadImageBox
import com.example.advancedinventory.ui.theme.BlackPrimary
import com.example.advancedinventory.ui.theme.OrangePrimary

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)
    var showLogoutDialog by remember { mutableStateOf(false) }
    val currentUser = viewModel.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangePrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(
            modifier = Modifier.background(OrangePrimary) . padding(top = 48.dp)
        ) {
            Image(
                painter = painterResource(id= R.drawable.profile_photo1),
                modifier =  Modifier
                    .padding(top = 56.dp)
                    .size(160.dp)
                    .clip(CircleShape),
                contentDescription = "Profile Image",
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier .height(8.dp))
            Text(
                text = currentUser?.displayName ?: "Guest",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = BlackPrimary
                )
            )
            Spacer(modifier = Modifier .height(18.dp))
        }
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(color = Color.White)
                .padding(start = 16.dp, end = 16.dp) .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
                    .background(color = Color.White)
            ){
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .pressClickEffect()
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                        )
                        Text(
                            text = "Edit Profile",
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 27.sp,
                                fontWeight = FontWeight(500),
                                color = BlackPrimary,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                    }

                }

                HorizontalDivider()

                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .pressClickEffect()
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                        )
                        Text(
                            text = "Ganti Kata Sandi",
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 27.sp,
                                fontWeight = FontWeight(500),
                                color = BlackPrimary,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                    }

                }
                HorizontalDivider()

                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .pressClickEffect()
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                        )
                        Text(
                            text = "Hapus Akun",
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 27.sp,
                                fontWeight = FontWeight(500),
                                color = BlackPrimary,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                    }

                }
                HorizontalDivider()
                IconButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .pressClickEffect()
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                        )
                        Text(
                            text = "Keluar",
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 27.sp,
                                fontWeight = FontWeight(500),
                                color = BlackPrimary,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                    }

                }


                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = { Text(text = "Konfirmasi Logout") },
                        text = { Text(text = "Apakah Anda yakin ingin logout?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showLogoutDialog = false
                                    viewModel.logout()
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            ) {
                                Text("Ya")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showLogoutDialog = false }
                            ) {
                                Text("Tidak")
                            }
                        }
                    )
                }
            }
        }
    }






}
enum class ButtonState { Pressed, Idle }
fun Modifier.pressClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}