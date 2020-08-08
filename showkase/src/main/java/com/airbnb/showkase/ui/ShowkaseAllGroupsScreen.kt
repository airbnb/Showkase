package com.airbnb.showkase.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseAllGroupsScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val filteredList = getFilteredSearchList(
        groupedComponentMap.keys.toList(),
        showkaseBrowserScreenMetadata
    )
    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

    LazyColumnFor(items = filteredList, itemContent = { group ->
        Card(
            modifier = Modifier.fillParentMaxWidth() +
                    Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp) +
                    Modifier.clickable(
                        onClick = {
                            showkaseBrowserScreenMetadata.value =
                                showkaseBrowserScreenMetadata.value.copy(
                                    currentScreen = ShowkaseCurrentScreen.GROUP_COMPONENTS,
                                    currentGroup = group,
                                    isSearchActive = false,
                                    searchQuery = null
                                )
                        }
                    )
        ) {
            Text(
                text = group, modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    fontSize = 20.sp, fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    })
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
                it.toLowerCase()
                    .contains(showkaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
