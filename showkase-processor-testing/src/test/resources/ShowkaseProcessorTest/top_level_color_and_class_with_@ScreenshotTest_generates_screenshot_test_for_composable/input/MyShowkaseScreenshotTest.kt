package com.airbnb.android.showkase_processor_testing

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType

@ShowkaseScreenshot(rootShowkaseClass = TestShowkaseRoot::class)
abstract class MyScreenshotTest: ShowkaseScreenshotTest {
    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        styleName: String?,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap,
    ) {
        
    }
}