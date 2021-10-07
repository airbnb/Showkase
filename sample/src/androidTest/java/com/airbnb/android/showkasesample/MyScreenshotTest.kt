package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotBaseTest

@ShowkaseScreenshotTest
open class MyScreenshotTest: ShowkaseScreenshotBaseTest() {
    override fun onScreenshot(id: String, name: String, group: String, screenshotBitmap: Bitmap) {
        // TODO(vinaygaba) - Add example of doing on-device screenshot testing.
    }
}