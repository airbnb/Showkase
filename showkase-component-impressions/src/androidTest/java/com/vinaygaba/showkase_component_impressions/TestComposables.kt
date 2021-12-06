package com.vinaygaba.showkase_component_impressions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun BasicComposableWithVisibilityToggle() {
    var showkaseVisibilityEvent: ShowkaseVisibilityEvent<String>? by remember {
        mutableStateOf(null)
    }
    var isVisible by remember { mutableStateOf(true) }
    Column {
        if (isVisible) {
            BasicText(
                text = "Testing Impression Events",
                modifier = Modifier
                    .visibilityEvents(
                        key = "key",
                        onVisibilityChanged = { event ->
                            showkaseVisibilityEvent = event
                        }
                    )
                    .clickable { isVisible = !isVisible }
            )
        }

        BasicText(
            text = "key: ${showkaseVisibilityEvent?.key} visibilityPercentage: ${showkaseVisibilityEvent?.visibilityPercentage}"
        )
    }
}

// WIP. Do Not Review
@Composable
internal fun BasicTestComposableWithEvents() {
    var counter by remember { mutableStateOf(0) }
    val counterLambda = { counter++ }
//    var impressionData: ImpressionData? by remember {
//        mutableStateOf(null)
//    }
    Column {
        BasicText(
            text = "Testing Impression Events",
            modifier = Modifier.visibilityEvents(
                key = "key",
                onVisibilityChanged = { event ->
//                    impressionData = ImpressionData(key, visibilityPercentage, bounds)
                    counterLambda()
                }
            )
        )

        BasicText(text = "counter: $counter")
    }
}

@Composable
internal fun PartiallyVisibleComposable() {
    var showkaseVisibilityEvent: ShowkaseVisibilityEvent<String>? by remember {
        mutableStateOf(null)
    }

    Column {
        BasicText(
            text = "Impression Logging Text",
            style = TextStyle(
                fontSize = 40.sp,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.offset(x = (-40).dp, y = (-40).dp)
                .visibilityEvents(
                    key = "key",
                    onVisibilityChanged = { event ->
                        showkaseVisibilityEvent = event
                    }
                )
        )

        BasicText(
            text = "key: ${showkaseVisibilityEvent?.key} visibilityPercentage: ${showkaseVisibilityEvent?.visibilityPercentage}"
        )
    }
}