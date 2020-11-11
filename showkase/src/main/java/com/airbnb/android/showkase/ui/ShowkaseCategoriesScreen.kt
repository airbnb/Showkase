package com.airbnb.android.showkase.ui

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
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
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
    navController: NavHostController
) {
    
    when {
        showkaseBrowserScreenMetadata.value.isSearchActive -> {
            showkaseBrowserScreenMetadata.clearActiveSearch()
        }
        else ->  {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES)
        }
    }
}
