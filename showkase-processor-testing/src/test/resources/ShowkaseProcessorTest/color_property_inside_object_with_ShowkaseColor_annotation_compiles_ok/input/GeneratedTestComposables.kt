package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

object ShowkaseObject {
    @ShowkaseColor("name", "group")
    val color = Color(0xffff0000)
}