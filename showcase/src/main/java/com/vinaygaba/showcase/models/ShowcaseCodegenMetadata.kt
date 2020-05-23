package com.vinaygaba.showcase.models

import androidx.compose.Composable

// TODO(vinaygaba): Move it to a different module that has Android/Compose dependencies hoooked up. 
// This was added here only because this module has compose dependencies.
data class ShowcaseCodegenMetadata(
    val group: String,
    val componentName: String,
    val component: @Composable () -> Unit
)
 
