package com.airbnb.android.showkase_processor_testing

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "light theme",
    group = "themes",
    uiMode = UI_MODE_NIGHT_NO
)
public annotation class ThemePreview

@ThemePreview
@Composable
public fun HelloWorldPreview() {

}
