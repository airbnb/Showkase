package com.airbnb.android.showkasetesting

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import org.junit.Rule

abstract class ShowkaseScreenshotBaseTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val context
        get() = InstrumentationRegistry.getInstrumentation().context

    @RequiresApi(Build.VERSION_CODES.O)
    fun runTest(
        showkaseBrowserComponent: ShowkaseBrowserComponent
    ) {
        // Disable animations for screenshots to make them deterministic
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { showkaseBrowserComponent.component() }
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        onScreenshot(showkaseBrowserComponent.hashCode().toString(), showkaseBrowserComponent.componentName, bitmap)
    }

    abstract fun onScreenshot(id: String, name: String, screenshotBitmap: Bitmap)
}