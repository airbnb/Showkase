@file:Suppress("PackageNaming")
package com.airbnb.android.showkase_browser_testing

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.google.android.material.composethemeadapter.MdcTheme

@ShowkaseComposable("Composable1", "Group1")
@Composable
fun TestComposable1() {
    MdcTheme {
        BasicText(text = "Test Composable1")
    }
}

@ShowkaseComposable("Composable2", "Group1")
@Composable
fun TestComposable2() {
    BasicText(text = "Test Composable2")
}

@ShowkaseComposable("Composable3", "Group2")
@Composable
fun TestComposable3() {
    BasicText(text = "Test Composable3")
}

@ShowkaseComposable("Composable4", "Group3")
@Composable
fun TestComposable4() {
    BasicText(text = "Test Composable4")
}

@ShowkaseComposable(
    "Composable6 Button Component wih a lot of extra stuff like spacing and such",
    "Group4"
)
@Composable
fun TestComposable6() {
    BasicText(text = "Test Composable6")
}

class WrapperComposableClass {
    @ShowkaseComposable("Composable5", "Group3")
    @Composable
    fun TestComposable5() {
        BasicText(text = "Test Composable5")
    }
}


@ShowkaseRoot
class MyRootModule: ShowkaseRootModule
