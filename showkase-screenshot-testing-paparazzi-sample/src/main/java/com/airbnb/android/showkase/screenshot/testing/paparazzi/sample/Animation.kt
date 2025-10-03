package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ScreenshotCaptureConfig
import com.airbnb.android.showkase.annotation.ScreenshotCaptureType
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(
    name = "AnimatedOffset",
    group = "Animated",
    defaultStyle = true,
    screenshotCaptureConfig = ScreenshotCaptureConfig(
        type = ScreenshotCaptureType.SingleAnimatedImage,
        durationMillis = 2000,
    ),
)
@Composable
fun BoxWithAnimatedOffsetPreview() {
    // This code triggers an animation in this preview
    var isOffsetToRight by remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        if (isOffsetToRight) 200.dp else 0.dp,
        animationSpec = tween(2000)
    )
    LaunchedEffect(Unit) {
        isOffsetToRight = true
    }

    Box(
        Modifier
            .width(300.dp)
            .border(1.dp, Color.Blue)
    ) {
        Spacer(
            Modifier
                .offset(x = offset)
                .size(40.dp)
                .background(Color.Red)
        )
    }
}
