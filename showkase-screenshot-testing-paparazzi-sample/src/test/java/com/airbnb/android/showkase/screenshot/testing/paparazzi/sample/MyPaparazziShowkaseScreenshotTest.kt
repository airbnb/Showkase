package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.ui.unit.LayoutDirection
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = PaparazziSampleRootModule::class)
abstract class MyPaparazziShowkaseScreenshotTest: PaparazziShowkaseScreenshotTest {
    companion object: PaparazziShowkaseScreenshotTest.CompanionObject
}