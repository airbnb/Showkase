package com.airbnb.android.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseComponentsInAGroupScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val groupComponentsList =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.sortedBy { it.componentName } ?: return
    val filteredList =
        getFilteredSearchList(groupComponentsList, showkaseBrowserScreenMetadata)
    LazyColumnFor(
        items = filteredList,
        itemContent = { groupComponent ->
            ComponentCardTitle(groupComponent.componentName)
            ComponentCard(
                metadata = groupComponent,
                onClick = {
                    showkaseBrowserScreenMetadata.update {
                        copy(
                            currentComponent = groupComponent.componentName,
                            isSearchActive = false
                        )
                    }
                    navController.navigate(ShowkaseCurrentScreen.COMPONENT_DETAIL)
                }
            )
        }
    )
    BackButtonHandler {
        goBackFromComponentsInAGroupScreen(showkaseBrowserScreenMetadata, navController)
    }
}

private fun goBackFromComponentsInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.COMPONENT_GROUPS)
        }
    }
}


private fun getFilteredSearchList(
    list: List<ShowkaseBrowserComponent>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) =
    when (showkaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.componentName.toLowerCase()
                    .contains(showkaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
