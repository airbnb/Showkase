package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding4x

val listItems = listOf("Games", "Apps", "Movies", "Books")

@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    ShowkaseTheme {
        BottomNavigation(modifier = Modifier.padding(padding4x)) {
            listItems.forEachIndexed { index, label ->
                BottomNavigationItem(
                    icon = {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favorite")
                    },
                    label = {
                        Text(text = label)
                    },
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index }
                )
            }
        }
    }
}

@ShowkaseComposable(name = "Bottom Navigation Bar", group = "Navigation")
@Preview
@Composable
fun BottomNavigationAlwaysShowLabelComponentPreview() {
    BottomNavigationAlwaysShowLabelComponent()
}
