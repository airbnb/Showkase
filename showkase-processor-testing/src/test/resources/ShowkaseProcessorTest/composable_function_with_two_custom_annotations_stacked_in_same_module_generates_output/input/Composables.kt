package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Custom Preview One First", group = "Custom Previews")
@Preview(name = "Custom Preview One Second", group = "Custom Previews")
public annotation class InternalPreviewOne

@Preview(name = "Custom Preview Two First", group = "Custom Previews")
@Preview(name = "Custom Preview Two Second", group = "Custom Previews")
public annotation class InternalPreviewTwo

@InternalPreviewOne
@InternalPreviewTwo
@Composable
public fun CustomInternalAnnotationPreviewCombined() {

}

