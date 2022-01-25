@file:Suppress("PackageNaming")
package com.airbnb.android.showkase_browser_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

class WrapperColorClass {
    @ShowkaseColor("Primary", "Light Colors", 0)
    val primary = Color(0xFF6200EE)
}

object WrapperColorObject {
    @ShowkaseColor("Primary Variant", "Light Colors", 1)
    val primaryVariant = Color(0xFF3700B3)
}

@ShowkaseColor("Secondary", "Light Colors", 2)
val secondary = Color(0xFF03DAC6)

@ShowkaseColor("Secondary Variant", "Light Colors", 3)
val secondaryVariant = Color(0xFF018786)
