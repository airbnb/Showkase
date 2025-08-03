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
import com.airbnb.android.showkase.annotation.ShowkaseComposable

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

// This preview was only meant for the Android Studio preview and we want to avoid displaying this
// in the ShowkaseBrowser. In order to do this, I used the ShowkaseComposable annotation along with
// the skip property set to true.
@ShowkaseComposable(skip = true)
@Preview(group = "Scrollable", name = "Vertical Scroll2", showBackground = true)
@Composable
fun VerticalScrollPreview2() {
    VerticalScrollSample()
}
