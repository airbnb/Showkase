package com.airbnb.submodule

import androidx.compose.ui.graphics.Color
import com.airbnb.showkase.annotation.models.ShowkaseColor

class WrapperClass {
    @ShowkaseColor("Red", "Default Colors")
    val redColor = Color(0xffff0000)

    @ShowkaseColor("Grey", "Default Colors")
    val grey = Color(0xffe5e5e5)

    @ShowkaseColor("Green", "Default Colors")
    val green = Color.Cyan

    @ShowkaseColor("Yellow", "Default Colors")
    val Yellow = Color(android.graphics.Color.parseColor("#FFD7D7"))
}
