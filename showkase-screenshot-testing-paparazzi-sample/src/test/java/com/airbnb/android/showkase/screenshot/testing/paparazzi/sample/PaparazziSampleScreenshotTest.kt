package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import java.util.*

/**
 * Credit to Alex Vanyo for creating this sample in the Now In Android app by Google.
 * PR here - https://github.com/android/nowinandroid/pull/101. Modified the test from that PR to
 * my own needs for this sample.
 */
@RunWith(TestParameterInjector::class)
class PaparazziSampleScreenshotTest {

    object PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<TestPreview> {
            val metadata = Showkase.getMetadata()
            val components = metadata.componentList.map(::ComponentTestPreview)
            val colors = metadata.colorList.map(::ColorTestPreview)
            val typography = metadata.typographyList.map(::TypographyTestPreview)

            return components + colors + typography
        }
    }

    enum class BaseDeviceConfig(
        val deviceConfig: DeviceConfig,
    ) {
        NEXUS_5(DeviceConfig.NEXUS_5),
        PIXEL_C(DeviceConfig.PIXEL_C),
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
    )

    @Test
    fun preview_tests(
        @TestParameter(valuesProvider = PreviewProvider::class) componentTestPreview: TestPreview,
        @TestParameter baseDeviceConfig: BaseDeviceConfig,
        @TestParameter(value = ["1.0", "1.5"]) fontScale: Float
    ) {
        paparazzi.unsafeUpdateConfig(
            baseDeviceConfig.deviceConfig.copy(
                softButtons = false,
            )
        )
        paparazzi.snapshot {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = fontScale
                )
            ) {
                Box {
                    componentTestPreview.Content()
                }
            }
        }
    }
}

interface TestPreview {
    @Composable
    fun Content()
}

class ComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : TestPreview {
    @Composable
    override fun Content() = showkaseBrowserComponent.component()
    override fun toString(): String = showkaseBrowserComponent.componentKey
}

class ColorTestPreview(
    private val showkaseBrowserColor: ShowkaseBrowserColor
) : TestPreview {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(showkaseBrowserColor.color)
        )
    }

    override fun toString(): String = "${showkaseBrowserColor.colorGroup}_${showkaseBrowserColor.colorName}"
}

class TypographyTestPreview(
    private val showkaseBrowserTypography: ShowkaseBrowserTypography
) : TestPreview {
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

    override fun toString(): String = "${showkaseBrowserTypography.typographyGroup}_${showkaseBrowserTypography.typographyName}"
}
