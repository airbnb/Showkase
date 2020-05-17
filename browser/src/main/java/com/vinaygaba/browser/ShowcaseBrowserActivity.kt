package com.vinaygaba.browser

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.remember
import androidx.ui.core.*
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
import androidx.ui.unit.Density
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import java.util.*

class ShowcaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val showcaseComponentsClass = Class.forName("$CODEGEN_PACKAGE_NAME.ShowcaseComponents")
            val map = showcaseComponentsClass.getDeclaredField("composableMap")
            map.isAccessible = true
            val result =
                map.get(showcaseComponentsClass.newInstance()) as Map<String, List<@androidx.compose.Composable() () -> Unit>>
            ShowcaseBrowserApp(groupedComponentMap = result)
        }
    }
}

@Composable
fun ShowcaseBrowserApp(groupedComponentMap: Map<String, List<@Composable() () -> Unit>>) {
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
                Text(
                    text = group, modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontSize = 20.sp, fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                )
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
    AdapterList(data = component) { component ->
        ShowcaseComponentCardType.values().forEach {
            when (it) {
                ShowcaseComponentCardType.BASIC -> BasicComponentCard(component, "Title")
                ShowcaseComponentCardType.FONT_SCALE -> FontScaledComponentCard(component, "Title")
                ShowcaseComponentCardType.DISPLAY_SCALED -> DisplayScaledComponentCard(component, "Title")
                ShowcaseComponentCardType.RTL -> RTLComponentCard(component, "Title")
                ShowcaseComponentCardType.DARK_MODE -> DarkModeComponentCard(
                    component = component,
                    title = "Title"
                )
            }
        }
    }
}

@Composable
fun ComponentCardTitle(componentName: String) {
    Text(
        text = componentName, modifier = Modifier.padding(16.dp),
        style = TextStyle(
            fontSize = 20.sp, fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun ComponentCard(component: @Composable() () -> Unit, modifier: Modifier = Modifier) {
    Card {
        Box(modifier = Modifier.padding(8.dp) + Modifier.fillMaxWidth() + modifier) {
            component()
        }
    }
}

@Composable
fun BasicComponentCard(component: @Composable() () -> Unit, title: String) {
    ComponentCardTitle("$title [Basic Example]")
    ComponentCard(component)
}

@Composable
fun FontScaledComponentCard(component: @Composable() () -> Unit, title: String) {
    val density = DensityAmbient.current
    val customDensity = Density(fontScale = density.fontScale * 2, density = density.density)

    ComponentCardTitle("$title [Font Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(component)
    }
}

@Composable
fun DisplayScaledComponentCard(component: @Composable() () -> Unit, title: String) {
    val density = DensityAmbient.current
    val customDensity = Density(density = density.density * 2f)

    ComponentCardTitle("$title [Display Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(component)
    }
}

@Composable
fun RTLComponentCard(component: @Composable() () -> Unit, title: String) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        val locale = Locale("ar")
        setLocale(locale)
        setLayoutDirection(locale)
    }
    val customContext = ContextAmbient.current.createConfigurationContext(customConfiguration)
    ComponentCardTitle("$title [RTL]")
    Providers(ContextAmbient provides customContext) {
        ComponentCard(component)
    }
}

@Composable
fun DarkModeComponentCard(component: @Composable() () -> Unit, title: String) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_YES
    }
    
    ComponentCardTitle("$title [Dark Mode]")
    Providers(ConfigurationAmbient provides customConfiguration) {
        ComponentCard(component)
    }
}

enum class ShowcaseComponentCardType {
    BASIC,
    DARK_MODE,
    RTL,
    FONT_SCALE,
    DISPLAY_SCALED,
}
