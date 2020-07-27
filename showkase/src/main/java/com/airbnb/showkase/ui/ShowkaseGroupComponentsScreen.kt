package com.airbnb.showkase.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.Modifier
import androidx.ui.foundation.clickable
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.unit.dp
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
    LazyColumnItems(
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
                currentComponent = null
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
