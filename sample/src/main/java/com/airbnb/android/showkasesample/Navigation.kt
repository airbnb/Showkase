package com.airbnb.android.showkasesample

import androidx.compose.foundation.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding4x

val listItems = listOf("Games", "Apps", "Movies", "Books")

@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    var selectedIndex by savedInstanceState { 0 }

    ShowkaseTheme {
        BottomNavigation(modifier = Modifier.padding(padding4x)) {
            listItems.forEachIndexed { index, label ->
                BottomNavigationItem(
                    icon = {
                        Icon(asset = Icons.Filled.Favorite)
                    },
                    label = {
                        Text(text = label)
                    },
                    selected = selectedIndex == index,
                    onSelect = { selectedIndex = index }
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
