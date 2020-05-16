package com.vinaygaba.browser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp

class ShowcaseBrowserActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val showcaseComponentsClass = Class.forName("$CODEGEN_PACKAGE_NAME.ShowcaseComponents")
            val map = showcaseComponentsClass.getDeclaredField("composableMap")
            map.isAccessible = true
            val result = map.get(showcaseComponentsClass.newInstance()) as Map<String, List<@androidx.compose.Composable()() -> Unit>>
            ShowcaseBrowserApp(groupedComponentMap = result)
        }
    }
}

@Composable
fun ShowcaseBrowserApp(groupedComponentMap: Map<String, List<@Composable()() -> Unit>>) {
    val screenMetadata = remember { ShowcaseScreenMetadata() }
    when (screenMetadata.currentScreen) {
        ShowcaseCurrentScreen.GROUPS -> {
            ShowcaseGroupsScreen(groupedComponentMap, screenMetadata)
        }
        ShowcaseCurrentScreen.GROUP_COMPONENTS -> {
            ShowcaseGroupComponentsScreen(groupedComponentMap, screenMetadata)
        }
    }
}

@Composable
fun ShowcaseGroupsScreen(
    groupedComponentMap: Map<String, List<@Composable() () -> Unit>>,
    screenMetadata: ShowcaseScreenMetadata
) {
    AdapterList(data = groupedComponentMap.keys.toList()) { group ->
        Clickable(onClick = {
            screenMetadata.currentScreen = ShowcaseCurrentScreen.GROUP_COMPONENTS
            screenMetadata.currentGroup = group
        }) {
            Card(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp)) {
                Text(text = group, modifier = Modifier.padding(16.dp), 
                    style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Serif, 
                        fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun ShowcaseGroupComponentsScreen(
    groupedComponentMap: Map<String, List<@Composable() () -> Unit>>,
    screenMetadata: ShowcaseScreenMetadata
) {
    val component = groupedComponentMap[screenMetadata.currentGroup] ?: return
    AdapterList(data = component) {
        Box(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp)) {
            it()
        }
    }
}
