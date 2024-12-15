package com.airbnb.android.showkase.screenshot.testing.paparazzi

import android.content.res.Configuration
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import com.android.ide.common.rendering.api.SessionParams
import java.util.Locale

/**
 *
 * Interface that needs to be implemented for auto-generating screenshot tests that leverage
 * [Paparazzi]. This is generally used along with the [ShowkaseScreenshot] annotation. You will
 * typically add the implementation of this interface in your root module that has access to all
 * your UI elements that you'd like to test. In addition, you need to make sure that the
 * implementing class is an abstract/open class. Finally, the companion object of your implementation
 * class needs to implement the [PaparazziShowkaseScreenshotTest.CompanionObject] interface. This
 * interface provides a mechanism to override the default behavior if you need to.
 *
 * <p>
 * Here's an example of how you would typically use it with the defaults:
 *
 * @ShowkaseScreenshotTest
 * abstract class MyScreenshotTest: PaparazziShowkaseScreenshotTest {
 *   companion object: PaparazziShowkaseScreenshotTest.CompanionObject
 * }
 *
 * </p>
 *
 * Note: Paparazzi requires your screenshot tests to be in a library module. Please ensure that the
 * class that implements this interface is in a library module, other the Paparazzi integration
 * won't work as expected.
 *
 */
interface PaparazziShowkaseScreenshotTest {

    /**
     * Interface that *must* be implemented by the companion object of your [PaparazziShowkaseScreenshotTest]
     * implementation.
     */
    interface CompanionObject {

        /**
         * Returns the [Paparazzi] implementation that should be used when running the screenshot
         * tests.
         */
        fun providePaparazzi(): Paparazzi = Paparazzi(
            maxPercentDifference = 0.0,
            showSystemUi = false,
            renderingMode = SessionParams.RenderingMode.SHRINK
        )

        /**
         * The list of devices that we should run the screenshot tests on. It returns a list
         * of [PaparazziShowkaseDeviceConfig], which is a wrapper for Paparazzi's [DeviceConfig].
         */
        fun deviceConfigs(): List<PaparazziShowkaseDeviceConfig> = listOf(
            PaparazziShowkaseDeviceConfig()
        )

        /**
         * The list of layout directions that we should run the screenshot on. By default, the
         * screenshots are only taken in left-to-right layout direction.
         */
        fun layoutDirections(): List<LayoutDirection> = listOf(LayoutDirection.Ltr)

        /**
         * The list of [PaparazziShowkaseUIMode]'s that we should run the screenshots on. Other than
         * default, you can also toggle dark mode through this setting for your screenshots.
         */
        fun uiModes(): List<PaparazziShowkaseUIMode> = listOf(PaparazziShowkaseUIMode.DEFAULT)
    }

    fun takePaparazziSnapshot(
        paparazzi: Paparazzi,
        testPreview: PaparazziShowkaseTestPreview,
        direction: LayoutDirection,
        mode: PaparazziShowkaseUIMode
    ) {
        paparazzi.snapshot(name = testPreview.toString()) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val configuration = if (mode == PaparazziShowkaseUIMode.DARK) {
                Configuration(LocalConfiguration.current).apply {
                    uiMode = Configuration.UI_MODE_NIGHT_YES
                }
            } else {
                LocalConfiguration.current
            }
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                ),
                LocalConfiguration provides configuration,
                LocalLayoutDirection provides direction,
                // Needed so that UI that uses it don't crash during screenshot tests
                LocalOnBackPressedDispatcherOwner provides object : OnBackPressedDispatcherOwner {
                    override val lifecycle: Lifecycle
                        get() = lifecycleOwner.lifecycle
                    override val onBackPressedDispatcher: OnBackPressedDispatcher
                        get() = OnBackPressedDispatcher()
                }
            ) {
                Box {
                    testPreview.Content()
                }
            }
        }
    }
}

interface PaparazziShowkaseTestPreview {
    @Composable
    fun Content()
}

private const val DELIM = "**"

class ComponentPaparazziShowkaseTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent,
) : PaparazziShowkaseTestPreview {

    @Composable
    override fun Content() = showkaseBrowserComponent.component()
    override fun toString(): String =
        "${showkaseBrowserComponent.group}$DELIM${showkaseBrowserComponent.componentName}$DELIM" +
                "${showkaseBrowserComponent.styleName}"
}

class ColorPaparazziShowkaseTestPreview(
    private val showkaseBrowserColor: ShowkaseBrowserColor
) : PaparazziShowkaseTestPreview {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(showkaseBrowserColor.color)
        )
    }

    override fun toString(): String =
        "${showkaseBrowserColor.colorGroup}${DELIM}${showkaseBrowserColor.colorName}"
}

class TypographyPaparazziShowkaseTestPreview(
    private val showkaseBrowserTypography: ShowkaseBrowserTypography
) : PaparazziShowkaseTestPreview {
    @Composable
    override fun Content() {
        BasicText(
            text = showkaseBrowserTypography.typographyName.replaceFirstChar {
                it.titlecase(Locale.getDefault())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding4x),
            style = showkaseBrowserTypography.textStyle
        )
    }

    override fun toString(): String =
        "${showkaseBrowserTypography.typographyGroup}${DELIM}${showkaseBrowserTypography.typographyName}"
}

/**
 * Wrapper class for Paparazzi's [DeviceConfig]. This was needed so that we could have a more
 * reasonable name for the test using the identifier that you pass to it. By default, the screenshots
 * are taken on a Pixel 5 device (as per Paparazzi's definition).
 */
data class PaparazziShowkaseDeviceConfig(
    val uniqueIdentifier: String = "Pixel5",
    val deviceConfig: DeviceConfig = DeviceConfig.PIXEL_5
) {
    override fun toString() = uniqueIdentifier
}

/**
 * Enum to represent the [Configuration.uiMode] that the screenshot execute under.
 */
enum class PaparazziShowkaseUIMode {
    DEFAULT,
    DARK
}
