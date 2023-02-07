package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

public object WrapperClass {
    /**
     * This component shows some static text in cursive text style.
     *
     * Example usage:
     *
     * ```
     * @Composable
     * fun MyComposable() { CursiveTextComponentPreview() }
     * ```
     */
    @ShowkaseComposable("name", "group")
    @Composable
    public fun TestComposable() {
        
    }
}