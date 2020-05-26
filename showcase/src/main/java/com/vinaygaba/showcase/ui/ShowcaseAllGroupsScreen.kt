package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseAllGroupsScreen(
    groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>
) {
    val filteredList = getFilteredSearchList(groupedComponentMap.keys.toList())

    AdapterList(data = filteredList) { group ->
        Clickable(onClick = {
            ShowcaseBrowserScreenMetadata.currentScreen =
                ShowcaseCurrentScreen.GROUP_COMPONENTS
            ShowcaseBrowserScreenMetadata.currentGroup = group
        }) {
            Card(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp)) {
                Text(
                    text = group, modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontSize = 20.sp, fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

internal fun getFilteredSearchList(list: List<String>) =
    when (ShowcaseBrowserScreenMetadata.isSearchActive) {
        false -> list
        !ShowcaseBrowserScreenMetadata.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.toLowerCase().contains(ShowcaseBrowserScreenMetadata.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
