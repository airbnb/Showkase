package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.compose.FrameManager
import androidx.ui.core.Modifier
import androidx.ui.core.testTag
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.clickable
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
        ComponentCard(
            metadata = groupComponent,
            cardModifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp) + Modifier.clickable(
                onClick = {
                    ShowcaseBrowserScreenMetadata.currentScreen =
                        ShowcaseCurrentScreen.COMPONENT_DETAIL
                    ShowcaseBrowserScreenMetadata.currentComponent = groupComponent.componentName
                    ShowcaseBrowserScreenMetadata.isSearchActive = false
                }
            )
        )
    }
    BackButtonHandler {
        goBack()
    }
}

private fun goBack() {
    // FrameManager.framed was added because when I was noticing a crash when it was run as part of 
    // the tests. This was done to avoid it.
    // Crash - java.lang.IllegalStateException: Not in a frame
    // Related discussion - https://kotlinlang.slack.com/archives/CJLTWPH7S/p1591055630230800
    FrameManager.framed {
        val isSearchActive = ShowcaseBrowserScreenMetadata.isSearchActive
        when {
            isSearchActive -> {
                ShowcaseBrowserScreenMetadata.isSearchActive = false
                ShowcaseBrowserScreenMetadata.searchQuery = null
            }
            else -> {
                ShowcaseBrowserScreenMetadata.currentScreen =
                    ShowcaseCurrentScreen.GROUPS
                ShowcaseBrowserScreenMetadata.currentGroup = null
                ShowcaseBrowserScreenMetadata.currentComponent = null
            }
        }
    }
}


private fun getFilteredSearchList(list: List<ShowcaseCodegenMetadata>) =
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
