package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.runtime.Composable

public class ParameterProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf(
            "String1",
            "String2"
        )

    override val count: Int
        get() = super.count
}

public class WrapperClass {
    @ShowkaseComposable
    @Composable
    public fun TestComposable1() {
        
    }
}


@ShowkaseComposable
@Composable
public fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
    
}