
import android.graphics.Bitmap
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest

@ShowkaseScreenshot(rootShowkaseClass = TestShowkaseRoot::class)
public abstract class MyScreenshotTest: PaparazziShowkaseScreenshotTest {
    public companion object: PaparazziShowkaseScreenshotTest.CompanionObject
}