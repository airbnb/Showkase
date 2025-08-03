package com.airbnb.android.showkase.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import java.util.Locale

@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    categoryMetadataMap: Map<ShowkaseCategory, Int>,
    onNavigateToComponentGroups: () -> Unit,
    onNavigateToColorGroups: () -> Unit,
    onNavigateToTypographyGroups: () -> Unit,
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
                            ShowkaseCategory.COMPONENTS -> onNavigateToComponentGroups()
                            ShowkaseCategory.COLORS -> onNavigateToColorGroups()
                            ShowkaseCategory.TYPOGRAPHY -> onNavigateToTypographyGroups()
                        }
                    }
                )
            }
        )
    }
    BackHandler {
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
        else -> {
            activity.finish()
        }
    }
}

internal fun goBackToCategoriesScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    onRootScreen: Boolean,
    onBackToCategories: () -> Unit,
    onBackPressOnRoot: () -> Unit,
) {
    when {
        showkaseBrowserScreenMetadata.isSearchActive -> {
            Log.e("BackPressed", "isSearchActive")
            onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clearActiveSearch())
        }
        onRootScreen -> {
            Log.e("BackPressed", "onRootScreen")
            onBackPressOnRoot()
        }
        else -> {
            Log.e("BackPressed", "else")
            showkaseBrowserScreenMetadata.clear()
            onBackToCategories()
        }
    }
}
