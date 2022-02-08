@file:Suppress("PackageNaming")
package com.vinaygaba.showkase_browser_testing_submodule

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Submodule Component", group = "Submodule")
@Composable
fun SubmoduleComposable() {
    BasicText(text = "Submodule Component")
}
