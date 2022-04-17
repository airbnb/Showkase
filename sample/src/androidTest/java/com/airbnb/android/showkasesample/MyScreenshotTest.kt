package com.airbnb.android.showkasesample

import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.shot.ShotShowkaseScreenshotTest
import com.airbnb.android.showkasetest.MyTestRootModule

@ShowkaseScreenshot(rootShowkaseClass = MyTestRootModule::class)
abstract class MyScreenshotTest: ShotShowkaseScreenshotTest()