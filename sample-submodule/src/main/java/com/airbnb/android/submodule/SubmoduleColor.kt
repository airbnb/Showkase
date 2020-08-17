package com.airbnb.android.submodule

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

class SubmoduleColor {
    @ShowkaseColor("Pink", "Default Colors")
    val pink = Color(android.graphics.Color.parseColor("#FFD7D7"))
}
