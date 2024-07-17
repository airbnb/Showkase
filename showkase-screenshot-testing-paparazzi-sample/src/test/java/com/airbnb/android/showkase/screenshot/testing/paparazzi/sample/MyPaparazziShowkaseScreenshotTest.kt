package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = PaparazziSampleRootModule::class)
abstract class MyPaparazziShowkaseScreenshotTest : PaparazziShowkaseScreenshotTest {
    companion object : PaparazziShowkaseScreenshotTest.CompanionObject
}
