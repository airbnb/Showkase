@file:Suppress("PackageNaming")
package com.vinaygaba.showcase_processor_testing

import androidx.compose.Composable
import androidx.ui.core.ContextAmbient
import androidx.ui.foundation.Text
import com.vinaygaba.showcase.annotation.models.Showcase

@Showcase("Composable1", "Group1")
@Composable
fun TestComposable1() {
    Text(text = "Test Composable1")
}

@Showcase("Composable2", "Group1")
@Composable
fun TestComposable2() {
    Text(text = "Test Composable2")
}

@Showcase("Composable3", "Group2")
@Composable
fun TestComposable3() {
    val context = ContextAmbient.current
    Text(text = context.getString(R.string.showcase))
}

@Showcase("Composable4", "Group3")
@Composable
fun TestComposable4() {
    Text(text = "Test Composable4")
}

class WrapperClass {
    @Showcase("Composable5", "Group3")
    @Composable
    fun TestComposable5() {
        Text(text = "Test Composable5")
    }
}
