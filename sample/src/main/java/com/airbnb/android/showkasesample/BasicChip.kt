package com.airbnb.android.showkasesample

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun BasicChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.graphicsLayer(
            clip = true,
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        BasicText(
            text = text,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@ShowkaseComposable(name = "Basic Chip", group = "Chips", defaultStyle = true)
@Composable
fun BasicChipPreview() {
    BasicChip(text = "Chip Component")
}

@ShowkaseComposable(name = "Basic Chip", group = "Chips", styleName = "Yellow Background")
@Composable
fun BasicChipYellowPreview() {
    BasicChip(
        text = "Chip Component",
        modifier = Modifier.background(color = Color.Yellow)
    )
}

@Preview(name = "Basic Chip Blue Light", group = "Chips", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Basic Chip Blue Dark", group = "Chips", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BasicChipBluePreview() {
    BasicChip(
        text = "Chip Component",
        modifier = Modifier.background(color = Color.Blue)
    )
}

@ShowkaseComposable(name = "Basic Chip Grey Light", group = "Chips")
@ShowkaseComposable(name = "Basic Chip Grey Dark", group = "Chips")
@Composable
fun BasicChipGreyPreview() {
    BasicChip(
        text = "Chip Component",
        modifier = Modifier.background(color = Color.Gray)
    )
}
