package com.vinaygaba.showkase_browser_testing_submodule.two

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable

@LocalePreview
@Composable
fun PreviewText() {
    BasicText(text = "Some text")
}

@EnglishLocalePreview
@Composable
fun PreviewEnglishText() {
    BasicText(text = "Some text In locale")
}

@EnglishLocalePreview
@Composable
private fun PrivateTextComposable() {
    BasicText(text = "Private Text Composable")
}