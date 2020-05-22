package com.vinaygaba.browser.ui

import android.content.res.Configuration
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.ui.core.ConfigurationAmbient
import androidx.ui.core.ContextAmbient
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.rtl
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.Density
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.vinaygaba.browser.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.browser.models.ShowcaseCodegenMetadata
import java.util.*

@Composable
fun ShowcaseComponentDetailScreen(
    groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>
) {
    val componentMetadataList = groupedComponentMap[ShowcaseBrowserScreenMetadata.currentGroup] ?: return
    val componentMetadata = componentMetadataList.find {
        it.componentName == ShowcaseBrowserScreenMetadata.currentComponent
    } ?: return

    AdapterList(data = listOf(componentMetadata)) { componentMetadata ->
        ShowcaseComponentCardType.values().forEach { showcaseComponentCardType ->
            when (showcaseComponentCardType) {
                ShowcaseComponentCardType.BASIC -> BasicComponentCard(
                    componentMetadata.component,
                    componentMetadata.componentName
                )
                ShowcaseComponentCardType.FONT_SCALE -> FontScaledComponentCard(
                    componentMetadata.component,
                    componentMetadata.componentName
                )
                ShowcaseComponentCardType.DISPLAY_SCALED -> DisplayScaledComponentCard(
                    componentMetadata.component,
                    componentMetadata.componentName
                )
                ShowcaseComponentCardType.RTL -> RTLComponentCard(
                    componentMetadata.component,
                    componentMetadata.componentName
                )
                ShowcaseComponentCardType.DARK_MODE -> DarkModeComponentCard(
                    componentMetadata.component,
                    componentMetadata.componentName
                )
            }
        }

    }
}

@Composable
internal fun ComponentCardTitle(componentName: String) {
    Text(
        text = componentName, modifier = Modifier.padding(16.dp),
        style = TextStyle(
            fontSize = 20.sp, fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
internal fun ComponentCard(component: @Composable() () -> Unit, modifier: Modifier = Modifier) {
    Card {
        Box(modifier = Modifier.padding(8.dp) + Modifier.fillMaxWidth() + modifier) {
            component()
        }
    }
}

@Composable
private fun BasicComponentCard(component: @Composable() () -> Unit, title: String) {
    ComponentCardTitle("$title [Basic Example]")
    ComponentCard(component)
}

@Composable
private fun FontScaledComponentCard(component: @Composable() () -> Unit, title: String) {
    val density = DensityAmbient.current
    val customDensity = Density(fontScale = density.fontScale * 2, density = density.density)

    ComponentCardTitle("$title [Font Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(component)
    }
}

@Composable
private fun DisplayScaledComponentCard(component: @Composable() () -> Unit, title: String) {
    val density = DensityAmbient.current
    val customDensity = Density(density = density.density * 2f)

    ComponentCardTitle("$title [Display Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(component)
    }
}

@Composable
private fun RTLComponentCard(component: @Composable() () -> Unit, title: String) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        val locale = Locale("ar")
        setLocale(locale)
        setLayoutDirection(locale)
    }
    val customContext = ContextAmbient.current.createConfigurationContext(customConfiguration)
    ComponentCardTitle("$title [RTL]")
    Providers(ContextAmbient provides customContext) {
        ComponentCard(component, Modifier.rtl)
    }
}

@Composable
private fun DarkModeComponentCard(component: @Composable() () -> Unit, title: String) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_YES
    }

    ComponentCardTitle("$title [Dark Mode]")
    Providers(ConfigurationAmbient provides customConfiguration) {
        ComponentCard(component)
    }
}
