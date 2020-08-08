package com.airbnb.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FilledTextField
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.showkase.R
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseBrowserApp(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    Scaffold(
        drawerContent = null,
        topBar = {
            ShowkaseAppBar(showkaseBrowserScreenMetadata)
        },
        bodyContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = SHOWKASE_COLOR_BACKGROUND
            ) {
                ShowkaseBodyContent(groupedComponentMap, showkaseBrowserScreenMetadata)
            }
        }
    )
}

@Composable
internal fun ShowkaseAppBar(showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    TopAppBar(
        title = {
            ShowkaseAppBarTitle(showkaseBrowserScreenMetadata)
        },
        actions = {
            ShowkaseAppBarActions(showkaseBrowserScreenMetadata)
        },
        backgroundColor = Color.White
    )
}

@Composable
private fun ShowkaseAppBarTitle(metadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    when {
        metadata.value.isSearchActive -> {
            ShowkaseSearchField(metadata)
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.GROUPS -> {
            Text(ContextAmbient.current.getString(R.string.app_name))
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.GROUP_COMPONENTS -> {
            Text(metadata.value.currentGroup.orEmpty())
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.COMPONENT_DETAIL -> {
            Text(metadata.value.currentComponent.orEmpty())
        }
    }
}

@Composable
internal fun ShowkaseSearchField(metadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    FilledTextField(
        value = metadata.value.searchQuery.orEmpty(),
        // Update value of textValue with the latest value of the text field
        onValueChange = {
            metadata.value = metadata.value.copy(searchQuery = it)
        },
        label = {
            Text(text = ContextAmbient.current.getString(R.string.search_label))
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = FontFamily.Default,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(asset = Icons.Filled.Search)
        },
        backgroundColor = Color.White
    )
}

@Composable
private fun ShowkaseAppBarActions(metadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    when {
        metadata.value.isSearchActive -> {
        }
        else -> {
            IconButton(onClick = {
                metadata.value = metadata.value.copy(isSearchActive = true)
            }) {
                Icon(asset = Icons.Filled.Search)
            }
        }
    }
}

@Composable
internal fun ShowkaseBodyContent(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    when (showkaseBrowserScreenMetadata.value.currentScreen) {
        ShowkaseCurrentScreen.GROUPS -> {
            ShowkaseAllGroupsScreen(
                groupedComponentMap, 
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.GROUP_COMPONENTS -> {
            ShowkaseGroupComponentsScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.COMPONENT_DETAIL -> {
            ShowkaseComponentDetailScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata
            )
        }
    }
}
