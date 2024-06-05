package com.airbnb.android.showkase.ui

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseGroupsScreen(
    groupedTypographyMap: Map<String, List<*>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController,
    onClick: () -> Unit
) {
    val filteredMap = getFilteredSearchList(
        groupedTypographyMap.toSortedMap(),
        showkaseBrowserScreenMetadata.isSearchActive,
        showkaseBrowserScreenMetadata.searchQuery
    )

    LazyColumn {
        items(
            items = filteredMap.entries.toList(),
            itemContent = { (group, list) ->
                val size = getNumOfUIElements(list)
                SimpleTextCard(
                    text = "$group ($size)",
                    onClick = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(
                                currentGroup = group,
                                isSearchActive = false,
                                searchQuery = null
                            )
                        )
                        onClick()
                    }
                )
            }
        )
    }
    val activity = LocalContext.current as AppCompatActivity
    BackButtonHandler {
        goBackToCategoriesScreen(showkaseBrowserScreenMetadata, onUpdateShowkaseBrowserScreenMetadata, navController) {
            activity.finish()
        }
    }
}

internal fun getNumOfUIElements(list: List<*>): Int {
    val isComponentList = list.filterIsInstance(ShowkaseBrowserComponent::class.java)
    return when {
        isComponentList.isNotEmpty() -> isComponentList.distinctBy { it.componentName }.size
        else -> list.size
    }
}

internal fun <T> getFilteredSearchList(
    map: Map<String, List<T>>,
    isSearchActive: Boolean,
    searchQuery: String?,
) =
    when (isSearchActive) {
        false -> map
        !searchQuery.isNullOrBlank() -> {
            map.filter {
                matchSearchQuery(
                    searchQuery!!,
                    it.key
                )
            }
        }
        else -> map
    }

@Composable
internal fun ShowkaseComponentGroupsScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController
) {
    ShowkaseGroupsScreen(
        groupedComponentMap,
        showkaseBrowserScreenMetadata,
        onUpdateShowkaseBrowserScreenMetadata,
        navController
    ) {
        navController.navigate(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP)
    }
}

@Composable
internal fun ShowkaseColorGroupsScreen(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController
) {
    ShowkaseGroupsScreen(
        groupedColorsMap,
        showkaseBrowserScreenMetadata,
        onUpdateShowkaseBrowserScreenMetadata,
        navController
    ) {
        navController.navigate(ShowkaseCurrentScreen.COLORS_IN_A_GROUP)
    }
}

@Composable
internal fun ShowkaseTypographyGroupsScreen(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController
) {
    if (groupedTypographyMap.size == 1) {
        onUpdateShowkaseBrowserScreenMetadata(
            showkaseBrowserScreenMetadata.copy(
                currentGroup = groupedTypographyMap.entries.first().key,
            )
        )

        ShowkaseTypographyInAGroupScreen(
            groupedTypographyMap = groupedTypographyMap,
            showkaseBrowserScreenMetadata = showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata = onUpdateShowkaseBrowserScreenMetadata,
            navController = navController
        )
    } else {
        ShowkaseGroupsScreen(
            groupedTypographyMap,
            showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata,
            navController
        ) {
            navController.navigate(ShowkaseCurrentScreen.TYPOGRAPHY_IN_A_GROUP)
        }
    }
}
