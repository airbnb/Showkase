package com.airbnb.android.showkase.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch

@Suppress("LongMethod")
@Composable
internal fun ShowkaseColorsInAGroupScreen(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val groupColorsList = remember(groupedColorsMap, showkaseBrowserScreenMetadata.currentGroup) {
        groupedColorsMap[showkaseBrowserScreenMetadata.currentGroup]
            ?.sortedBy { it.colorName }
    } ?: return
    val filteredList = remember(groupColorsList, showkaseBrowserScreenMetadata.isSearchActive, showkaseBrowserScreenMetadata.searchQuery) {
        getFilteredSearchList(
            groupColorsList,
            isSearchActive = showkaseBrowserScreenMetadata.isSearchActive,
            searchQuery = showkaseBrowserScreenMetadata.searchQuery,
        )
    }
    LazyColumn(
        modifier = Modifier.testTag("ColorsInAGroupList")
    ) {
        items(items = filteredList,
            itemContent = { groupColorMetadata ->
                Card(
                    modifier = Modifier.padding(
                        start = padding4x,
                        end = padding4x,
                        top = padding2x,
                        bottom = padding2x
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(padding4x),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = groupColorMetadata.colorName,
                            modifier = Modifier
                                .padding(start = padding4x, end = padding4x)
                                .weight(1f),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = padding4x, end = padding4x)
                                .size(75.dp)
                                .shadow(elevation = 5.dp)
                                .background(color = groupColorMetadata.color)
                        ) {}
                    }
                }
            }
        )
    }
    BackHandler {
        goBackFromColorsInAGroupScreen(
            showkaseBrowserScreenMetadata,
            onUpdateShowkaseBrowserScreenMetadata,
            navigateTo
        )
    }
}

private fun goBackFromColorsInAGroupScreen(
    showkaseBrowserScreenMetadata: ShowkaseBrowserScreenMetadata,
    onUpdateShowkaseBrowserScreenMetadata: (ShowkaseBrowserScreenMetadata) -> Unit,
    navigateTo: (ShowkaseCurrentScreen) -> Unit,
) {
    val isSearchActive = showkaseBrowserScreenMetadata.isSearchActive
    when {
        isSearchActive ->
            onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clearActiveSearch())
        else -> {
            onUpdateShowkaseBrowserScreenMetadata(showkaseBrowserScreenMetadata.clear())
            navigateTo(ShowkaseCurrentScreen.COLOR_GROUPS)
        }
    }
}

internal fun getFilteredSearchList(
    list: List<ShowkaseBrowserColor>,
    isSearchActive: Boolean,
    searchQuery: String?,
) = when (isSearchActive) {
    false -> list
    !searchQuery.isNullOrBlank() -> {
        list.filter {
            matchSearchQuery(
                searchQuery!!,
                it.colorName
            )
        }
    }

    else -> list
}
