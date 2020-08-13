package com.airbnb.submodule

import androidx.compose.ui.graphics.Color
import com.airbnb.showkase.annotation.models.ShowkaseColor

class SubmoduleColor {
    @ShowkaseColor("Pink", "Default Colors")
    val pink = Color(android.graphics.Color.parseColor("#FFD7D7"))
}
