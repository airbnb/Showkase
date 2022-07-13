package com.airbnb.android.showkase_processor_testing_my_very_long_name

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.runtime.Composable


public object WrapperClass {
    @Preview(name = "name", group = "group")
    @Composable
    public fun TestComposable(@PreviewParameter(provider = MyVeryLongPackageNameViewStateSomethingSomethingFunnyStuffProvider::class) text: String) {
        
    }
}