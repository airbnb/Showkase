package com.airbnb.android.showkase.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
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
import com.airbnb.android.showkase.ui.SemanticsUtils.lineCountVal

@Suppress("LongMethod")
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun ShowkaseBrowserApp(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
) {
    val lightModeConfiguration = Configuration(LocalConfiguration.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_NO
    }
    CompositionLocalProvider(
        LocalConfiguration provides lightModeConfiguration,
        LocalInspectionMode provides true,
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            drawerContent = null,
            topBar = {
                ShowkaseAppBar(
                    currentRoute = currentRoute,
                    showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
                    onSearchQueryChanged = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(searchQuery = it)
                        )
                    },
                    onClearSearch = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(searchQuery = "")
                        )
                    },
                    onActivateSearch = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(isSearchActive = true)
                        )
                    },
                    onCloseSearch = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(isSearchActive = false)
                        )
                    },

                    )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = SHOWKASE_COLOR_BACKGROUND),
                ) {
                    ShowkaseBodyContent(
                        navController,
                        groupedComponentMap,
                        groupedColorsMap,
                        groupedTypographyMap,
                        showkaseBrowserScreenMetadata,
                        onUpdateShowkaseBrowserScreenMetadata,
                        navigateTo = {
                            navController.navigate(it.name)
                        }
                    )
                }
            }
        )
    }
}

@Composable
internal fun ShowkaseAppBar(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    currentRoute: String?,
    onSearchQueryChanged: (String) -> Unit,
    onCloseSearch: () -> Unit,
    onActivateSearch: () -> Unit,
    onClearSearch: () -> Unit,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .graphicsLayer(shadowElevation = 4f)
            .padding(padding2x),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShowkaseAppBarTitle(
            showkaseBrowserScreenMetadata.isSearchActive,
            showkaseBrowserScreenMetadata.currentGroup,
            showkaseBrowserScreenMetadata.currentComponentName,
            showkaseBrowserScreenMetadata.currentComponentStyleName,
            currentRoute,
            showkaseBrowserScreenMetadata.searchQuery,
            {
                onSearchQueryChanged(it)
            },
            Modifier.fillMaxWidth(0.75f),
            onCloseSearchFieldClick = {
                onCloseSearch()
            },
            onClearSearchField = {
                onClearSearch()
            }
        )
        ShowkaseAppBarActions(
            isActive = showkaseBrowserScreenMetadata.isSearchActive,
            onActionClicked = {
                onActivateSearch()
            },
            currentRoute = currentRoute,
            modifier = Modifier.fillMaxWidth(0.25f)
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
    modifier: Modifier = Modifier,
    onCloseSearchFieldClick: () -> Unit,
    onClearSearchField: () -> Unit,
) {

    AnimatedVisibility(
        visible = isSearchActive,
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        ShowkaseSearchField(
            searchQuery = searchQuery,
            searchQueryValueChange = searchQueryValueChange,
            onCloseSearchFieldClick = onCloseSearchFieldClick,
            onClearSearchField = onClearSearchField,
        )
    }
    AnimatedVisibility(
        visible = !isSearchActive,
        enter = slideInHorizontally() + expandIn()
    ) {
        AppBarTitle(
            currentRoute = currentRoute,
            modifier = modifier,
            currentGroup = currentGroup,
            currentComponentName = currentComponentName,
            currentComponentStyleName = currentComponentStyleName
        )
    }
}

@Composable
private fun AppBarTitle(
    modifier: Modifier,
    currentRoute: String?,
    currentGroup: String?,
    currentComponentName: String?,
    currentComponentStyleName: String?
) {
    val context = LocalContext.current

    when {
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
    val lineCount = remember {
        mutableStateOf(0)
    }

    Text(
        text = string,
        modifier = modifier then Modifier
            .padding(vertical = verticalToolbarPadding)
            .semantics {
                lineCountVal = lineCount.value
            },
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        ),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = {
            lineCount.value = it.lineCount
        }
    )
}

@Composable
internal fun ShowkaseSearchField(
    searchQuery: String?,
    searchQueryValueChange: (String) -> Unit,
    onCloseSearchFieldClick: () -> Unit,
    onClearSearchField: () -> Unit,
) {
    var localSearchQuery by remember { mutableStateOf(searchQuery.orEmpty()) }
    
    LaunchedEffect(localSearchQuery) {
        delay(300) // Debounce delay
        searchQueryValueChange(localSearchQuery)
    }
    
    LaunchedEffect(searchQuery) {
        if (searchQuery != localSearchQuery) {
            localSearchQuery = searchQuery.orEmpty()
        }
    }
    
    TextField(
        value = localSearchQuery,
        // Update value of textValue with the latest value of the text field
        onValueChange = { localSearchQuery = it },
        label = {
            Text(text = LocalContext.current.getString(R.string.search_label))
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = FontFamily.Default,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier
            .testTag("SearchTextField")
            .fillMaxWidth(),
        leadingIcon = {
            IconButton(
                onClick = onCloseSearchFieldClick,
                modifier = Modifier.testTag("close_search_bar_tag")
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            }
        },
        colors = TextFieldDefaults.textFieldColors(),
        trailingIcon = {
            IconButton(
                onClick = { 
                    localSearchQuery = ""
                    onClearSearchField() 
                },
                modifier = Modifier.testTag("clear_search_field"),
                enabled = localSearchQuery.isNotEmpty()
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear Search Field")
            }
        }
    )
}

@Composable
private fun ShowkaseAppBarActions(
    isActive: Boolean,
    onActionClicked: () -> Unit,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    when {
        isActive -> {
        }

        currentRoute == ShowkaseCurrentScreen.COMPONENT_DETAIL.name ||
                currentRoute == ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name -> {
        }

        else -> {
            IconButton(
                modifier = modifier.testTag("SearchIcon"),
                onClick = {
                    onActionClicked()
                }
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
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
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val startDestination = startDestination(
        groupedColorsMap,
        groupedTypographyMap,
        groupedComponentMap
    )
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            navGraph(
                showkaseBrowserScreenMetadata,
                onUpdateShowkaseBrowserScreenMetadata,
                groupedColorsMap,
                groupedTypographyMap,
                groupedComponentMap,
                onRootScreen = navController.currentDestination?.id == navController.graph.startDestinationId,
                navigateTo = navigateTo
            )
        }
    )
}

private fun startDestination(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>
) = when {
    groupedComponentMap.isOnlyCategory(groupedColorsMap, groupedTypographyMap) ->
        ShowkaseCurrentScreen.COMPONENT_GROUPS.name

    groupedColorsMap.isOnlyCategory(groupedTypographyMap, groupedComponentMap) ->
        ShowkaseCurrentScreen.COLOR_GROUPS.name

    groupedTypographyMap.isOnlyCategory(groupedColorsMap, groupedComponentMap) ->
        ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name

    else ->
        ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name
}

private fun NavGraphBuilder.navGraph(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    onRootScreen: Boolean,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) = when {
    groupedComponentMap.isOnlyCategory(groupedColorsMap, groupedTypographyMap) ->
        componentsNavGraph(
            groupedComponentMap = groupedComponentMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )

    groupedColorsMap.isOnlyCategory(groupedTypographyMap, groupedComponentMap) ->
        colorsNavGraph(
            groupedColorsMap = groupedColorsMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )

    groupedTypographyMap.isOnlyCategory(groupedColorsMap, groupedComponentMap) ->
        typographyNavGraph(
            groupedTypographyMap = groupedTypographyMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )

    else ->
        fullNavGraph(
            groupedComponentMap = groupedComponentMap,
            groupedColorsMap = groupedColorsMap,
            groupedTypographyMap = groupedTypographyMap,
            onRootScreen = onRootScreen,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )
}

private fun Map<String, List<*>>.isOnlyCategory(
    otherCategoryMap1: Map<String, List<*>>,
    otherCategoryMap2: Map<String, List<*>>
) = this.values.isNotEmpty() && otherCategoryMap1.isEmpty() && otherCategoryMap2.isEmpty()

private fun NavGraphBuilder.componentsNavGraph(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onRootScreen: Boolean,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    composable(ShowkaseCurrentScreen.COMPONENT_GROUPS.name) {
        ShowkaseComponentGroupsScreen(
            groupedComponentMap = groupedComponentMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            navigateTo = navigateTo,
        )
    }
    composable(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP.name) {
        ShowkaseComponentsInAGroupScreen(
            groupedComponentMap = groupedComponentMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )
    }
    composable(ShowkaseCurrentScreen.COMPONENT_STYLES.name) {
        ShowkaseComponentStylesScreen(
            groupedComponentMap = groupedComponentMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,
        )
    }
    composable(ShowkaseCurrentScreen.COMPONENT_DETAIL.name) {
        ShowkaseComponentDetailScreen(
            groupedComponentMap = groupedComponentMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo,

        )
    }
}

private fun NavGraphBuilder.colorsNavGraph(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onRootScreen: Boolean,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    composable(ShowkaseCurrentScreen.COLOR_GROUPS.name) {
        ShowkaseColorGroupsScreen(
            groupedColorsMap = groupedColorsMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo
        )
    }
    composable(ShowkaseCurrentScreen.COLORS_IN_A_GROUP.name) {
        ShowkaseColorsInAGroupScreen(
            groupedColorsMap = groupedColorsMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo
        )
    }
}

private fun NavGraphBuilder.typographyNavGraph(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onRootScreen: Boolean,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    composable(ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name) {
        ShowkaseTypographyGroupsScreen(
            groupedTypographyMap = groupedTypographyMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navigateTo = navigateTo
        )
    }
    composable(ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP.name) {
        ShowkaseTypographyInAGroupScreen(
            groupedTypographyMap = groupedTypographyMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            onRootScreen = onRootScreen,
            navigateTo = navigateTo,
        )
    }
}

private fun NavGraphBuilder.fullNavGraph(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    onRootScreen: Boolean,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    composable(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name) {
        ShowkaseCategoriesScreen(
            showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata,
            getCategoryMetadataMap(
                groupedComponentMap,
                groupedColorsMap,
                groupedTypographyMap,
            ),
            onNavigateToComponentGroups = {
                navigateTo(ShowkaseCurrentScreen.COMPONENT_GROUPS)
            },
            onNavigateToColorGroups = {
                navigateTo(ShowkaseCurrentScreen.COLOR_GROUPS)
            },
            onNavigateToTypographyGroups = {
                navigateTo(ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS)
            }
        )
    }
    componentsNavGraph(
        groupedComponentMap,
        showkaseBrowserScreenMetadata,
        onRootScreen = onRootScreen,
        onUpdateShowkaseBrowserScreenMetadata,
        navigateTo
    )
    colorsNavGraph(
        groupedColorsMap,
        showkaseBrowserScreenMetadata,
        onRootScreen = onRootScreen,
        onUpdateShowkaseBrowserScreenMetadata,
        navigateTo
    )
    typographyNavGraph(
        groupedTypographyMap,
        showkaseBrowserScreenMetadata,
        onRootScreen = onRootScreen,
        onUpdateShowkaseBrowserScreenMetadata,
        navigateTo
    )
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

private val verticalToolbarPadding = 16.dp
