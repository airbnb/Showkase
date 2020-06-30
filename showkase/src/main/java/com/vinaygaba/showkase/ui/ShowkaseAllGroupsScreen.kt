package com.vinaygaba.showkase.ui

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
import com.vinaygaba.showkase.models.ShowkaseBrowserScreenMetadata
import com.vinaygaba.showkase.models.ShowkaseBrowserComponent
import com.vinaygaba.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseAllGroupsScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val filteredList = getFilteredSearchList(groupedComponentMap.keys.toList(), 
        showkaseBrowserScreenMetadata)
    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

    AdapterList(data = filteredList) { group ->
        Card(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp) + Modifier.clickable(
            onClick = {
                showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
                    currentScreen = ShowkaseCurrentScreen.GROUP_COMPONENTS,
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
        goBack(activity, showkaseBrowserScreenMetadata)
    }
}

internal fun goBack(
    activity: ComponentActivity,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> {
            showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
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
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) =
    when (showkaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.toLowerCase().contains(showkaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
