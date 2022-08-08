package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

public object ShowkaseObject {
    @ShowkaseColor("name", "group")
    public val color: Color = Color(0xffff0000)
}