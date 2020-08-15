package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseColorGroupsScreen(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val filteredList = getFilteredSearchList(
        groupedColorsMap.keys.sorted(),
        showkaseBrowserScreenMetadata
    )
    LazyColumnFor(items = filteredList, itemContent = { group ->
        SimpleTextCard(
            text = group,
            onClick = {
                showkaseBrowserScreenMetadata.value =
                    showkaseBrowserScreenMetadata.value.copy(
                        currentScreen = ShowkaseCurrentScreen.COLORS_IN_A_GROUP,
                        currentGroup = group,
                        isSearchActive = false,
                        searchQuery = null
                    )
            }
        )
    })
    BackButtonHandler {
        goBackFromColorsGroupsScreen(showkaseBrowserScreenMetadata)
    }
}

private fun  goBackFromColorsGroupsScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> {
            showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
                isSearchActive = false,
                searchQuery = null
            )
        }
        else -> {
            showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
                currentScreen = ShowkaseCurrentScreen.SHOWKASE_CATEGORIES,
                currentComponent = null,
                isSearchActive = false,
                searchQuery = null,
                currentGroup = null
            )
        }
    }
}
