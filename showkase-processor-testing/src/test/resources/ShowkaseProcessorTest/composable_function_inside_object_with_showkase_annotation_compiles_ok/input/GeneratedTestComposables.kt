package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

object ShowkaseObject {
    @ShowkaseComposable("name", "group")
    @Composable
    fun TestComposable() {
        
    }
}