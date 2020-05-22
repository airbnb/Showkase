package com.vinaygaba.browser.models

import androidx.compose.Composable

data class ShowcaseCodegenMetadata(
    val group: String,
    val componentName: String,
    val component: @Composable () -> Unit
)
