package com.airbnb.android.showkase.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.LayoutDirectionAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.R
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update
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
                ShowkaseComponentCardType.BASIC -> {
                    if (!metadata.componentKDoc.isBlank()) {
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
        back(showkaseBrowserScreenMetadata)
    }
    
}

@Composable
private fun DocumentationPanel(kDoc: String) {
    var showDocumentation by remember { mutableStateOf(false) }
    val context = ContextAmbient.current
    val (buttonText, icon) = getCollabsableTextAndIcon(context, showDocumentation)
    val onClick = { showDocumentation = !showDocumentation }
    if (showDocumentation) {
        Text(
            modifier = Modifier.padding(start = padding4x, end = padding4x, top = padding2x),
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
        modifier = Modifier.padding(start = padding4x, end = padding4x, top = padding2x)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalGravity = Alignment.CenterVertically
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Text(
                text = buttonText,
                color = MaterialTheme.colors.primary
            )
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

internal fun generateComposableModifier(metadata: ShowkaseBrowserComponent): Modifier {
    val baseModifier = Modifier.padding(padding4x)
    return when {
        metadata.heightDp != null && metadata.widthDp != null -> baseModifier.size(
            width = metadata.widthDp.dp, 
            height = metadata.heightDp.dp
        )
        metadata.heightDp != null -> baseModifier.height(Dp(metadata.heightDp.toFloat()))
        metadata.widthDp != null -> baseModifier.width(Dp(metadata.widthDp.toFloat()))
        else -> baseModifier.fillMaxWidth()
    }
}

private fun back(showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>) {
    showkaseBrowserScreenMetadata.update {
        copy(
            currentScreen = ShowkaseCurrentScreen.COMPONENTS_IN_A_GROUP,
            currentComponent = null,
            isSearchActive = false,
            searchQuery = null
        )
    }
}
