package com.airbnb.android.showkase.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.scrollBy
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

@Composable
internal fun ShowkaseTypographyInAGroupScreen(
    groupedTypographyMap: Map<String, List<ShowkaseBrowserTypography>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val groupTypographyList =
        groupedTypographyMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.sortedBy { it.typographyName } ?: return
    val filteredList =
        getFilteredSearchList(groupTypographyList, showkaseBrowserScreenMetadata.value)
    LazyColumnFor(
        modifier = Modifier.background(Color.White)
            .fillMaxSize()
            .testTag("TypographyInAGroupList"),
        items = filteredList,
        itemContent = { groupTypographyMetadata ->
            Text(
                text = groupTypographyMetadata.typographyName.capitalize(Locale.getDefault()),
                modifier = Modifier.fillParentMaxWidth().padding(padding4x),
                style = groupTypographyMetadata.textStyle
            )
            Divider()
        }
    )
    BackButtonHandler {
        goBackFromTypographyInAGroupScreen(showkaseBrowserScreenMetadata)
    }
}

private fun goBackFromTypographyInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> showkaseBrowserScreenMetadata.update {
            copy(
                currentScreen = ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS,
                currentGroup = null,
                currentComponent = null,
                isSearchActive = false,
                searchQuery = null
            )
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
                it.typographyName.toLowerCase()
                    .contains(showkaseBrowserScreenMetadata.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
