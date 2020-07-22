package com.airbnb.showkasesample

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.padding
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.airbnb.showkase.annotation.models.Showkase

val listItems = listOf("Games", "Apps", "Movies", "Books")

@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    var selectedIndex by state { 0 }

    ShowkaseTheme {
        BottomNavigation(modifier = Modifier.padding(16.dp)) {
            listItems.forEachIndexed { index, label ->
                BottomNavigationItem(
                    icon = {
                        Icon(asset = Icons.Filled.Favorite)
                    },
                    text = {
                        Text(text = label)
                    },
                    selected = selectedIndex == index,
                    onSelected = { selectedIndex = index }
                )
            }
        }
    }
}

@Showkase(name = "Bottom Navigation Bar", group = "Navigation")
@Preview
@Composable
fun BottomNavigationAlwaysShowLabelComponentPreview() {
    BottomNavigationAlwaysShowLabelComponent()
}
