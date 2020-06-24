package com.vinaygaba.showcase.models

internal enum class ShowcaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS,
    COMPONENT_DETAIL,
}

internal data class ShowcaseBrowserScreenMetadata(
    val currentScreen: ShowcaseCurrentScreen = ShowcaseCurrentScreen.GROUPS,
    val currentGroup: String? = null,
    val currentComponent: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null
)
