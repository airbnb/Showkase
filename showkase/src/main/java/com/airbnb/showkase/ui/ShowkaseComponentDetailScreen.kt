package com.airbnb.showkase.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.Providers
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.ConfigurationAmbient
import androidx.ui.core.ContextAmbient
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.graphics.Color
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.rtl
import androidx.ui.layout.size
import androidx.ui.material.Card
import androidx.ui.material.TextButton
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.KeyboardArrowDown
import androidx.ui.material.icons.filled.KeyboardArrowUp
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.Density
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.airbnb.showkase.R
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
    LazyColumnItems(items = listOf(componentMetadata), itemContent = { metadata ->
        ShowkaseComponentCardType.values().forEach { showkaseComponentCardType ->
            when (showkaseComponentCardType) {
                ShowkaseComponentCardType.BASIC -> {
                    if (!metadata.componentKDoc.isNullOrBlank()) {
                        DocumentationPanel(metadata.componentKDoc)
                    }
                    BasicComponentCard(metadata)
                }
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
private fun DocumentationPanel(kDoc: String) {
    var showDocumentation by state { false }
    val context = ContextAmbient.current
    val (buttonText, icon) = getCollabsableTextAndIcon(context, showDocumentation)
    if (showDocumentation) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = kDoc,
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W300
            )
        )
    }
    Row(
        modifier = Modifier.padding(start = 10.dp, end = 16.dp, top = 8.dp) + Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalGravity = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
                showDocumentation = !showDocumentation
            }
        ) {
            Text(text = buttonText)
        }
        Icon(asset = icon)
    }
    
}

private fun getCollabsableTextAndIcon(
    context: Context,
    showDocumentation: Boolean
) = when (showDocumentation) {
    true -> context.getString(R.string.showkase_browser_hide_documentation) to Icons.Filled.KeyboardArrowUp
    false -> context.getString(R.string.showkase_browser_show_documentation) to Icons.Filled.KeyboardArrowDown
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
            Box(modifier = Modifier.rtl) {
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
