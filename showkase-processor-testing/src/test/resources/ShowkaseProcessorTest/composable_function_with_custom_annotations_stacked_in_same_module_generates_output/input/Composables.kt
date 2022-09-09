package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Custom Preview First", group = "Custom Previews")
@Preview(name = "Custom Preview Second", group = "Custom Previews")
public annotation class InternalCustomPreviewAnnotation

@InternalCustomPreviewAnnotation
@Composable
public fun CustomInternalAnnotationPreview() {

}