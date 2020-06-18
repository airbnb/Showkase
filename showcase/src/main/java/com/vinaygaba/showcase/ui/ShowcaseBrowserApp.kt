package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextFieldValue
import androidx.ui.graphics.Color
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
import com.vinaygaba.showcase.R
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.models.ShowcaseCurrentScreen

@Composable
internal fun ShowcaseBrowserApp(groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>) {
    Scaffold(
        drawerContent = null,
        topAppBar = {
            ShowcaseAppBar()
        },
        bodyContent = {
            ShowcaseBodyContent(groupedComponentMap)
        }
    )
}

@Composable
internal fun ShowcaseAppBar() {
    val currentScreen = ShowcaseBrowserScreenMetadata.currentScreen
    TopAppBar(
        title = {
            ShowcaseAppBarTitle(currentScreen)
        },
        actions = {
            ShowcaseAppBarActions()
        }
    )
}

@Composable
private fun ShowcaseAppBarTitle(currentScreen: ShowcaseCurrentScreen) {
    when {
        ShowcaseBrowserScreenMetadata.isSearchActive -> {
            ShowcaseSearchField()
        }
        currentScreen == ShowcaseCurrentScreen.GROUPS -> {
            Text(ContextAmbient.current.getString(R.string.app_name))
        }
        currentScreen == ShowcaseCurrentScreen.GROUP_COMPONENTS -> {
            Text(ShowcaseBrowserScreenMetadata.currentGroup.orEmpty())
        }
        currentScreen == ShowcaseCurrentScreen.COMPONENT_DETAIL -> {
            Text(ShowcaseBrowserScreenMetadata.currentComponent.orEmpty())
        }
    }
}

@Composable
internal fun ShowcaseSearchField() {
    // Needed to create another field to due a crash I was seeing when I 
    // directly used the search query field inside the 
    // ShowcaseBrowserScreenMetadata model
    // java.lang.IllegalStateException: Expected a group start
    var searchQuery by state { TextFieldValue("") }
    FilledTextField(
        value = searchQuery,
        // Update value of textValue with the latest value of the text field
        onValueChange = {
            searchQuery = it
            ShowcaseBrowserScreenMetadata.searchQuery = it.text
        },
        label = {
            Text(text = ContextAmbient.current.getString(R.string.search_label))
        },
        textStyle = TextStyle(
            color = Color.White,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(asset = Icons.Filled.Search)
        }
    )
}

@Composable
private fun ShowcaseAppBarActions() {
    when {
        ShowcaseBrowserScreenMetadata.isSearchActive -> {
        }
        else -> {
            IconButton(onClick = {
                ShowcaseBrowserScreenMetadata.isSearchActive = true
            }) {
                Icon(asset = Icons.Filled.Search)
            }
        }
    }
}

@Composable
internal fun ShowcaseBodyContent(groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>) {
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
