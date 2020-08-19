package com.airbnb.android.showkase.models

import androidx.compose.runtime.Composable

// TODO(vinaygaba): Move it to a different module that has Android/Compose dependencies hoooked up. 
// This was added here only because this module has compose dependencies.
data class ShowkaseBrowserComponent(
    val group: String,
    val componentName: String,
    val componentKDoc: String,
    val component: @Composable () -> Unit,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
)
