package com.airbnb.android.showkase.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun ShowkaseBrowserApp(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val lightModeConfiguration = Configuration(LocalConfiguration.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_NO
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val backPressedDispatcherOwner = remember {
        object : OnBackPressedDispatcherOwner {
            override val lifecycle: Lifecycle
                get() = lifecycleOwner.lifecycle
            override val onBackPressedDispatcher: OnBackPressedDispatcher
                get() = OnBackPressedDispatcher()
        }
    }
    CompositionLocalProvider(
        LocalConfiguration provides lightModeConfiguration,
        LocalInspectionMode provides true,
        // This is added to make sure that the navigation of the ShowkaseBrowser does not break
        // when one of the previews has a back press handler in the implementation of the component.
        LocalOnBackPressedDispatcherOwner provides backPressedDispatcherOwner
    ) {
        val navController = rememberNavController()
        Scaffold(
            drawerContent = null,
            topBar = {
                ShowkaseAppBar(navController, showkaseBrowserScreenMetadata)
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
                        showkaseBrowserScreenMetadata
                    )
                }
            }
        )
    }
}

@Composable
internal fun ShowkaseAppBar(
    navController: NavHostController,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Row(
        Modifier
            .fillMaxWidth()
            .graphicsLayer(shadowElevation = 4f)
            .padding(padding2x),
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
            Modifier.fillMaxWidth(0.75f),
            onCloseSearchFieldClick = {
                showkaseBrowserScreenMetadata.value =
                    showkaseBrowserScreenMetadata.value.copy(isSearchActive = false)
            },
            onClearSearchField = {
                showkaseBrowserScreenMetadata.value =
                    showkaseBrowserScreenMetadata.value.copy(searchQuery = "")
            }
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
                onClick = onClearSearchField,
                modifier = Modifier.testTag("clear_search_field"),
                enabled = !searchQuery.isNullOrEmpty()
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear Search Field")
            }
        }
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

@Composable
internal fun ShowkaseBodyContent(
    navController: NavHostController,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
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
                navController,
                showkaseBrowserScreenMetadata,
                groupedColorsMap,
                groupedTypographyMap,
                groupedComponentMap
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
    navController: NavHostController,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>
) = when {
    groupedComponentMap.isOnlyCategory(groupedColorsMap, groupedTypographyMap) ->
        componentsNavGraph(navController, groupedComponentMap, showkaseBrowserScreenMetadata)
    groupedColorsMap.isOnlyCategory(groupedTypographyMap, groupedComponentMap) ->
        colorsNavGraph(navController, groupedColorsMap, showkaseBrowserScreenMetadata)
    groupedTypographyMap.isOnlyCategory(groupedColorsMap, groupedComponentMap) ->
        typographyNavGraph(navController, groupedTypographyMap, showkaseBrowserScreenMetadata)
    else ->
        fullNavGraph(
            navController,
            groupedComponentMap,
            groupedColorsMap,
            groupedTypographyMap,
            showkaseBrowserScreenMetadata
        )
}

private fun Map<String, List<*>>.isOnlyCategory(
    otherCategoryMap1: Map<String, List<*>>,
    otherCategoryMap2: Map<String, List<*>>
) = this.values.isNotEmpty() && otherCategoryMap1.isEmpty() && otherCategoryMap2.isEmpty()

private fun NavGraphBuilder.componentsNavGraph(
    navController: NavHostController,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
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
}

private fun NavGraphBuilder.colorsNavGraph(
    navController: NavHostController,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
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
}

private fun NavGraphBuilder.typographyNavGraph(
    navController: NavHostController,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
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

private fun NavGraphBuilder.fullNavGraph(
    navController: NavHostController,
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
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
    componentsNavGraph(navController, groupedComponentMap, showkaseBrowserScreenMetadata)
    colorsNavGraph(navController, groupedColorsMap, showkaseBrowserScreenMetadata)
    typographyNavGraph(navController, groupedTypographyMap, showkaseBrowserScreenMetadata)
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
