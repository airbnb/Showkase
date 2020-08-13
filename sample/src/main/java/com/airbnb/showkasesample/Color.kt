package com.airbnb.showkasesample

import androidx.compose.ui.graphics.Color
import com.airbnb.showkase.annotation.models.ShowkaseColor


class WrapperClass {
    @ShowkaseColor("Primary", "Light Colors")
    val primary = Color(0xFF6200EE)
}

object WrapperObjet {
    @ShowkaseColor("Primary Variant", "Light Colors")
    val primaryVariant = Color(0xFF3700B3)
}

@ShowkaseColor("Secondary", "Light Colors")
val secondary = Color(0xFF03DAC6)

@ShowkaseColor("Secondary Variant", "Light Colors")
val secondaryVariant = Color(0xFF018786)

@ShowkaseColor("Background", "Light Colors")
val background = Color.White

@ShowkaseColor("Surface", "Light Colors")
val surfacce = Color.White

@ShowkaseColor("Error", "Light Colors")
val error = Color(0xFFB00020)
