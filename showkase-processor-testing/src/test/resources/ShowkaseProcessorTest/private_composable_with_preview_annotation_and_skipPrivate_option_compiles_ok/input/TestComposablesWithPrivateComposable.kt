package com.airbnb.android.showkase_processor_testing
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview("name1", "group1")
@Composable
public fun TestComposable1() {

}

@Preview("name2", "group2")
@Composable
private fun TestComposable2() {

}

@Preview("name3", "group3")
@Composable
public fun TestComposable3() {

}