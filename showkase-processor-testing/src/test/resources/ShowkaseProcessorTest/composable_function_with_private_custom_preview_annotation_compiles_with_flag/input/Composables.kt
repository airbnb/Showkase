package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    name = "large Screen",
    group = "device",
)
public annotation class DevicePreviews

public class Composables {

    @DevicePreviews
    @Composable
    private fun Component() {
    }
}
