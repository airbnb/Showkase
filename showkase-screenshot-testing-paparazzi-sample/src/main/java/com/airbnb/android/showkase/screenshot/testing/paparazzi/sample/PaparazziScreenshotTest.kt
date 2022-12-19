package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = PaparazziSampleRootModule::class)
class PaparazziScreenshotTest: PaparazziShowkaseScreenshotTest() {
    override val paparazzi = Paparazzi()
}