package com.vinaygaba.subsample

import androidx.compose.Composable
import androidx.ui.foundation.Text
import com.vinaygaba.showcase.annotation.models.Showcase

@Showcase(name = "SubModule Composable", group = "Submodule")
@Composable
fun DummyComposable() {
    Text(text = "This Submodule Rocks!")
}
