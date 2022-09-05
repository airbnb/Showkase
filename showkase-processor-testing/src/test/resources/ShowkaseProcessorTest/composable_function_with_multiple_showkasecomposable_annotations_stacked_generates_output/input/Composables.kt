package com.airbnb.android.showkase_processor_testing

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(
    name = "small font",
    group = "font scales",
    widthDp = 250,
    heightDp = 250,
)
@ShowkaseComposable(
    name = "large font",
    group = "font scales",
    widthDp = 200,
    heightDp = 200,
)
@Composable
public fun StackedShowkaseComposables() {

}