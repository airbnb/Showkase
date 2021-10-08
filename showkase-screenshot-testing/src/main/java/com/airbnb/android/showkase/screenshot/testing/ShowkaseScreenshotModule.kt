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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.ui.padding4x
import org.junit.Rule
import java.util.*

/**
 * TODO: Add documentation
 */
interface ShowkaseScreenshotModule {
    @get:Rule
    val composeTestRule: ComposeContentTestRule

    /**
     * TODO: Add documentation
     */
    fun onScreenshot(
        id: String,
        name: String,
        group: String,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun takeComposableScreenshot(
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
            ShowkaseScreenshotType.Composable,
            bitmap
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun takeTypographyScreenshot(
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
            ShowkaseScreenshotType.Typography,
            bitmap
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun takeColorScreenshot(
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
            ShowkaseScreenshotType.Color,
            bitmap
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun screenshotComposable(content: @Composable () -> Unit): Bitmap {
        // Disable animations for screenshots to make them deterministic
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            content()
        }
        return composeTestRule.onRoot().captureToImage().asAndroidBitmap()
    }
}
