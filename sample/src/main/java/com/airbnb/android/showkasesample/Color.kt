package com.airbnb.android.showkasesample

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor


class WrapperClass {
    @ShowkaseColor("Primary", "Light Colors", 0)
    val primary = Color(0xFF6200EE)
}

object WrapperObject {
    @ShowkaseColor("Primary Variant", "Light Colors", 1)
    val primaryVariant = Color(0xFF3700B3)
}

@ShowkaseColor("Secondary", "Light Colors", 2)
val secondary = Color(0xFF03DAC6)

@ShowkaseColor("Secondary Variant", "Light Colors",3)
val secondaryVariant = Color(0xFF018786)

@ShowkaseColor("Background", "Light Colors",4)
val background = Color.White

@ShowkaseColor("Surface", "Light Colors",5)
val surface = Color.White

@ShowkaseColor("Error", "Light Colors",6)
val error = Color(0xFFB00020)
