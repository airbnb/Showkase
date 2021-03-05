package com.airbnb.android.showkasesample

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding4x

val listItems = listOf("Games", "Apps", "Movies", "Books")

@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    var selectedIndex by com.airbnb.android.showkasesample.rememberSaveable { mutableStateOf(0) }

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

/**
 * Added temporarily as there is a bug in beta-01. Should be able to remove this once beta-02 is 
 * released. More details here - https://issuetracker.google.com/issues/180042685 
 */
@Composable
fun <T : Any> rememberSaveable(
    vararg inputs: Any?,
    stateSaver: Saver<T, out Any> = autoSaver(),
    key: String? = null,
    init: () -> MutableState<T>
): MutableState<T> = rememberSaveable(
    *inputs,
    saver = mutableStateSaver(init, stateSaver),
    key = key,
    init = init
)

private fun <T : Any, K : Any> mutableStateSaver(
    init: () -> MutableState<T>,
    inner: Saver<T, K>
) =
    Saver<MutableState<T>, K>(
        save = { state ->
            with(inner) { save(state.value) }
        },
        restore = { value ->
            init().also { it.value = inner.restore(value)!! }
        }
    )
