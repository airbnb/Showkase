package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseComponentGroupsScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val filteredList = getFilteredSearchList(
        groupedComponentMap.keys.sorted(),
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
                navController.navigate(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP)
            }
        )
    })
    BackButtonHandler {
        goBackToCategoriesScreen(showkaseBrowserScreenMetadata, navController)
    }
}

internal fun getFilteredSearchList(
    list: List<String>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) =
    when (showkaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.toLowerCase()
                    .contains(showkaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
