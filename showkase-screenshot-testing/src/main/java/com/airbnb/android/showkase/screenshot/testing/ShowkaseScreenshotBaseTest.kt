package com.airbnb.android.showkase.screenshot.testing

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import org.junit.Rule
import java.util.*

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
        onScreenshot(
            showkaseBrowserComponent.hashCode().toString(),
            showkaseBrowserComponent.componentName,
            showkaseBrowserComponent.group,
            bitmap
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runTest(
        showkaseBrowserTypography: ShowkaseBrowserTypography
    ) {
        // Disable animations for screenshots to make them deterministic
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            BasicText(
                text = showkaseBrowserTypography.typographyName.replaceFirstChar {
                    it.titlecase(Locale.getDefault())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = showkaseBrowserTypography.textStyle
            )
        }
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        onScreenshot(
            showkaseBrowserTypography.hashCode().toString(),
            showkaseBrowserTypography.typographyName,
            showkaseBrowserTypography.typographyGroup,
            bitmap
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runTest(
        showkaseBrowserColor: ShowkaseBrowserColor
    ) {
        // Disable animations for screenshots to make them deterministic
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(250.dp)
                    .background(showkaseBrowserColor.color)
            )
        }
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        onScreenshot(
            showkaseBrowserColor.hashCode().toString(),
            showkaseBrowserColor.colorName,
            showkaseBrowserColor.colorGroup,
            bitmap
        )
    }

    abstract fun onScreenshot(id: String, name: String, group: String, screenshotBitmap: Bitmap)
}