package com.airbnb.android.showkase.models

import androidx.compose.runtime.MutableState

internal enum class ShowkaseCurrentScreen {
    COMPONENT_GROUPS,
    COMPONENTS_IN_A_GROUP,
    COMPONENT_STYLES,
    COMPONENT_DETAIL,
    SHOWKASE_CATEGORIES,
    COLOR_GROUPS,
    COLORS_IN_A_GROUP,
    TYPOGRAPHY_GROUPS,
    TYPOGRAPHY_IN_A_GROUP,
}

internal fun String?.insideGroup() =
    this == ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP.name ||
            this == ShowkaseCurrentScreen.COLORS_IN_A_GROUP.name ||
            this == ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP.name

internal data class ShowkaseBrowserScreenMetadata(
    val currentGroup: String? = null,
    val currentComponentName: String? = null,
    val currentComponentStyleName: String? = null,
    val currentComponentKey: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null,
)

internal fun MutableState<ShowkaseBrowserScreenMetadata>.clear() {
    update {
        copy(
            isSearchActive = false,
            searchQuery = null,
            currentComponentKey = null,
            currentComponentName = null,
            currentComponentStyleName = null,
            currentGroup = null
        )
    }
}

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
