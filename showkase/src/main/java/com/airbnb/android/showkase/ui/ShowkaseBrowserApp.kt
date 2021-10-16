package com.airbnb.android.showkase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.android.showkase.R
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCategory
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
        content = {
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
    val currentRoute = navBackStackEntry?.destination?.route
    Row(
        Modifier.fillMaxWidth()
            .graphicsLayer(shadowElevation = 4f)
            .padding(padding2x)
            .height(64.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShowkaseAppBarTitle(
            showkaseBrowserScreenMetadata.value.isSearchActive,
            showkaseBrowserScreenMetadata.value.currentGroup,
            showkaseBrowserScreenMetadata.value.currentComponentName,
            showkaseBrowserScreenMetadata.value.currentComponentStyleName,
            currentRoute,
            showkaseBrowserScreenMetadata.value.searchQuery,
            {
                showkaseBrowserScreenMetadata.value =
                    showkaseBrowserScreenMetadata.value.copy(searchQuery = it)
            },
            Modifier.fillMaxWidth(0.75f)
        )
        ShowkaseAppBarActions(
            showkaseBrowserScreenMetadata,
            currentRoute,
            Modifier.fillMaxWidth(0.25f)
        )
    }

    /**
     * Commented out due to TopAppBar not working properly in beta-01 for this use case. Seems to be
     * related to use to Surface inside TopAppBar and a TextField. Creating my own implementation
     * for now. Will uncomment if this issue gets fixed.
     */
//    TopAppBar(
//        title = {
//            ShowkaseAppBarTitle(
//                showkaseBrowserScreenMetadata.value.isSearchActive,
//                showkaseBrowserScreenMetadata.value.currentGroup,
//                showkaseBrowserScreenMetadata.value.currentComponentName,
//                currentRoute,
//                showkaseBrowserScreenMetadata.value.searchQuery
//            ) {
//                showkaseBrowserScreenMetadata.value = 
//                    showkaseBrowserScreenMetadata.value.copy(searchQuery = it)
//            }
//        },
//        actions = {
//            ShowkaseAppBarActions(showkaseBrowserScreenMetadata, currentRoute)
//        },
//        backgroundColor = Color.White
//    )
}

@Suppress("LongParameterList")
@Composable
private fun ShowkaseAppBarTitle(
    isSearchActive: Boolean,
    currentGroup: String?,
    currentComponentName: String?,
    currentComponentStyleName: String?,
    currentRoute: String?,
    searchQuery: String?,
    searchQueryValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    when {
        isSearchActive -> {
            ShowkaseSearchField(searchQuery, searchQueryValueChange)
        }
        currentRoute == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name -> {
            ToolbarTitle(context.getString(R.string.showkase_title), modifier)
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_GROUPS.name -> {
            ToolbarTitle(context.getString(R.string.components_category), modifier)
        }
        currentRoute == ShowkaseCurrentScreen.COLOR_GROUPS.name -> {
            ToolbarTitle(context.getString(R.string.colors_category), modifier)
        }
        currentRoute == ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name -> {
            ToolbarTitle(context.getString(R.string.typography_category), modifier)
        }
        currentRoute.insideGroup() -> {
            ToolbarTitle(currentGroup ?: "currentGroup", modifier)
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_STYLES.name -> {
            ToolbarTitle(currentComponentName.orEmpty(), modifier)
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_DETAIL.name -> {
            val styleName = currentComponentStyleName?.let { "[$it]" }.orEmpty()
            ToolbarTitle(
                "${currentComponentName.orEmpty()} $styleName",
                modifier
            )
        }
    }
}

@Composable
fun ToolbarTitle(
    string: String,
    modifier: Modifier
) {
    Text(
        text = string,
        modifier = modifier,
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
internal fun ShowkaseSearchField(
    searchQuery: String?,
    searchQueryValueChange: (String) -> Unit
) {
    TextField(
        value = searchQuery.orEmpty(),
        // Update value of textValue with the latest value of the text field
        onValueChange = searchQueryValueChange,
        label = {
            Text(text = LocalContext.current.getString(R.string.search_label))
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = FontFamily.Default,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier.testTag("SearchTextField").fillMaxWidth(),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
        },
        colors = TextFieldDefaults.textFieldColors()
    )
}

@Composable
private fun ShowkaseAppBarActions(
    metadata: MutableState<ShowkaseBrowserScreenMetadata>,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    when {
        metadata.value.isSearchActive -> {
        }
        currentRoute == ShowkaseCurrentScreen.COMPONENT_DETAIL.name ||
                currentRoute == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name -> {
        }
        else -> {
            IconButton(
                modifier = modifier.testTag("SearchIcon"),
                onClick = {
                    metadata.value = metadata.value.copy(isSearchActive = true)
                }
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            }
        }
    }
}

@Suppress("LongMethod")
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
                navController,
                getCategoryMetadataMap(
                    groupedComponentMap,
                    groupedColorsMap,
                    groupedTypographyMap
                )
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
        composable(ShowkaseCurrentScreen.COMPONENT_STYLES.name) {
            ShowkaseComponentStylesScreen(
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

private fun getCategoryMetadataMap(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
) = mapOf(
    ShowkaseCategory.COMPONENTS to groupedComponentMap.flatComponentCount(),
    ShowkaseCategory.COLORS to groupedColorsMap.flatCount(),
    ShowkaseCategory.TYPOGRAPHY to groupedTypographyMap.flatCount()
)

private fun Map<String, List<*>>.flatCount() = flatMap { it.value }.count()

private fun Map<String, List<ShowkaseBrowserComponent>>.flatComponentCount() = flatMap { entry ->
    // Only group name and component name is taken into account for the count to ensure that the
    // styles of the same component aren't added  in this calculation.
    entry.value.distinctBy { "${it.group}_${it.componentName}" }
}.count()

/**
 * Helper function to navigate to the passed [ShowkaseCurrentScreen]
 */
internal fun NavHostController.navigate(destinationScreen: ShowkaseCurrentScreen) =
    navigate(destinationScreen.name)
