package com.airbnb.showkase.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.FilledTextField
import androidx.ui.material.IconButton
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Search
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.sp
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
                backgroundColor = SHOWKASE_BACKGROUND_COLOR
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
    // Needed to create another field to due a crash I was seeing when I 
    // directly used the search query field inside the 
    // ShowkaseBrowserScreenMetadata model
    // java.lang.IllegalStateException: Expected a group start
    var searchQuery by state { TextFieldValue("") }
    FilledTextField(
        value = searchQuery,
        // Update value of textValue with the latest value of the text field
        onValueChange = {
            searchQuery = it
            metadata.value = metadata.value.copy(searchQuery = it.text)
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
