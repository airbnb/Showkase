package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.ui.unit.LayoutDirection
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseDeviceConfig
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseTestPreview
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseUIMode

@ShowkaseScreenshot(rootShowkaseClass = PaparazziSampleRootModule::class)
abstract class MyPaparazziShowkaseScreenshotTest : PaparazziShowkaseScreenshotTest {
    override fun takePaparazziSnapshot(
        paparazzi: Paparazzi,
        testPreview: PaparazziShowkaseTestPreview,
        direction: LayoutDirection,
        mode: PaparazziShowkaseUIMode
    ) {
        if (paparazzi.resources.configuration.locales.get(0).toString() == "en-gb" ||
            testPreview.group == "Localised"
        ) {
            super.takePaparazziSnapshot(paparazzi, testPreview, direction, mode)
        }
    }

    companion object : PaparazziShowkaseScreenshotTest.CompanionObject {
        override fun deviceConfigs(): List<PaparazziShowkaseDeviceConfig> {
            return listOf("en-rGB", "nb-rNO").map { locale ->
                PaparazziShowkaseDeviceConfig(
                    deviceConfig = DeviceConfig.PIXEL_5.copy(locale = locale)
                )
            }
        }
    }
}
