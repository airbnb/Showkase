package com.airbnb.android.showkase_processor_testing_my_very_long_name

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

public class MyVeryLongPackageNameViewStateSomethingSomethingFunnyStuffProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf(
            "String1",
            "String2"
        )

    override val count: Int
        get() = super.count
}