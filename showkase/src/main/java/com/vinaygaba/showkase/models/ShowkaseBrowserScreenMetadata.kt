package com.vinaygaba.showkase.models

internal enum class ShowkaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS,
    COMPONENT_DETAIL,
}

internal data class ShowkaseBrowserScreenMetadata(
    val currentScreen: ShowkaseCurrentScreen = ShowkaseCurrentScreen.GROUPS,
    val currentGroup: String? = null,
    val currentComponent: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null
)
