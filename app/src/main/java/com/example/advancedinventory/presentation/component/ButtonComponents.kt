package com.example.advancedinventory.presentation.component


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advancedinventory.ui.theme.OrangePrimary


@Composable
fun SmallBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = OrangePrimary,
            contentColor = Color.White
        ),
        modifier = Modifier
            .height(30.dp)
            .wrapContentWidth(),
        shape = RoundedCornerShape(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
        )

    }
}

@Composable
fun LargeBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = OrangePrimary,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(51.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight(700),
            )
        )

    }
}
@Composable
fun LargeRedBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(51.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight(700),
            )
        )

    }
}