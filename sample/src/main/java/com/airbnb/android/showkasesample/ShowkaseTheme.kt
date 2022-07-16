package com.airbnb.android.showkasesample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun ShowkaseTheme(children: @Composable () -> Unit) {
    val light = lightColors()
    val dark = darkColors()
    val colors = if (isSystemInDarkTheme()) { dark } else { light }
    MaterialTheme(colors = colors) {
        children()
    }
}
