package com.vinaygaba.browser.ui

import androidx.compose.Composable
import com.vinaygaba.browser.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.browser.models.ShowcaseCodegenMetadata
import com.vinaygaba.browser.models.ShowcaseCurrentScreen

@Composable
fun ShowcaseBrowserApp(groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>) {
    when (ShowcaseBrowserScreenMetadata.currentScreen) {
        ShowcaseCurrentScreen.GROUPS -> {
            ShowcaseAllGroupsScreen(groupedComponentMap)
        }
        ShowcaseCurrentScreen.GROUP_COMPONENTS -> {
            ShowcaseGroupComponentsScreen(
                groupedComponentMap
            )
        }
        ShowcaseCurrentScreen.COMPONENT_DETAIL -> {
            ShowcaseComponentDetailScreen(
                groupedComponentMap
            )
        }
    }
}
