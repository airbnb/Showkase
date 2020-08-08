package com.airbnb.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.MutableState
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseGroupComponentsScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val groupComponentsList =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup] ?: return
    val filteredList =
        getFilteredSearchList(groupComponentsList, showkaseBrowserScreenMetadata)
    LazyColumnFor(
        items = filteredList,
        itemContent = { groupComponent ->
            ComponentCardTitle(groupComponent.componentName)
            ComponentCard(
                metadata = groupComponent,
                onClick = {
                    showkaseBrowserScreenMetadata.value =
                        showkaseBrowserScreenMetadata.value.copy(
                            currentScreen = ShowkaseCurrentScreen.COMPONENT_DETAIL,
                            currentComponent = groupComponent.componentName,
                            isSearchActive = false
                        )
                }
            )
        }
    )
    BackButtonHandler {
        goBack(showkaseBrowserScreenMetadata)
    }
}

private fun goBack(showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>) {
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
                currentScreen = ShowkaseCurrentScreen.GROUPS,
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
