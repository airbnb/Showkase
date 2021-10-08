package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import androidx.compose.ui.test.junit4.createComposeRule
import com.airbnb.android.showkase.annotation.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotModule
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
import org.junit.Rule

@ShowkaseScreenshotTest
open class MyScreenshotTest: ShowkaseScreenshotModule {
    @get:Rule
    override val composeTestRule = createComposeRule()

    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap
    ) {
        // TODO(vinaygaba) - Add example of doing on-device screenshot testing.
    }
}