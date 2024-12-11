package com.airbnb.android.showkase.screenshot.testing.paparazzi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// This is for testing that custom annotation
// works with paparazzi
@HighFont
@Composable
fun Preview() {
    Text(
        modifier = Modifier.background(color = Color.White).fillMaxWidth(),
        text = "Gustav"
    )
}
