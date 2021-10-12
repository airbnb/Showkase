package com.airbnb.android.showkasesample

import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.DefaultShowkaseScreenshotTest

@ShowkaseScreenshot
abstract class MyDefaultScreenshotTest: DefaultShowkaseScreenshotTest() {
    override fun generateGoldenCopy(): Boolean {
        return true
    }
}