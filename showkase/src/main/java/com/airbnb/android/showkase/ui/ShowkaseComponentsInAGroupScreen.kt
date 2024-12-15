package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    val groupByComponentName =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.groupBy { it.componentName } ?: return
    // Use the default style as the preview if its available or take the first style for the component
    val componentList = groupByComponentName.values.map {
        it.firstOrNull { it.isDefaultStyle } ?: it.first()
    }
    val filteredList =
        getFilteredSearchList(componentList, showkaseBrowserScreenMetadata)
    LazyColumn {
        items(
            items = filteredList,
            itemContent = { groupComponent ->
                ComponentCardTitle(groupComponent.componentName)
                ComponentCard(
                    metadata = groupComponent,
                    onClick = {
                        showkaseBrowserScreenMetadata.update {
                            copy(
                                currentComponentKey = groupComponent.componentKey,
                                currentComponentName = groupComponent.componentName,
                                currentComponentStyleName = groupComponent.styleName,
                                isSearchActive = false
                            )
                        }
                        navController.navigate(ShowkaseCurrentScreen.COMPONENT_STYLES)
                    }
                )
            }
        )
    }
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
                matchSearchQuery(
                    showkaseBrowserScreenMetadata.value.searchQuery!!,
                    it.componentName
                )
            }
        }
        else -> list
    }
