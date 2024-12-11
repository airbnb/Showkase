package com.airbnb.android.showkase.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import java.util.Locale

@Composable
internal fun ShowkaseTypographyInAGroupScreen(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val activity = LocalContext.current as AppCompatActivity
    val groupTypographyList =
        groupedTypographyMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.sortedBy { it.typographyName } ?: return
    val filteredList =
        getFilteredSearchList(groupTypographyList, showkaseBrowserScreenMetadata.value)
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .testTag("TypographyInAGroupList")
    ) {
        items(
            items = filteredList,
            itemContent = { groupTypographyMetadata ->
                Text(
                    text = groupTypographyMetadata.typographyName.replaceFirstChar {
                        it.titlecase(Locale.getDefault())
                    },
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(padding4x),
                    style = groupTypographyMetadata.textStyle
                )
                Divider()
            }
        )
    }
    BackButtonHandler {
        goBackFromTypographyInAGroupScreen(
            showkaseBrowserScreenMetadata,
            groupedTypographyMap.size == 1, navController
        ) { activity.finish() }
    }
}

private fun goBackFromTypographyInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    noGroups: Boolean,
    navController: NavHostController,
    onBackPressOnRoot: () -> Unit
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        noGroups -> {
            showkaseBrowserScreenMetadata.clear()
            if (navController.currentDestination?.id == navController.graph.startDestinationId) {
                onBackPressOnRoot()
            } else {
                navController.navigate(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES)
            }
        }
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS)
        }
    }
}

private fun getFilteredSearchList(
    list: List<ShowkaseBrowserTypography>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata
) =
    when (showkaseBrowserScreenMetadata.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.searchQuery.isNullOrBlank() -> {
            list.filter {
                matchSearchQuery(
                    showkaseBrowserScreenMetadata.searchQuery!!,
                    it.typographyName
                )
            }
        }
        else -> list
    }
