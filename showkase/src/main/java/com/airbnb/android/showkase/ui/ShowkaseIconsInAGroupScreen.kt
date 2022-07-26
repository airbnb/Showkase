package com.airbnb.android.showkase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.R
import com.airbnb.android.showkase.models.ShowkaseBrowserIcon
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch

@Suppress("LongMethod")
@Composable
internal fun ShowkaseIconsInAGroupScreen(
    groupedIconsMap: Map<String, List<ShowkaseBrowserIcon>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val groupIconsList =
        groupedIconsMap[showkaseBrowserScreenMetadata.value.currentGroup]?.sortedBy { it.name }
            ?: return
    val filteredList = getFilteredSearchList(groupIconsList, showkaseBrowserScreenMetadata)

    LazyColumn(
        modifier = Modifier.testTag("IconsInAGroupList")
    ) {
        items(
            items = filteredList,
            itemContent = { groupIconMetadata ->
                Card(
                    modifier = Modifier.padding(
                        start = padding4x, end = padding4x, top = padding2x,
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
                            text = groupIconMetadata.name,
                            modifier = Modifier
                                .padding(start = padding4x, end = padding4x)
                                .weight(1f),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Box(
                            modifier = Modifier.size(75.dp).shadow(elevation = 4.dp),
                        ) {

                            when {
                                groupIconMetadata.imageVector != null -> {
                                    Icon(
                                        modifier = Modifier.align(Alignment.Center),
                                        imageVector = groupIconMetadata.imageVector,
                                        contentDescription = groupIconMetadata.name,
                                    )
                                }
                                groupIconMetadata.drawableRes != null -> {
                                    Icon(
                                        modifier = Modifier.align(Alignment.Center),
                                        painter = painterResource(id = groupIconMetadata.drawableRes),
                                        contentDescription = groupIconMetadata.name,
                                    )

                                }
                                else -> {
                                    error("Unsupported type is used. Are you sure you have the correct icon field?")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
    BackButtonHandler {
        goBackFromIconsInAGroupScreen(showkaseBrowserScreenMetadata, navController)
    }
}

private fun goBackFromIconsInAGroupScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.ICON_GROUPS)
        }
    }
}

private fun getFilteredSearchList(
    list: List<ShowkaseBrowserIcon>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) = when (showkaseBrowserScreenMetadata.value.isSearchActive) {
    false -> list
    !showkaseBrowserScreenMetadata.value.searchQuery.isNullOrBlank() -> {
        list.filter {
            matchSearchQuery(
                showkaseBrowserScreenMetadata.value.searchQuery!!,
                it.name,
                ""
            )
        }
    }
    else -> list
}
