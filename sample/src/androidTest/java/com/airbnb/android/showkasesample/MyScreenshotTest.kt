package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
import com.airbnb.android.showkasetest.MyTestRootModule

@ShowkaseScreenshot(rootShowkaseClass = MyTestRootModule::class)
abstract class MyScreenshotTest: ShowkaseScreenshotTest {
    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        styleName: String?,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap,
    ) {
        // TODO(vinaygaba) - Add example of doing on-device screenshot testing.
    }
}