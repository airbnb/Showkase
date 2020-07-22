package com.airbnb.showkasesample

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette

@Composable
fun ShowkaseTheme(children: @Composable()() -> Unit) {
    val light = lightColorPalette()
    val dark = darkColorPalette()
    val colors = if (isSystemInDarkTheme()) { dark } else { light }
    MaterialTheme(colors = colors) {
        children()
    }
}
