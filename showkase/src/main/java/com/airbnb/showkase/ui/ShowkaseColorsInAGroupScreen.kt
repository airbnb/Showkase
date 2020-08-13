package com.airbnb.showkase.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.showkase.models.ShowkaseBrowserColor
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseCurrentScreen

@Composable
internal fun ShowkaseColorsInAGroupScreen(
    groupedColorsMap: Map<String, List<ShowkaseBrowserColor>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val groupColorsList =
        groupedColorsMap[showkaseBrowserScreenMetadata.value.currentGroup]
            ?.sortedBy { it.colorName } ?: return
    val filteredList =
        getFilteredSearchList(groupColorsList, showkaseBrowserScreenMetadata)
    LazyColumnFor(
        items = filteredList,
        itemContent = { groupColorMetadata ->
            Card(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillParentMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalGravity = Alignment.CenterVertically
                ) {
                    Text(
                        text = groupColorMetadata.colorName,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Box(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                            .size(75.dp)
                            .drawShadow(elevation = 5.dp),
                        backgroundColor = groupColorMetadata.color,
                    )
                }
            }
        }
    )
    BackButtonHandler {
        goBackFromColorsInAGroupScreen(showkaseBrowserScreenMetadata)
    }
}

private fun  goBackFromColorsInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> {
            showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
                isSearchActive = false,
                searchQuery = null
            )
        }
        else -> {
            showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
                currentScreen = ShowkaseCurrentScreen.COLOR_GROUPS,
                currentComponent = null,
                isSearchActive = false,
                searchQuery = null,
                currentGroup = null
            )
        }
    }
}

internal fun getFilteredSearchList(
    list: List<ShowkaseBrowserColor>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) =
    when (showkaseBrowserScreenMetadata.value.isSearchActive) {
        false -> list
        !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
            list.filter {
                it.colorName.toLowerCase()
                    .contains(showkaseBrowserScreenMetadata.value.searchQuery!!.toLowerCase())
            }
        }
        else -> list
    }
