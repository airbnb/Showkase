package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

public class WrapperClass {
    public companion object {
        @ShowkaseComposable("name", "group")
        @Composable
        public fun TestComposable() {
            
        }
    }
}