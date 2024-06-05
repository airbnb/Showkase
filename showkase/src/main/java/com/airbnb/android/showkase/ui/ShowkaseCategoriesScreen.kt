package com.airbnb.android.showkase.ui

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController,
    categoryMetadataMap: Map<ShowkaseCategory, Int>
) {
    val activity = LocalContext.current as AppCompatActivity
    LazyColumn {
        items(
            items = categoryMetadataMap.entries.toList(),
            itemContent = { (category, categorySize) ->
                val defaultLocale = Locale.getDefault()
                val title = category.name
                    .lowercase(defaultLocale)
                    .replaceFirstChar { it.titlecase(defaultLocale) }

                SimpleTextCard(
                    text = "$title ($categorySize)",
                    onClick = {
                        onUpdateShowkaseBrowserScreenMetadata(
                            showkaseBrowserScreenMetadata.copy(
                                currentGroup = null,
                                isSearchActive = false,
                                searchQuery = null
                            )
                        )
                        when (category) {
                            ShowkaseCategory.COMPONENTS -> navController.navigate(
                                ShowkaseCurrentScreen.COMPONENT_GROUPS
                            )
                            ShowkaseCategory.COLORS -> navController.navigate(
                                ShowkaseCurrentScreen.COLOR_GROUPS
                            )
                            ShowkaseCategory.TYPOGRAPHY -> navController.navigate(
                                ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS
                            )
                        }
                    }
                )
            }
        )
    }
    BackButtonHandler {
        goBackFromCategoriesScreen(activity, showkaseBrowserScreenMetadata, onUpdateShowkaseBrowserScreenMetadata)
    }
}

private fun goBackFromCategoriesScreen(
    activity: AppCompatActivity,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit
) {
    val isSearchActive = showkaseBrowserScreenMetadata.isSearchActive
    when {
        isSearchActive -> onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clearActiveSearch())
        else -> activity.finish()
    }
}

internal fun goBackToCategoriesScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navController: NavHostController,
    onBackPressOnRoot: () -> Unit
) {
    when {
        showkaseBrowserScreenMetadata.isSearchActive -> {
            onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clearActiveSearch())
        }
        navController.currentDestination?.id == navController.graph.startDestinationId -> {
            onBackPressOnRoot()
        }
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES)
        }
    }
}
