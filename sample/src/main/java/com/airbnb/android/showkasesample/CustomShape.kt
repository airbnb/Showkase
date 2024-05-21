package com.airbnb.android.showkasesample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.submodule.showkasesample.FontPreview

@Preview(name = "Shape 100 by 100", group = "Shapes", widthDp = 100, heightDp = 100)
@Preview(name = "Shape 150 by 150", group = "Shapes", widthDp = 150, heightDp = 150)
annotation class CustomShape

@CustomShape
@Composable
fun CustomRoundedBlueSquare() {
    Box(modifier = Modifier
        .size(40.dp)
        .background(Color.Blue)
        .clip(RoundedCornerShape(8.dp)))
}

@FontPreview
@Composable
fun CustomRoundedSquareWithText() {
    Box(Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))) {
        Text("This is a rounded square!")
    }
}

@FontPreview
@Composable
private fun PrivateCustomRoundedSquareWithText() {
    Box(Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))) {
        Text("This is a private rounded square!")
    }
}
