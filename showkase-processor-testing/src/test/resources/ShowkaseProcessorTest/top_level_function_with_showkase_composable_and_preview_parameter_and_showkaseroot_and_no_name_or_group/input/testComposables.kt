package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.runtime.Composable

class ParameterProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf(
            "String1",
            "String2"
        )

    override val count: Int
        get() = super.count
}

@ShowkaseComposable
@Composable
fun TestComposable() {
    
}

@ShowkaseComposable
@Composable
fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
    
}