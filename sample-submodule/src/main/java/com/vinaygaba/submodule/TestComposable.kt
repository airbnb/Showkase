package com.vinaygaba.submodule

import androidx.compose.Composable
import androidx.ui.foundation.Text
import com.vinaygaba.showkase.annotation.models.Showkase

@Showkase(name = "SubModule Composable1", group = "Submodule")
@Composable
fun DummyComposable1() {
    Text(text = "SubModule Composable1 Rocks!")
}

@Showkase(name = "SubModule Composable2", group = "Submodule")
@Composable
fun DummyComposable2() {
    Text(text = "SubModule Composable2 Rocks!")
}

@Showkase(name = "SubModule Composable3", group = "Submodule")
@Composable
fun DummyComposable3() {
    Text(text = "SubModule Composable3 Rocks!")
}
