package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

@ShowkaseComposable("name", "component")
@Composable
public fun TestComposable() {
    
}

@ShowkaseColor("name", "color")
public val red: Color = Color(0xffff0000)
