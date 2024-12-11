package com.airbnb.android.showkase.screenshot.testing.shot

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
import com.karumi.shot.ScreenshotTest

abstract class ShotShowkaseScreenshotTest : ShowkaseScreenshotTest, ScreenshotTest {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        styleName: String?,
        tags: List<String>,
        extraMetadata: List<String>,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap
    ) {
        compareScreenshot(composeTestRule)
    }
}
