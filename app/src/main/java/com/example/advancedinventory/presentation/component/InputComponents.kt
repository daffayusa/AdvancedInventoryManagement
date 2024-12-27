package com.example.advancedinventory.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.advancedinventory.R
import com.example.advancedinventory.ui.theme.BlackPrimary
import com.example.advancedinventory.ui.theme.GreyPrimary
import com.example.advancedinventory.ui.theme.OrangePrimary

@Composable
fun NormalTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            androidx.compose.material3.Text(text = label)
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent, RoundedCornerShape(10.dp)),
        textStyle = TextStyle(
            color = BlackPrimary
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangePrimary,
            unfocusedBorderColor = BlackPrimary,
            cursorColor = OrangePrimary,
            focusedLabelColor = BlackPrimary,

            )
    )

}
@Composable
fun IconTextField(
    value: String,
    label: String,
    iconText:Painter,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        leadingIcon = {
            Icon(
                painter = iconText,
                contentDescription = null,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
        },
        value = value,
        onValueChange = onValueChange,
        label = {
            androidx.compose.material3.Text(text = label)
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent, RoundedCornerShape(10.dp)),
        textStyle = TextStyle(
            color = BlackPrimary
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangePrimary,
            unfocusedBorderColor = BlackPrimary,
            cursorColor = OrangePrimary,
            focusedLabelColor = BlackPrimary,

        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    iconText: Painter,
    label: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

    OutlinedTextField(
        leadingIcon = {
            Icon(
                painter = iconText,
                contentDescription = null,
                modifier = Modifier
                    .width(25.dp)
                    .width(25.dp)
            )
        },
        value = value,
        textStyle = TextStyle(
            color = BlackPrimary
        ),
        onValueChange = onValueChange,
        label = {
            androidx.compose.material3.Text(text = label)
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangePrimary,
            unfocusedBorderColor = BlackPrimary,
            cursorColor = OrangePrimary,
            focusedLabelColor = BlackPrimary,

        ),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
        trailingIcon = {
            IconButton(
                onClick = {
                    passwordVisible = !passwordVisible
                }
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "vis"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}