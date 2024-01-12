package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.android.ide.common.rendering.api.SessionParams
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class ComponentPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) {
    val content: @Composable () -> Unit = showkaseBrowserComponent.component
    override fun toString(): String =
        showkaseBrowserComponent.functionName + "-" + showkaseBrowserComponent.group + "_" + showkaseBrowserComponent.componentName
}

@RunWith(TestParameterInjector::class)
class ComposePaparazziTest {
    object PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<ComponentPreview> {
            return try {
                val metadata = Showkase.getMetadata()
                val componentList = metadata.componentList
                componentList
                    .filterGenerateScreenshotComponents()
                    .map(::ComponentPreview)
            } catch (error: Throwable) {
                emptyList()
            }
        }

        private fun List<ShowkaseBrowserComponent>.filterGenerateScreenshotComponents(): List<ShowkaseBrowserComponent> {
            return filter {
                return@filter (it.generateScreenshot)
            }
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        showSystemUi =  false,
        deviceConfig = DeviceConfig.PIXEL_5.copy(
            softButtons = false,
            locale = "en"),
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    @Test
    fun preview_tests(
        @TestParameter(valuesProvider = PreviewProvider::class) componentPreview: ComponentPreview,
        @TestParameter(value = ["1.0"]) fontScale: Float
    ) {
        paparazzi.snapshot() {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = fontScale
                )
            ) {
                Box {
                    componentPreview.content()
                }
            }
        }
    }
}