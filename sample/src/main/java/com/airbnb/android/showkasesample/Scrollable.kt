package com.airbnb.android.showkasesample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerticalScrollSample() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        repeat(50) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

@Preview(group = "Scrollable", name = "Vertical Scroll")
@Composable
fun VerticalScrollPreview() {
    VerticalScrollSample()
}
