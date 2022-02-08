package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType

// This test is similar to [MyScreenshotTest] however it uses a different root module. This is
// to provide coverage of a bug that was introduced previously - 
// https://github.com/airbnb/Showkase/issues/212
@ShowkaseScreenshot(rootShowkaseClass = RootModule::class)
abstract class MySecondScreenshotTest: ShowkaseScreenshotTest {
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