@file:Suppress("PackageName")

package com.vinaygaba.showkase_browser_testing_submodule

import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "CustomSize 100 * 100", widthDp = 100, heightDp = 100, group = "CustomSubmodulePreview")
@Preview(name = "CustomSize 200 * 200", widthDp = 200, heightDp = 200, group = "CustomSubmodulePreview")
annotation class CustomSizePreview

@Preview(name = "Custom Font Size 1.2f", fontScale = 1.2f, group = "Custom Size Submodule")
annotation class CustomFontSizePreview
