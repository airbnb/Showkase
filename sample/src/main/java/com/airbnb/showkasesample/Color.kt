package com.airbnb.showkasesample

import androidx.compose.ui.graphics.Color
import com.airbnb.showkase.annotation.models.ShowkaseColor


class WrapperClass {
    @ShowkaseColor("Red", "Default Colors")
    val redColor = Color(0xffff0000)
}

object WrapperObjet {
    @ShowkaseColor("Blue", "Default Colors")
    val redColor = Color(0xff0000ff)
}

@ShowkaseColor("Green", "Default Colors")
val green = Color.Green
