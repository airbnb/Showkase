package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

public class WrapperClass {
    public companion object {
        @ShowkaseComposable("name", "group")
        @Composable
        public fun TestComposable() {
        
        }
    }
}