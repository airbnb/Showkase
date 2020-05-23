package com.vinaygaba.showcase.ui

import android.content.res.Configuration
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.ui.core.CombinedModifier
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
import androidx.ui.layout.size
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.Density
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
import java.util.*

@Composable
internal fun ShowcaseComponentDetailScreen(
    groupedComponentMap: Map<String, List<ShowcaseCodegenMetadata>>
) {
    val componentMetadataList = groupedComponentMap[ShowcaseBrowserScreenMetadata.currentGroup] ?: return
    val componentMetadata = componentMetadataList.find {
        it.componentName == ShowcaseBrowserScreenMetadata.currentComponent
    } ?: return

    AdapterList(data = listOf(componentMetadata)) { metadata ->
        ShowcaseComponentCardType.values().forEach { showcaseComponentCardType ->
            when (showcaseComponentCardType) {
                ShowcaseComponentCardType.BASIC -> BasicComponentCard(metadata)
                ShowcaseComponentCardType.FONT_SCALE -> FontScaledComponentCard(metadata)
                ShowcaseComponentCardType.DISPLAY_SCALED -> DisplayScaledComponentCard(metadata)
                ShowcaseComponentCardType.RTL -> RTLComponentCard(metadata)
                ShowcaseComponentCardType.DARK_MODE -> DarkModeComponentCard(metadata)
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
internal fun ComponentCard(metadata: ShowcaseCodegenMetadata) {
    val updatedModifier = generateDimensionModifier(metadata)
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = updatedModifier) {
            metadata.component()
        }
    }
}

@Composable
private fun BasicComponentCard(metadata: ShowcaseCodegenMetadata) {
    ComponentCardTitle("${metadata.componentName} [Basic Example]")
    ComponentCard(metadata)
}

@Composable
private fun FontScaledComponentCard(metadata: ShowcaseCodegenMetadata) {
    val density = DensityAmbient.current
    val customDensity = Density(fontScale = density.fontScale * 2, density = density.density)

    ComponentCardTitle("${metadata.componentName} [Font Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun DisplayScaledComponentCard(metadata: ShowcaseCodegenMetadata) {
    val density = DensityAmbient.current
    val customDensity = Density(density = density.density * 2f)

    ComponentCardTitle("${metadata.componentName} [Display Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun RTLComponentCard(metadata: ShowcaseCodegenMetadata) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        val locale = Locale("ar")
        setLocale(locale)
        setLayoutDirection(locale)
    }
    val customContext = ContextAmbient.current.createConfigurationContext(customConfiguration)
    ComponentCardTitle("${metadata.componentName} [RTL]")
    Providers(ContextAmbient provides customContext) {
        val updatedModifier = generateDimensionModifier(metadata)
        Card(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.rtl) {
                Box(modifier = updatedModifier) {
                    metadata.component()
                }
            }
        }
    }
}

@Composable
private fun DarkModeComponentCard(metadata: ShowcaseCodegenMetadata) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_YES
    }

    ComponentCardTitle("${metadata.componentName} [Dark Mode]")
    Providers(ConfigurationAmbient provides customConfiguration) {
        ComponentCard(metadata)
    }
}

private fun generateDimensionModifier(metadata: ShowcaseCodegenMetadata): Modifier {
    val baseModifier = Modifier.padding(16.dp)
    if (metadata.heightDp > 0 || metadata.widthDp > 0) {
        return baseModifier + 
                Modifier.size(width =  metadata.widthDp.dp, height = metadata.heightDp.dp)
    }
    return baseModifier + Modifier.fillMaxWidth()
}
