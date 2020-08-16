package com.airbnb.android.showkase.models

internal enum class ShowkaseCurrentScreen {
    COMPONENT_GROUPS,
    COMPONENTS_IN_A_GROUP,
    COMPONENT_DETAIL,
    SHOWKASE_CATEGORIES,
    COLOR_GROUPS,
    COLORS_IN_A_GROUP,
    TYPOGRAPHY_GROUPS,
    TYPOGRAPHY_IN_A_GROUP,
}

internal data class ShowkaseBrowserScreenMetadata(
    val currentScreen: ShowkaseCurrentScreen = ShowkaseCurrentScreen.SHOWKASE_CATEGORIES,
    val currentGroup: String? = null,
    val currentComponent: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null
)
