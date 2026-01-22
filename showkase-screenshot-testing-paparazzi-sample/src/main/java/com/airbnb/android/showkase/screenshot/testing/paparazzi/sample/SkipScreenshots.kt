package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ScreenshotCaptureConfig
import com.airbnb.android.showkase.annotation.ScreenshotCaptureType
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(
    name = "SkipScreenshots",
    defaultStyle = true,
    screenshotCaptureConfig = ScreenshotCaptureConfig(type = ScreenshotCaptureType.Skip),
)
@Composable
fun SkipScreenshotsPreview() {
    throw IllegalStateException("Intentional crash to fail if screenshot taken")
}
