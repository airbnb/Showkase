package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseTypographyGroupsScreen(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val filteredList = getFilteredSearchList(
        groupedTypographyMap.keys.sorted(),
        showkaseBrowserScreenMetadata
    )
    
    LazyColumnFor(items = filteredList, itemContent = { group ->
        SimpleTextCard(
            text = group,
            onClick = {
                showkaseBrowserScreenMetadata.update {
                    copy(
                        currentScreen = ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP,
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
