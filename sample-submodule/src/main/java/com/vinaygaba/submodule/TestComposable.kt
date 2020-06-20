package com.vinaygaba.submodule

import androidx.compose.Composable
import androidx.ui.foundation.Text
import com.vinaygaba.showcase.annotation.models.Showcase

@Showcase(name = "SubModule Composable1", group = "Submodule")
@Composable
fun DummyComposable1() {
    Text(text = "SubModule Composable1 Rocks!")
}

@Showcase(name = "SubModule Composable2", group = "Submodule")
@Composable
fun DummyComposable2() {
    Text(text = "SubModule Composable2 Rocks!")
}

@Showcase(name = "SubModule Composable3", group = "Submodule")
@Composable
fun DummyComposable3() {
    Text(text = "SubModule Composable3 Rocks!")
}
