package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Preview("name1", "group1")
@ShowkaseComposable("name2", "group2")
@Composable
public fun TestComposable1() {
    
}