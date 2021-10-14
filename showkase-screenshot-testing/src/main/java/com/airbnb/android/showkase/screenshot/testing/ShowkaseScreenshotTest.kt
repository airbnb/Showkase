package com.airbnb.android.showkase.screenshot.testing

import android.content.Context
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
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import org.junit.Rule
import java.util.*

/**
 *
 * Interface used to provide the logic needed for enabling screenshot test support in your repository.
 * This is always used along with the [ShowkaseScreenshot] annotation. You would typically add
 * a class that implements this interface in your root module that has access to all your UI elements.
 *
 * <p>
 * Here's an example of how you would typically use it:
 *
 * @ShowkaseScreenshotTest
 * abstract class MyScreenshotTest: ShowkaseScreenshotModule {
 *   override fun onScreenshot(
 *       id: String,
 *       name: String,
 *       group: String,
 *       screenshotType: ShowkaseScreenshotType,
 *       screenshotBitmap: Bitmap
 *   ) {
 *       // Here you do the action you want to take with the screenshot.
 *   }
 * }
 *
 * </p>
 *
 * Note: you should add this class to the androidTest sourceSet as that's where your testing
 * dependencies will exists otherwise the generate test won't compile.Additionally,Its important
 * that the class you annotate with [ShowkaseScreenshot] is either abstract or open as Showkase
 * generates a class that extends this class in order to get access to theonScreenshot method.
 */
interface ShowkaseScreenshotTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule

    val context: Context

    /**
     * This method is called during the execution of each screenshot test after the screenshot of
     * the UI element has been successfully taken. Things that you'd typically want to do here include,
     * but not limited to, the following:
     * - Calling an API service that your app uses for matching screenshots
     * - Storing the screenshot on device to generate golden copies of images
     * - Using the new screenshot and comparing/asserting against a golden copy of the same element
     *
     * @param id: a unique id to represent this screenshot. There are no guarantees that the id will
     * be identical across screenshots for the same UI element.
     * @param name: name of the UI element.
     * @param group: group that this UI element belongs to
     * @param screenshotType: A screenshot can be one of the following types: Composable, Color or Typography
     * @param screenshotBitmap: Bitmap of the given UI elemen
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
            showkaseBrowserComponent.componentKey,
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
}
