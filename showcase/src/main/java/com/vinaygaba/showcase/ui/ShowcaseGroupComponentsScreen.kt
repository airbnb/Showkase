package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.testTag
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseGroupComponentsScreen(
    groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>
) {
    val groupComponentsList =
        groupedComponentMap[ShowcaseBrowserScreenMetadata.currentGroup] ?: return
    val filteredList = getFilteredSearchList(groupComponentsList)
    AdapterList(data = filteredList, modifier = Modifier.testTag("GroupComponentsList")) { groupComponent ->
        ComponentCardTitle(groupComponent.componentName)
        Clickable(onClick = {
            ShowcaseBrowserScreenMetadata.currentScreen =
                ShowcaseCurrentScreen.COMPONENT_DETAIL
            ShowcaseBrowserScreenMetadata.currentComponent = groupComponent.componentName
            ShowcaseBrowserScreenMetadata.isSearchActive = false
        }) {
            ComponentCard(
                metadata = groupComponent,
                cardModifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp)
            )
        }
    }
}

internal fun getFilteredSearchList(list: List<ShowcaseCodegenMetadata>) =
    when (ShowcaseBrowserScreenMetadata.isSearchActive) {
        false -> list
        !ShowcaseBrowserScreenMetadata.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.componentName.toLowerCase()
                    .contains(ShowcaseBrowserScreenMetadata.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
