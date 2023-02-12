package com.airbnb.android.showkase_processor_testing

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "small font",
    group = "font scales",
    fontScale = 0.5f
)
@Preview(
    name = "large font",
    group = "font scales",
    fontScale = 1.5f
)
public annotation class FontScalePreviews

@Preview(
    name = "small screen",
    group = "device",
)
@Preview(
    name = "large Screen",
    group = "device",
)
public annotation class DevicePreviews

public class Composables {

    @Preview(name = "component", group = "component-group")
    @FontScalePreviews
    @Composable
    public fun Component() {
    }
}

@Preview(
    name = "dark theme",
    group = "themes",
    uiMode = UI_MODE_NIGHT_YES
)
public annotation class CombinedPreviews

@CombinedPreviews
@Composable
public fun HelloWorldPreview() {

}