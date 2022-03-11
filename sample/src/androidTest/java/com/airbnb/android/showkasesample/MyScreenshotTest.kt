package com.airbnb.android.showkasesample

import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkasetest.MyTestRootModule
import com.vinaygaba.showkase_screenshot_testing_shot.ShotShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = MyTestRootModule::class)
abstract class MyScreenshotTest: ShotShowkaseScreenshotTest()