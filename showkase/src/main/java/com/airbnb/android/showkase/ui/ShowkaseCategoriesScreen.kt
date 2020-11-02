package com.airbnb.android.showkase.ui

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.ContextAmbient
import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navHostController: NavHostController
) {
    val activity = ContextAmbient.current as  AppCompatActivity
    LazyColumnFor(items = ShowkaseCategory.values().toList()) { category ->
        val defaultlLocale = Locale.getDefault()
        SimpleTextCard(
            text = category.name.toLowerCase(defaultlLocale).capitalize(defaultlLocale),
            onClick = {
                showkaseBrowserScreenMetadata.update {
                    copy(
                        currentGroup = null,
                        isSearchActive = false,
                        searchQuery = null
                    )
                }
                when(category) {
                    ShowkaseCategory.COMPONENTS -> navHostController.navigate(
                        ShowkaseCurrentScreen.COMPONENT_GROUPS.name
                    )
                    ShowkaseCategory.COLORS -> navHostController.navigate(
                        ShowkaseCurrentScreen.COLOR_GROUPS.name
                    )
                    ShowkaseCategory.TYPOGRAPHY -> navHostController.navigate(
                        ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS.name
                    )
                }
            }
        )
    }
    BackButtonHandler {
        goBackFromCategoriesScreen(activity, showkaseBrowserScreenMetadata)
    }
}

private fun goBackFromCategoriesScreen(
    activity: AppCompatActivity,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> activity.finish()
    }
}

internal fun goBackToCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navHostController: NavHostController
) {
    
    when {
        showkaseBrowserScreenMetadata.value.isSearchActive -> {
            showkaseBrowserScreenMetadata.clearActiveSearch()
            Log.e("goBackToCategories clear", showkaseBrowserScreenMetadata.toString())
        }
        else ->  {
            showkaseBrowserScreenMetadata.update {
                copy(
                    currentComponent = null,
                    isSearchActive = false,
                    searchQuery = null,
                    currentGroup = null
                )
            }
            Log.e("goBackToCategories else", showkaseBrowserScreenMetadata.toString())
            navHostController.navigate(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES.name)
        }
    }
}
