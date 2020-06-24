package com.vinaygaba.showcase.ui

import androidx.activity.ComponentActivity
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseBrowserComponent
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseAllGroupsScreen(
    groupedComponentMap: Map<String, List<ShowcaseBrowserComponent>>,
    showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>
) {
    val filteredList = getFilteredSearchList(groupedComponentMap.keys.toList(), 
        showcaseBrowserScreenMetadata)
    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

    AdapterList(data = filteredList) { group ->
        Card(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp) + Modifier.clickable(
            onClick = {
                showcaseBrowserScreenMetadata.value = showcaseBrowserScreenMetadata.value.copy(
                    currentScreen = ShowcaseCurrentScreen.GROUP_COMPONENTS,
                    currentGroup = group,
                    isSearchActive = false
                )
            }
        )) {
            Text(
                text = group, modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    fontSize = 20.sp, fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
    BackButtonHandler {
        goBack(activity, showcaseBrowserScreenMetadata)
    }
}

internal fun goBack(
    activity: ComponentActivity,
    showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>
) {
    val isSearchActive = showcaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> {
            showcaseBrowserScreenMetadata.value = showcaseBrowserScreenMetadata.value.copy(
                isSearchActive = false,
                searchQuery = null
            )
        }
        else -> {
            activity.finish()
        }
    }
}

internal fun getFilteredSearchList(
    list: List<String>,
    showcaseBrowserScreenMetadata: MutableState<ShowcaseBrowserScreenMetadata>
) =
    when (showcaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showcaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.toLowerCase().contains(showcaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
