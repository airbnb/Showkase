package com.airbnb.android.showkase.screenshot.testing

import android.graphics.Bitmap
import androidx.compose.runtime.Composable

abstract class DefaultShowkaseScreenshotTest : ShowkaseScreenshotTest {
    val matcher = BitmapMatcher()

    abstract fun generateGoldenCopy(): Boolean

    override fun onScreenshot(
        id: String,
        name: String,
        group: String,
        screenshotType: ShowkaseScreenshotType,
        screenshotBitmap: Bitmap
    ) {
        val bitmapIntArray = screenshotBitmap.toIntArray()
        val otherBitmap = screenshotBitmap.toIntArray()
        for (index in 0..2) {
            otherBitmap[index] = 100
        }

        val result = matcher.compareBitmaps(bitmapIntArray, otherBitmap, screenshotBitmap.width, screenshotBitmap.height)
        println("Match similarity is: ${result.similarityScore}")
        require(result.matches) {
            "Screenshot match failed for ${screenshotType.name} name: $name belonging to group: $group. " +
                    "Similarity score: ${result.similarityScore} is lower than the expected threshold ${result.threshold}"
        }
    }

    fun require(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            val message = lazyMessage()
            throw AssertionError(message.toString())
        }
    }
}

@Composable

fun Row(
    content: @composable () -> Unit,
    leading
    content: @composable () -> Unit
)