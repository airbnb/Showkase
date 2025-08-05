@file:Suppress("PackageName")

package com.vinaygaba.showkase_browser_testing_submodule.two

import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "English", group = "LocalePreview", locale = "en")
annotation class EnglishLocalePreview

@Preview(name = "Norwegian", group = "LocalePreview", locale = "no")
@Preview(name = "English", group = "LocalePreview", locale = "en")
annotation class LocalePreview
