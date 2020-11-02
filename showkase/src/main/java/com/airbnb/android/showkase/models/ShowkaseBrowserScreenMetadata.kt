package com.airbnb.android.showkase.models

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController

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

internal fun String?.insideGroup() =
    this == ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP.name ||
            this == ShowkaseCurrentScreen.COLORS_IN_A_GROUP.name ||
            this == ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP.name

internal data class ShowkaseBrowserScreenMetadata(
    val currentGroup: String? = null,
    val currentComponent: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String? = null,
)

internal fun MutableState<ShowkaseBrowserScreenMetadata>.clearActiveSearch() {
    Log.e("Clearing Search", "Clearing Search")
    update {
        copy(
            isSearchActive = false,
            searchQuery = null
        )
    }
}

internal fun <T> MutableState<T>.update(block: T.() -> T) {
    value = this.component1().run(block)
    Log.e("Metadat value", value.toString())
}
