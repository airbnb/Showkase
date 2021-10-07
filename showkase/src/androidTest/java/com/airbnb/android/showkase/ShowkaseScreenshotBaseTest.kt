package com.airbnb.android.showkase

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import org.junit.Rule

//abstract class ShowkaseScreenshotBaseTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    val context
//        get() = InstrumentationRegistry.getInstrumentation().context
//
//    fun runTest(
//        showkaseBrowserComponent: ShowkaseBrowserComponent
//    ) {
//        composeTestRule.setContent { showkaseBrowserComponent.component() }
//        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
//        onBitmap(showkaseBrowserComponent.hashCode().toString(), showkaseBrowserComponent.componentName, bitmap)
//    }
//
//    abstract fun onBitmap(id: String, componentName: String, bitmap: Bitmap)
//}