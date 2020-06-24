package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.clickable
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseBrowserComponent
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseGroupComponentsScreen(
    groupedComponentMap: Map<String, List<ShowcaseBrowserComponent>>,
    showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>
) {
    val groupComponentsList =
        groupedComponentMap[showcaseBrowserScreenMetadata.value.currentGroup] ?: return
    val filteredList = getFilteredSearchList(groupComponentsList, showcaseBrowserScreenMetadata)
    AdapterList(data = filteredList) { groupComponent ->
        ComponentCardTitle(groupComponent.componentName)
        ComponentCard(
            metadata = groupComponent,
            cardModifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp) + Modifier.clickable(
                onClick = {
                    showcaseBrowserScreenMetadata.value = showcaseBrowserScreenMetadata.value.copy(
                        currentScreen = ShowcaseCurrentScreen.COMPONENT_DETAIL,
                        currentComponent = groupComponent.componentName,
                        isSearchActive = false
                    )
                }
            )
        )
    }
    BackButtonHandler {
        goBack(showcaseBrowserScreenMetadata)
    }
}

private fun goBack(showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>) {
    val isSearchActive = showcaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> {
            showcaseBrowserScreenMetadata.value = showcaseBrowserScreenMetadata.value.copy(
                isSearchActive = false,
                searchQuery = null
            )
        }
        else -> {
            showcaseBrowserScreenMetadata.value = showcaseBrowserScreenMetadata.value.copy(
                currentScreen = ShowcaseCurrentScreen.GROUPS,
                currentGroup = null,
                currentComponent = null
            )
        }
    }
}


private fun getFilteredSearchList(
    list: List<ShowcaseBrowserComponent>,
    showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>
) =
    when (showcaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showcaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.componentName.toLowerCase()
                    .contains(showcaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
