package com.airbnb.android.showkase.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clearActiveSearch

@Composable
internal fun ShowkaseComponentStylesScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val componentStylesList =
        groupedComponentMap[showkaseBrowserScreenMetadata.currentGroup]
            ?.filter { it.componentName == showkaseBrowserScreenMetadata.currentComponentName  }
            ?.sortedWith { a, b ->
                when {
                    a.isDefaultStyle -> -1
                    b.isDefaultStyle -> 1
                    else -> a.styleName.orEmpty().compareTo(b.styleName.orEmpty())
                }
            } ?: return
    val filteredList = remember(componentStylesList, showkaseBrowserScreenMetadata.searchQuery) {
            getFilteredSearchList(
                componentStylesList,
                isSearchActive = showkaseBrowserScreenMetadata.isSearchActive,
                searchQuery = showkaseBrowserScreenMetadata.searchQuery,
            )
        }
    LazyColumn {
        items(
            items = filteredList,
            itemContent = { groupComponent ->
                val styleName = generatedStyleName(groupComponent.styleName, componentStylesList.size)
                ComponentCardTitle(
                    "${groupComponent.componentName} $styleName"
                )
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
                        navigateTo(ShowkaseCurrentScreen.COMPONENT_DETAIL)
                    }
                )
            }
        )
    }
    BackHandler {
        back(
            showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata,
            navigateTo
        )
    }
}

private fun generatedStyleName(
    styleName: String?,
    componentsSize: Int
) = when {
    !styleName.isNullOrEmpty() -> "[$styleName]"
    componentsSize == 1 -> "[Default Style]"
    else -> ""
}

private fun back(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val isSearchActive = showkaseBrowserScreenMetadata.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> {
            onUpdateShowkaseBrowserScreenMetadata(
                showkaseBrowserScreenMetadata.copy(
                    currentComponentStyleName = null,
                    isSearchActive = false,
                    searchQuery = null
                )
            )
            navigateTo(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP)
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
        searchQuery?.isNotBlank() -> {
            list.filter {
                matchSearchQuery(
                    searchQuery!!,
                    it.componentName,
                    it.styleName.orEmpty()
                )
            }
        }
        else -> list
    }

internal fun matchSearchQuery(
    searchQuery: String,
    vararg properties: String
) = properties.any { it.contains(searchQuery, ignoreCase = true) }
