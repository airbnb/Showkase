package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.runtime.Composable

@Preview
@Composable
fun TestComposable() {
    
}

@Preview
@Composable
fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
    
}

class ParameterProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf(
            "String1",
            "String2"
        )

    override val count: Int
        get() = super.count
}