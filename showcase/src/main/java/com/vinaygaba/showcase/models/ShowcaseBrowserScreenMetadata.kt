package com.vinaygaba.showcase.models

import androidx.compose.getValue
import androidx.compose.mutableStateOf
import androidx.compose.setValue

internal enum class ShowcaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS,
    COMPONENT_DETAIL,
}

internal class ShowcaseBrowserScreenMetadata {
    var currentScreen by mutableStateOf(ShowcaseCurrentScreen.GROUPS)
    var currentGroup: String? by mutableStateOf(null)
    var currentComponent: String?  by mutableStateOf(null)
    var isSearchActive by mutableStateOf(false)
    var searchQuery: String? by mutableStateOf(null)
}
