package com.airbnb.android.showkase.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.R
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.insideGroup

@Composable
internal fun ShowkaseBrowserApp(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
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
                ShowkaseBodyContent(groupedComponentMap, groupedColorsMap, groupedTypographyMap,
                    showkaseBrowserScreenMetadata)
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
        metadata.value.currentScreen == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES -> {
            Text(ContextAmbient.current.getString(R.string.app_name))
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.COMPONENT_GROUPS -> {
            Text(ContextAmbient.current.getString(R.string.components_category))
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.COLOR_GROUPS -> {
            Text(ContextAmbient.current.getString(R.string.colors_category))
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS -> {
            Text(ContextAmbient.current.getString(R.string.typography_category))
        }
        metadata.value.currentScreen.insideGroup() -> {
            Text(metadata.value.currentGroup.orEmpty())
        }
        metadata.value.currentScreen == ShowkaseCurrentScreen.COMPONENT_DETAIL -> {
            Text(metadata.value.currentComponent.orEmpty())
        }
    }
}

@Composable
internal fun ShowkaseSearchField(metadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    TextField(
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
        metadata.value.isSearchActive -> { }
        metadata.value.currentScreen == ShowkaseCurrentScreen.COMPONENT_DETAIL ||  
        metadata.value.currentScreen == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES -> { }
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
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    when (showkaseBrowserScreenMetadata.value.currentScreen) {
        ShowkaseCurrentScreen.SHOWKASE_CATEGORIES -> {
            ShowkaseCategoriesScreen(showkaseBrowserScreenMetadata)
        }
        ShowkaseCurrentScreen.COMPONENT_GROUPS -> {
            ShowkaseComponentGroupsScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP -> {
            ShowkaseComponentsInAGroupScreen(
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
        ShowkaseCurrentScreen.COLOR_GROUPS -> {
            ShowkaseColorGroupsScreen(
                groupedColorsMap, 
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.COLORS_IN_A_GROUP -> {
            ShowkaseColorsInAGroupScreen(
                groupedColorsMap,
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS -> {
            ShowkaseTypographyGroupsScreen(
                groupedTypographyMap,
                showkaseBrowserScreenMetadata
            )
        }
        ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP -> {
            ShowkaseTypographyInAGroupScreen(
                groupedTypographyMap,
                showkaseBrowserScreenMetadata
            )
        }
    }
}
