package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import app.cash.paparazzi.DeviceConfig
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseDeviceConfig
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = PaparazziSampleRootModule::class)
abstract class MyPaparazziShowkaseScreenshotTest : PaparazziShowkaseScreenshotTest {
    companion object : PaparazziShowkaseScreenshotTest.CompanionObject {
        override fun deviceConfigs(): List<PaparazziShowkaseDeviceConfig> {
            return listOf("en-rGB", "nb-rNO").map { locale ->
                PaparazziShowkaseDeviceConfig(
                    deviceConfig = DeviceConfig.PIXEL_5.copy(locale = locale)
                )
            }
        }
    }

    override fun shouldTakeScreenshot(deviceConfig: DeviceConfig, group: String): Boolean {
        return deviceConfig.locale == "en-rGB" || group == "Localised"
    }
}
