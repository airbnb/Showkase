package com.airbnb.android.showkase.screenshot.testing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.screenshot.testing.exceptions.ShowkaseScreenshotTestingException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

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
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        when {
            generateGoldenCopy() ->
                saveBitmapToDisk(screenshotBitmap, id, name, group, screenshotType, context)
            else -> compareBitmaps(screenshotBitmap, screenshotType, name, group, context)
        }
    }

    private fun saveBitmapToDisk(
        screenshotBitmap: Bitmap,
        id: String,
        name: String,
        group: String,
        screenshotType: ShowkaseScreenshotType,
        context: Context
    ) {
        val folderPath = File(
            context.filesDir.canonicalPath,
            generateFolderPath(context)
        )
        if (!folderPath.exists()) {
            folderPath.mkdirs()
        }
        val fileName = generateFileName(
            screenshotType = screenshotType,
            group = group,
            name = name
        )
        FileOutputStream("$folderPath/$fileName").use { out ->
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        println("Saved screenshot")
    }

    private fun generateFileName(
        screenshotType: ShowkaseScreenshotType,
        group: String,
        name: String,
    ) = "${screenshotType}_${group}_${name}.png"

    private fun generateFolderPath(context: Context): String {
        val apiVersion = getAndroidAPIVersion()
        val screenDensityName = getScreenDensityName(context)
        val abi = getAndroidABI()
        val (width, height) = getScreenDimensions(context)

        return "API${apiVersion}_${screenDensityName}_${width}x${height}_${abi}"
    }

    private fun compareBitmaps(
        screenshotBitmap: Bitmap,
        screenshotType: ShowkaseScreenshotType,
        name: String,
        group: String,
        context: Context
    ) {
        val folderPath = generateFolderPath(context)
        val bitmapIntArray = screenshotBitmap.toIntArray()
        val goldenBitmap = loadBitmapFromAsset(
            screenshotType = screenshotType,
            group = group,
            name = name,
            folderPath = folderPath
        ).toIntArray()

        val result = matcher.compareBitmaps(
            given = bitmapIntArray,
            expected = goldenBitmap,
            width = screenshotBitmap.width,
            height = screenshotBitmap.height
        )
        Log.i(TAG, "Match similarity for screenshotType: \"${screenshotType.name}\" name: \"$name\" " +
                "group: \"$group\" is: ${result.similarityScore}")
        require(result.matches) {
            "Screenshot match failed for screenshotType: \"${screenshotType.name}\" name: \"$name\" " +
                    "belonging to group: \"$group\" and present in folder: \"$folderPath\". Similarity " +
                    "score: ${result.similarityScore} is lower than the expected threshold " +
                    "${result.threshold}"
        }
    }

    private fun loadBitmapFromAsset(
        screenshotType: ShowkaseScreenshotType,
        group: String,
        name: String,
        folderPath: String
    ): Bitmap {
        val fileName = generateFileName(
            screenshotType = screenshotType,
            group = group,
            name = name
        )
        try {
            return loadBitmap(
                folderPath = folderPath,
                fileName = fileName
            )
        } catch (exception: FileNotFoundException) {
            throw ShowkaseScreenshotTestingException(
                "The golden copy of screenshotType: \"$screenshotType\" name: \"$name\" group: " +
                        "\"$group\" was not found in the folder: \"$folderPath\" under " +
                        "assets. If this is a new UI element, you will have to generate " +
                        "its golden copy and place it in the asset folder in the androidTest " +
                        "sourceSet where you have defined your ShowkaseScreenshotTest. You can " +
                        "leverage the generateGoldenCopy method to generate the new golden copies " +
                        "of you UI elements. "
            )
        }
    }

    private fun loadBitmap(
        folderPath: String,
        fileName: String
    ) = InstrumentationRegistry.getInstrumentation()
        .context.resources.assets.open("${folderPath}/${fileName}").use {
            BitmapFactory.decodeStream(it)
        }

    private fun require(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            val message = lazyMessage()
            throw ShowkaseScreenshotTestingException(message.toString())
        }
    }
}