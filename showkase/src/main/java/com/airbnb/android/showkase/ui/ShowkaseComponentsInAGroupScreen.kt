package com.airbnb.android.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.MutableState
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseComponentsInAGroupScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
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
                            currentScreen = ShowkaseCurrentScreen.COMPONENT_DETAIL,
                            currentComponent = groupComponent.componentName,
                            isSearchActive = false
                        )
                    }
                }
            )
        }
    )
    BackButtonHandler {
        goBackFromComponentsInAGroupScreen(showkaseBrowserScreenMetadata)
    }
}

private fun goBackFromComponentsInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> showkaseBrowserScreenMetadata.update {
            copy(
                currentScreen = ShowkaseCurrentScreen.COMPONENT_GROUPS,
                currentGroup = null,
                currentComponent = null,
                isSearchActive = false,
                searchQuery = null
            )
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
