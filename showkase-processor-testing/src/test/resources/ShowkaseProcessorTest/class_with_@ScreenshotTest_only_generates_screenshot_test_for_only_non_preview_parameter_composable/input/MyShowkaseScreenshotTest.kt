package com.airbnb.android.showkase_processor_testing

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType

@ShowkaseScreenshot(rootShowkaseClass = TestShowkaseRoot::class)
public abstract class MyScreenshotTest: ShowkaseScreenshotTest {
    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        styleName: String?,
        tags: List<String>,
        extraMetadata: List<String>,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap,
    ) {

    }
}