package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

class WrapperClass {
    companion object {
        @ShowkaseComposable
        @Composable
        fun testComposable() {
            
        }
    }
}