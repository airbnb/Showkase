package com.airbnb.android.showkase.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.*

@Composable
internal fun ShowkaseComponentStylesScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val componentStylesList =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.filter { it.componentName == showkaseBrowserScreenMetadata.value.currentComponentName  }
            ?.sortedBy { it.styleName } ?: return
    val filteredList =
        getFilteredSearchList(componentStylesList, showkaseBrowserScreenMetadata)
    LazyColumn {
        items(
            items = filteredList,
            itemContent = { groupComponent ->
                ComponentCardTitle(
                    "${groupComponent.componentName} [${groupComponent.styleName}]"
                )
                ComponentCard(
                    metadata = groupComponent,
                    onClick = {
                        showkaseBrowserScreenMetadata.update {
                            copy(
                                currentComponentKey = groupComponent.componentKey,
                                currentComponentName = groupComponent.componentName,
                                currentComponentStyleName = groupComponent.styleName,
                                isSearchActive = false
                            )
                        }
                        navController.navigate(ShowkaseCurrentScreen.COMPONENT_DETAIL)
                    }
                )
            }
        )
    }
    BackButtonHandler {
        back(showkaseBrowserScreenMetadata, navController)
    }
}

private fun back(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> {
            showkaseBrowserScreenMetadata.update {
                copy(
                    currentComponentStyleName = null,
                    isSearchActive = false,
                    searchQuery = null
                )
            }
            navController.navigate(ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP)
        }
    }

}

private fun getFilteredSearchList(
    list: List<ShowkaseBrowserComponent>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) =
    when (showkaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.componentName.lowercase(Locale.getDefault())
                    .contains(
                        showkaseBrowserScreenMetadata.value.searchQuery!!
                            .lowercase(Locale.getDefault())
                    ) || it.styleName.lowercase(Locale.getDefault())
                    .contains(
                        showkaseBrowserScreenMetadata.value.searchQuery!!
                            .lowercase(Locale.getDefault())
                    )
            }
        }
        else -> list
    }