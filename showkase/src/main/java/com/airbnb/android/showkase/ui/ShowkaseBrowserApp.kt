package com.airbnb.android.showkase.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()
    Scaffold(
        drawerContent = null,
        topBar = {
            ShowkaseAppBar(navController, showkaseBrowserScreenMetadata)
        },
        bodyContent = {
            Column(
                modifier = Modifier.fillMaxSize().background(color = SHOWKASE_COLOR_BACKGROUND),
            ) {
                ShowkaseBodyContent(
                    navController,
                    groupedComponentMap, 
                    groupedColorsMap, 
                    groupedTypographyMap,
                    showkaseBrowserScreenMetadata
                )
            }
        }
    )
}

@Composable
internal fun ShowkaseAppBar(
    navController: NavHostController,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
    TopAppBar(
        title = {
            ShowkaseAppBarTitle(showkaseBrowserScreenMetadata, currentRoute)
        },
        actions = {
            ShowkaseAppBarActions(showkaseBrowserScreenMetadata, currentRoute)
        },
        backgroundColor = Color.White
    )
}

@Composable
private fun ShowkaseAppBarTitle(
    metadata: MutableState<ShowkaseBrowserScreenMetadata>,
    currentRoute: String?
) {
    when {
        metadata.value.isSearchActive -> {
            ShowkaseSearchField(metadata)
        }
        currentRoute == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name -> {
            Text(ContextAmbient.current.getString(R.string.app_name))
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_GROUPS.name -> {
            Text(ContextAmbient.current.getString(R.string.components_category))
        }
        currentRoute == ShowkaseCurrentScreen.COLOR_GROUPS.name -> {
            Text(ContextAmbient.current.getString(R.string.colors_category))
        }
        currentRoute == ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name -> {
            Text(ContextAmbient.current.getString(R.string.typography_category))
        }
        currentRoute.insideGroup() -> {
            Text(metadata.value.currentGroup.orEmpty())
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_DETAIL.name -> {
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
        modifier = Modifier.testTag("SearchTextField").fillMaxWidth(),
        leadingIcon = {
            Icon(asset = Icons.Filled.Search)
        },
        backgroundColor = Color.White
    )
}

@Composable
private fun ShowkaseAppBarActions(
    metadata: MutableState<ShowkaseBrowserScreenMetadata>,
    currentRoute: String?
) {
    when {
        metadata.value.isSearchActive -> {
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_DETAIL.name ||
                currentRoute == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name -> {
        }
        else -> {
            IconButton(
                modifier = Modifier.testTag("SearchIcon"),
                onClick = {
                    metadata.value = metadata.value.copy(isSearchActive = true)
                }
            ) {
                Icon(asset = Icons.Filled.Search)
            }
        }
    }
}

@Composable
internal fun ShowkaseBodyContent(
    navController: NavHostController,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    NavHost(
        navController = navController,
        startDestination = ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name
    ) {
        composable(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name) {
            ShowkaseCategoriesScreen(
                showkaseBrowserScreenMetadata, 
                navController
            )
        }
        composable(ShowkaseCurrentScreen.COMPONENT_GROUPS.name) {
            ShowkaseComponentGroupsScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
        composable(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP.name) {
            ShowkaseComponentsInAGroupScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata, 
                navController
            )
        }
        composable(ShowkaseCurrentScreen.COMPONENT_DETAIL.name) {
            ShowkaseComponentDetailScreen(
                groupedComponentMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
        composable(ShowkaseCurrentScreen.COLOR_GROUPS.name) {
            ShowkaseColorGroupsScreen(
                groupedColorsMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
        composable(ShowkaseCurrentScreen.COLORS_IN_A_GROUP.name) {
            ShowkaseColorsInAGroupScreen(
                groupedColorsMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
        composable(ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name) {
            ShowkaseTypographyGroupsScreen(
                groupedTypographyMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
        composable(ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP.name) {
            ShowkaseTypographyInAGroupScreen(
                groupedTypographyMap,
                showkaseBrowserScreenMetadata,
                navController
            )
        }
    }
}

/**
 * Helper function to navigate to the passed [ShowkaseCurrentScreen]
 */
internal fun NavHostController.navigate(destinationScreen: ShowkaseCurrentScreen) = 
    navigate(destinationScreen.name)
