package com.vinaygaba.showcase.ui

import androidx.compose.Composable
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
    showcaseBrowserScreenMetadata: ShowcaseBrowserScreenMetadata
) {
    val groupComponentsList =
        groupedComponentMap[showcaseBrowserScreenMetadata.currentGroup] ?: return
    val filteredList = getFilteredSearchList(groupComponentsList, showcaseBrowserScreenMetadata)
    AdapterList(data = filteredList) { groupComponent ->
        ComponentCardTitle(groupComponent.componentName)
        ComponentCard(
            metadata = groupComponent,
            cardModifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp) + Modifier.clickable(
                onClick = {
                    showcaseBrowserScreenMetadata.currentScreen =
                        ShowcaseCurrentScreen.COMPONENT_DETAIL
                    showcaseBrowserScreenMetadata.currentComponent = groupComponent.componentName
                    showcaseBrowserScreenMetadata.isSearchActive = false
                }
            )
        )
    }
    BackButtonHandler {
        goBack(showcaseBrowserScreenMetadata)
    }
}

private fun goBack(showcaseBrowserScreenMetadata: ShowcaseBrowserScreenMetadata) {
    val isSearchActive = showcaseBrowserScreenMetadata.isSearchActive
    when {
        isSearchActive -> {
            showcaseBrowserScreenMetadata.isSearchActive = false
            showcaseBrowserScreenMetadata.searchQuery = null
        }
        else -> {
            showcaseBrowserScreenMetadata.currentScreen =
                ShowcaseCurrentScreen.GROUPS
            showcaseBrowserScreenMetadata.currentGroup = null
            showcaseBrowserScreenMetadata.currentComponent = null
        }
    }
}


private fun getFilteredSearchList(
    list: List<ShowcaseBrowserComponent>,
    showcaseBrowserScreenMetadata: ShowcaseBrowserScreenMetadata
) =
    when (showcaseBrowserScreenMetadata.isSearchActive) {
        false -> list
        !showcaseBrowserScreenMetadata.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.componentName.toLowerCase()
                    .contains(showcaseBrowserScreenMetadata.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
