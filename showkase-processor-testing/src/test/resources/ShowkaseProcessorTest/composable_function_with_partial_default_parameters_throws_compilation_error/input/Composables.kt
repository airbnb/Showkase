package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Preview("name", "group")
@Composable
public fun TestComposable(
    name: String = "",
    age: Int
) {

}