package com.airbnb.showkase.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.showkase.models.ShowkaseBrowserColor
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata

@Composable
fun ShowkaseColorsScreen(colorList: List<ShowkaseBrowserColor>) {
    LazyColumnFor(items = colorList) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                modifier = Modifier.background(it.color)
                    .preferredHeight(200.dp)
                    .fillParentMaxWidth()
            )
        }
    }
}

private fun getFilteredSearchList(
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
