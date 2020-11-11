package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseTypographyGroupsScreen(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
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
                        currentGroup = group,
                        isSearchActive = false,
                        searchQuery = null
                    )
                }
                navController.navigate(ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP)
            }
        )
    })
    BackButtonHandler {
        goBackToCategoriesScreen(showkaseBrowserScreenMetadata, navController)
    }
}
