@file:Suppress("PackageName")

package com.vinaygaba.showkase_browser_testing_submodule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Submodule Component", group = "Submodule")
@Composable
fun SubmoduleComposable() {
    BasicText(text = "Submodule Component")
}

@CustomFontSizePreview
@Composable
fun CustomSubmoduleText() {
    BasicText(text = "Submodule text composable")
}

@CustomSizePreview
@Composable
fun CustomShape() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(Color.Blue)
            .clip(CircleShape)
    )
}
