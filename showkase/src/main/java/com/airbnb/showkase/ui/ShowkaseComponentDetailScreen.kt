package com.airbnb.showkase.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.LayoutDirectionAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseCurrentScreen
import java.util.Locale

@Composable
internal fun ShowkaseComponentDetailScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val componentMetadataList =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup] ?: return
    val componentMetadata = componentMetadataList.find {
        it.componentName == showkaseBrowserScreenMetadata.value.currentComponent
    } ?: return
    LazyColumnFor(items = listOf(componentMetadata), itemContent = { metadata ->
        ShowkaseComponentCardType.values().forEach { showkaseComponentCardType ->
            when (showkaseComponentCardType) {
                ShowkaseComponentCardType.BASIC -> BasicComponentCard(metadata)
                ShowkaseComponentCardType.FONT_SCALE -> FontScaledComponentCard(metadata)
                ShowkaseComponentCardType.DISPLAY_SCALED -> DisplayScaledComponentCard(metadata)
                ShowkaseComponentCardType.RTL -> RTLComponentCard(metadata)
                ShowkaseComponentCardType.DARK_MODE -> DarkModeComponentCard(metadata)
            }
        }
    })
    BackButtonHandler {
        goBack(showkaseBrowserScreenMetadata)
    }
    
}

@Composable
internal fun ComponentCardTitle(componentName: String) {
    Text(
        text = componentName, 
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 4.dp),
        style = TextStyle(
            fontSize = 16.sp, 
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
internal fun ComponentCard(
    metadata: ShowkaseBrowserComponent,
    onClick: (() -> Unit)? = null
) {
    val composableModifier = generateComposableModifier(metadata)
    val composableContainerModifier = generateContainerModifier(onClick)
    Card() {
        Stack() {
            Box(modifier = composableModifier) {
                metadata.component()
            }
            // Need to add this as part of the stack so that we can intercept the touch of the 
            // component when we are on the "Group components" screen. If 
            // composableContainerModifier does not have any clickable modifiers, this box has no
            // impact and the touches go through to the component(this happens in the "Component 
            // Detail" screen.
            Box(
                modifier = Modifier.matchParentSize() + composableContainerModifier
            )
        }
        
    }
}

@Composable
private fun BasicComponentCard(metadata: ShowkaseBrowserComponent) {
    ComponentCardTitle("${metadata.componentName} [Basic Example]")
    ComponentCard(metadata)
}

@Composable
private fun FontScaledComponentCard(metadata: ShowkaseBrowserComponent) {
    val density = DensityAmbient.current
    val customDensity = Density(fontScale = density.fontScale * 2, density = density.density)

    ComponentCardTitle("${metadata.componentName} [Font Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun DisplayScaledComponentCard(metadata: ShowkaseBrowserComponent) {
    val density = DensityAmbient.current
    val customDensity = Density(density = density.density * 2f)

    ComponentCardTitle("${metadata.componentName} [Display Scaled x 2]")
    Providers(DensityAmbient provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun RTLComponentCard(metadata: ShowkaseBrowserComponent) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        val locale = Locale("ar")
        setLocale(locale)
        setLayoutDirection(locale)
    }
    val customContext = ContextAmbient.current.createConfigurationContext(customConfiguration)
    ComponentCardTitle("${metadata.componentName} [RTL]")
    Providers(ContextAmbient provides customContext) {
        val updatedModifier = generateComposableModifier(metadata)
        Card(modifier = Modifier.fillMaxWidth()) {
            Providers(LayoutDirectionAmbient provides LayoutDirection.Rtl) {
                Box(modifier = updatedModifier) {
                    metadata.component()
                }
            }
        }
    }
}

@Composable
private fun DarkModeComponentCard(metadata: ShowkaseBrowserComponent) {
    val customConfiguration = Configuration(ConfigurationAmbient.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_YES
    }

    ComponentCardTitle("${metadata.componentName} [Dark Mode]")
    Providers(ConfigurationAmbient provides customConfiguration) {
        ComponentCard(metadata)
    }
}

private fun generateComposableModifier(metadata: ShowkaseBrowserComponent): Modifier {
    val baseModifier = Modifier.padding(16.dp)
    if (metadata.heightDp != null && metadata.widthDp != null) {
        return baseModifier +
                Modifier.size(width = metadata.widthDp.dp, height = metadata.heightDp.dp)
    }
    return baseModifier + Modifier.fillMaxWidth()
}

@Composable
private fun generateContainerModifier(onClick: (() -> Unit)?): Modifier = onClick?.let {
    Modifier.fillMaxWidth() + Modifier.clickable(onClick = onClick)
} ?: Modifier.fillMaxWidth()

private fun goBack(showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    showkaseBrowserScreenMetadata.value = showkaseBrowserScreenMetadata.value.copy(
        currentScreen = ShowkaseCurrentScreen.GROUP_COMPONENTS,
        currentComponent = null,
        isSearchActive = false,
        searchQuery = null
    )
}
