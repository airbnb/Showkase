package com.airbnb.android.showkase.ui

import androidx.compose.material.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleTextCard(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding4x, end = padding4x, top = padding2x, bottom = padding2x),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(padding4x),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
internal fun ComponentCardTitle(componentName: String) {
    Text(
        text = componentName,
        modifier = Modifier.padding(start = padding4x, end = padding4x, top = padding8x,
            bottom = padding1x),
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
    onClick: (() -> Unit)? = null,
    darkMode: Boolean = false,
) {
    val composableModifier = Modifier.generateComposableModifier(metadata)
    val composableContainerModifier = Modifier.generateContainerModifier(onClick)
    MaterialTheme(
        colors = if (darkMode) darkColors() else lightColors()
    ) {
        Card(
            shape = MaterialTheme.shapes.large
        ) {
            Box {
                Column(modifier = composableModifier) {
                    metadata.component()
                }
                // Need to add this as part of the stack so that we can intercept the touch of the
                // component when we are on the "Group components" screen. If
                // composableContainerModifier does not have any clickable modifiers, this column has no
                // impact and the touches go through to the component(this happens in the "Component
                // Detail" screen.
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .then(composableContainerModifier)
                ) {}
            }
        }
    }
}

private fun Modifier.generateContainerModifier(onClick: (() -> Unit)?) =
    onClick?.let {
        fillMaxWidth()
            .clickable(onClick = onClick)
    } ?: fillMaxWidth()
