package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseColor

@ShowkaseComposable("name", "group")
@Composable
fun TestComposable() {
    
}

class WrapperClass {
    @ShowkaseColor("name", "group")
    val red = Color(0xffff0000)
}