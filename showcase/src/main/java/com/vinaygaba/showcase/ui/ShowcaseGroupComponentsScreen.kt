package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseGroupComponentsScreen(
    groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>
) {
    val groupComponentsList = groupedComponentMap[ShowcaseBrowserScreenMetadata.currentGroup] ?: return
    AdapterList(data = groupComponentsList) { groupComponent ->
        ComponentCardTitle(groupComponent.componentName)
        Clickable(onClick = {
            ShowcaseBrowserScreenMetadata.currentScreen =
                ShowcaseCurrentScreen.COMPONENT_DETAIL
            ShowcaseBrowserScreenMetadata.currentComponent = groupComponent.componentName
        }) {
            ComponentCard(groupComponent)
        }
    }
}
