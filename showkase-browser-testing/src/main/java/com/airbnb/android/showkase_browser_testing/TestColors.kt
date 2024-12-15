@file:Suppress("PackageName")
package com.airbnb.android.showkase_browser_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

class WrapperColorClass {
    @ShowkaseColor("Primary", "Light Colors")
    val primary = Color(0xFF6200EE)
}

object WrapperColorObject {
    @ShowkaseColor("Primary Variant", "Light Colors")
    val primaryVariant = Color(0xFF3700B3)
}

@ShowkaseColor("Secondary", "Light Colors")
val secondary = Color(0xFF03DAC6)

@ShowkaseColor("Secondary Variant", "Light Colors")
val secondaryVariant = Color(0xFF018786)
