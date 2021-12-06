package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

object WrapperObject {
    @ShowkaseComposable("name", "group")
    @Composable
    fun TestComposable() {
    
    }
}