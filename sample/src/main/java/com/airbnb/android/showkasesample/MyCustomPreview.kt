package com.airbnb.android.showkasesample

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "MyCustomPreview", group = "Custom")
annotation class MyCustomPreview


@MyCustomPreview
@Composable
fun MyCustomComposable() {
    Text(text = "Custom Preview Sample")
}