package com.airbnb.android.showkase.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)
    
    LazyColumnFor(items = ShowkaseCategory.values().toList()) { category ->
        val defaultlLocale = Locale.getDefault()
        SimpleTextCard(
            text = category.name.toLowerCase(defaultlLocale).capitalize(defaultlLocale),
            onClick = {
                showkaseBrowserScreenMetadata.update {
                    copy(
                        currentScreen = when(category) {
                            ShowkaseCategory.COMPONENTS -> ShowkaseCurrentScreen.COMPONENT_GROUPS
                            ShowkaseCategory.COLORS -> ShowkaseCurrentScreen.COLOR_GROUPS
                            ShowkaseCategory.TYPOGRAPHY -> ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS
                        },
                        currentGroup = null,
                        isSearchActive = false,
                        searchQuery = null
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
    activity: ComponentActivity,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> activity.finish()
    }
}

internal fun goBackToCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> showkaseBrowserScreenMetadata.update {
            copy(
                currentScreen = ShowkaseCurrentScreen.SHOWKASE_CATEGORIES,
                currentComponent = null,
                isSearchActive = false,
                searchQuery = null,
                currentGroup = null
            )
        }
    }
}
