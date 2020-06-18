package com.vinaygaba.showcase.annotation.models

// TODO(vinaygaba): Move it to a different module that has Android/Compose dependencies hoooked up. 
// This was added here only because this module has compose dependencies.
data class ShowcaseCodegenMetadata(
    val group: String,
    val componentName: String,
    val widthDp: Int,
    val heightDp: Int,
    val component: () -> Unit
)
 
