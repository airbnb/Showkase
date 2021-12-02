package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

class ShowkaseClass {
    companion object {
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    }
}