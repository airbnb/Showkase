package com.airbnb.android.showkase.models

import androidx.compose.runtime.MutableState

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

internal fun ShowkaseCurrentScreen.insideGroup() =
    this == ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP ||
            this == ShowkaseCurrentScreen.COLORS_IN_A_GROUP ||
            this == ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP

internal data class ShowkaseBrowserScreenMetadata(
    val currentScreen: ShowkaseCurrentScreen = ShowkaseCurrentScreen.SHOWKASE_CATEGORIES,
    val currentGroup: String? = null,
    val currentComponent: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null,
)

internal fun MutableState<ShowkaseBrowserScreenMetadata>.clearActiveSearch() {
    update {
        copy(
            isSearchActive = false,
            searchQuery = null
        )
    }
}

internal fun <T> MutableState<T>.update(block: T.() -> T) {
    value = this.component1().run(block)
}
