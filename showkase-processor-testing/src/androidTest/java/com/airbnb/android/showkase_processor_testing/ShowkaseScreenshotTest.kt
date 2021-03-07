package com.airbnb.android.showkase_processor_testing

import android.graphics.Bitmap
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.models.ShowkaseProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@RunWith(JUnit4::class)
class ShowkaseScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()
//    @get:Rule
//    val screenshotTestRule =  AndroidXScreenshotRule

    val deviceOutputDirectory
        get() = File(
            context.filesDir,
            "showkase_screenshots"
        )
    
    val context
    get() = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun generateScreenshotReport() {
        val key = "com.vinaygaba.showcase_processor_testing.MyRootModule"
        val showkaseComponentProvider =
            Class.forName("${key}Codegen").newInstance() as ShowkaseProvider
        val components = showkaseComponentProvider.getShowkaseComponents()

        val component = components.first()
        composeTestRule.setContent { component.component() }
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        saveImage(bitmap)
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val root: String = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES!!)
            ?.absolutePath!!
        val myDir = File("$root/saved_images")
        val dirCreated = myDir.mkdirs()
        Log.e("Dir created","dir created $dirCreated")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Shutta_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            Log.e("Saving file", file.absolutePath)
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Log.e("Exception", "Exception" + e.message)
            e.printStackTrace()
        }
    }
}
