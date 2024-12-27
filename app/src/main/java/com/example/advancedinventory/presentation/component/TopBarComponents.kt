package com.example.advancedinventory.presentation.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.advancedinventory.R
import com.example.advancedinventory.presentation.navigation.Screen
import com.example.advancedinventory.ui.theme.BlackPrimary
import com.example.advancedinventory.ui.theme.PrimaryWhite


@Composable
fun TopBarComponentProfile(
    userName: String?,
    navController: NavHostController
) {
    Log.d("TopBar", "TopBarComponentProfile called with username: $userName")
    Column(modifier = Modifier.background(PrimaryWhite).padding(top = 32.dp, bottom = 8.dp)) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier

                        .clip(RoundedCornerShape(180.dp))
                        .width(50.dp)
                        .height(50.dp),
                    painter = painterResource(id = R.drawable.profile_photo1),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Bonjour,",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(400),
                            fontStyle = FontStyle.Italic,
                        )
                    )
                    Text(
                        text = userName ?: "Guest",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(500),
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(title: String, navController: NavHostController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(PrimaryWhite),
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = BlackPrimary,
                    textAlign = TextAlign.Center,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()

            )

        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        },
        modifier = Modifier
            .background(Color.White)

    )
}