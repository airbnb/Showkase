package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

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
                showkaseBrowserScreenMetadata.update {
                    copy(
                        currentScreen = ShowkaseCurrentScreen.COLORS_IN_A_GROUP,
                        currentGroup = group,
                        isSearchActive = false,
                        searchQuery = null
                    ) 
                }
            }
        )
    })
    BackButtonHandler {
        goBackToCategoriesScreen(showkaseBrowserScreenMetadata)
    }
}
