package com.airbnb.android.submodule.showkasesample

import androidx.compose.ui.tooling.preview.Preview

/**
 * Custom preview annotation. This is used to annotate
 * composable and generates two previews based on the annotations.
 *
 */
@Preview(name = "names1", group = "CustomPreviewAnnotationGroup")
@Preview(name = "names2", group = "CustomPreviewAnnotationGroup")
annotation class CustomPreviewAnnotation
