package com.vinaygaba.browser

import androidx.compose.Composable

data class ShowcaseCodegenMetadata(
    val group: String,
    val componentName: String,
    val component: @Composable () -> Unit
)
