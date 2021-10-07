package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshotTest
import com.airbnb.android.showkasetesting.ShowkaseScreenshotBaseTest

@ShowkaseScreenshotTest
open class MyScreenshotTest: ShowkaseScreenshotBaseTest() {
    override fun onScreenshot(id: String, name: String, screenshotBitmap: Bitmap) {

    }
}