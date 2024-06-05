package com.airbnb.android.showkase.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch

@Composable
internal fun ShowkaseComponentsInAGroupScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val groupByComponentName =
        groupedComponentMap[showkaseBrowserScreenMetadata.currentGroup]
            ?.groupBy { it.componentName } ?: return
    // Use the default style as the preview if its available or take the first style for the component
    val componentList = groupByComponentName.values.map {
        it.firstOrNull { it.isDefaultStyle } ?: it.first()
    }
    val filteredList = getFilteredSearchList(
        list = componentList,
        isSearchActive = showkaseBrowserScreenMetadata.isSearchActive,
        searchQuery = showkaseBrowserScreenMetadata.searchQuery
    )
    LazyColumn {
        items(
            items = filteredList,
            itemContent = { groupComponent ->
                ComponentCardTitle(groupComponent.componentName)
                ComponentCard(
                    metadata = groupComponent,
                    onClick = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(
                                currentComponentKey = groupComponent.componentKey,
                                currentComponentName = groupComponent.componentName,
                                currentComponentStyleName = groupComponent.styleName,
                                isSearchActive = false
                            )
                        )
                        navigateTo(ShowkaseCurrentScreen.COMPONENT_STYLES)
                    }
                )
            }
        )
    }
    BackHandler {
        goBackFromComponentsInAGroupScreen(showkaseBrowserScreenMetadata, onUpdateShowkaseBrowserScreenMetadata, navigateTo)
    }
}

private fun goBackFromComponentsInAGroupScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val isSearchActive = showkaseBrowserScreenMetadata.isSearchActive
    when {
        isSearchActive -> onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clearActiveSearch())
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navigateTo(ShowkaseCurrentScreen.COMPONENT_GROUPS)
        }
    }
}


private fun getFilteredSearchList(
    list: List<ShowkaseBrowserComponent>,
    isSearchActive: Boolean,
    searchQuery: String?,
) =
    when (isSearchActive) {
        false -> list
        !searchQuery.isNullOrBlank() -> {
            list.filter {
                matchSearchQuery(
                    searchQuery!!,
                    it.componentName
                )
            }
        }
        else -> list
    }
