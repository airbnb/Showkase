package com.airbnb.android.showkase.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

@Composable
fun SimpleTextCard(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(start = padding4x, end = padding4x, top = padding2x, bottom = padding2x)
            .clickable(
                onClick = { onClick() }
            )
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
                modifier = Modifier.matchParentSize().then(composableContainerModifier)
            )
        }

    }
}

@Composable
private fun generateContainerModifier(onClick: (() -> Unit)?): Modifier = onClick?.let {
    Modifier.fillMaxWidth()
        .clickable(onClick = onClick)
} ?: Modifier.fillMaxWidth()
