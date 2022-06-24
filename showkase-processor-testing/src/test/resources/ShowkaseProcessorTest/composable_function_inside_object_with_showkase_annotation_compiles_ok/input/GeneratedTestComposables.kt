package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

public object ShowkaseObject {
    @ShowkaseComposable("name", "group")
    @Composable
    public fun TestComposable() {
        
    }
}