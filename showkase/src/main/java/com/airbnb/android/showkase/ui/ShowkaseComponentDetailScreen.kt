package com.airbnb.android.showkase.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.R
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.update

@Composable
internal fun ShowkaseComponentDetailScreen(
    groupedComponentMap: Map<String, List<ShowkaseBrowserComponent>>,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    val componentMetadataList =
        groupedComponentMap[showkaseBrowserScreenMetadata.value.currentGroup] ?: return
    val componentMetadata = componentMetadataList.find {
        it.componentKey == showkaseBrowserScreenMetadata.value.currentComponentKey
    } ?: return
    LazyColumn(
        modifier = Modifier.testTag("ShowkaseComponentDetailList")
    ) {
        items(
            items = listOf(componentMetadata),
            itemContent = { metadata ->
                ShowkaseComponentCardType.values().forEach { showkaseComponentCardType ->
                    when (showkaseComponentCardType) {
                        ShowkaseComponentCardType.BASIC -> {
                            if (metadata.componentKDoc.isNotBlank()) {
                                DocumentationPanel(metadata.componentKDoc)
                            }
                            BasicComponentCard(metadata)
                        }
                        ShowkaseComponentCardType.FONT_SCALE -> FontScaledComponentCard(metadata)
                        ShowkaseComponentCardType.DISPLAY_SCALED -> DisplayScaledComponentCard(
                            metadata
                        )
                        ShowkaseComponentCardType.RTL -> RTLComponentCard(metadata)
                        ShowkaseComponentCardType.DARK_MODE -> DarkModeComponentCard(metadata)
                    }
                }
            }
        )
    }
    BackButtonHandler {
        back(showkaseBrowserScreenMetadata, navController)
    }
}

@Composable
private fun DocumentationPanel(kDoc: String) {
    var showDocumentation by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Text(
                text = buttonText,
                color = MaterialTheme.colors.primary
            )
        }
        Icon(imageVector = icon, contentDescription = buttonText)
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
    val density = LocalDensity.current
    val customDensity = Density(fontScale = density.fontScale * 2, density = density.density)

    ComponentCardTitle("${metadata.componentName} [Font Scaled x 2]")
    CompositionLocalProvider(LocalDensity provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun DisplayScaledComponentCard(metadata: ShowkaseBrowserComponent) {
    val density = LocalDensity.current
    val customDensity = Density(density = density.density * 2f)

    ComponentCardTitle("${metadata.componentName} [Display Scaled x 2]")
    CompositionLocalProvider(LocalDensity provides customDensity) {
        ComponentCard(metadata)
    }
}

@Composable
private fun RTLComponentCard(metadata: ShowkaseBrowserComponent) {
    ComponentCardTitle("${metadata.componentName} [RTL]")
    val updatedModifier = Modifier.generateComposableModifier(metadata)
    Card(modifier = Modifier.fillMaxWidth()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(modifier = updatedModifier) {
                metadata.component()
            }
        }
    }
}

@Composable
private fun DarkModeComponentCard(metadata: ShowkaseBrowserComponent) {
    val darkModeConfiguration = Configuration(LocalConfiguration.current).apply {
        uiMode = Configuration.UI_MODE_NIGHT_YES
    }
    ComponentCardTitle("${metadata.componentName} [Dark Mode]")
    CompositionLocalProvider(LocalConfiguration provides darkModeConfiguration) {
        ComponentCard(
            metadata = metadata,
            darkMode = true
        )
    }
}

internal fun Modifier.generateComposableModifier(metadata: ShowkaseBrowserComponent) = composed {
    val baseModifier = padding(padding4x).sizeIn(maxHeight = Dp(LocalConfiguration.current.screenHeightDp.toFloat()))
    when {
        metadata.heightDp != null && metadata.widthDp != null -> baseModifier.size(
            width = metadata.widthDp.dp,
            height = metadata.heightDp.dp
        )
        metadata.heightDp != null -> baseModifier.height(Dp(metadata.heightDp.toFloat()))
        metadata.widthDp != null -> baseModifier.width(Dp(metadata.widthDp.toFloat()))
        else -> baseModifier.fillMaxWidth()
    }
}

private fun back(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController
) {
    showkaseBrowserScreenMetadata.update {
        copy(
            currentComponentStyleName = null,
            isSearchActive = false,
            searchQuery = null
        )
    }
    navController.navigate(ShowkaseCurrentScreen.COMPONENT_STYLES)
}
