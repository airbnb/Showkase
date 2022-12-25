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
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import java.util.Locale

interface PaparazziShowkaseScreenshotTest {

    interface CompanionObject {
        fun providePaparazzi(): Paparazzi = Paparazzi(maxPercentDifference = 0.0)

        fun deviceConfigs(): List<PaparazziShowkaseDeviceConfig> = listOf(
            PaparazziShowkaseDeviceConfig("Pixel", DeviceConfig.PIXEL)
        )

        fun layoutDirections(): List<LayoutDirection> = listOf(LayoutDirection.Ltr)

        fun uiModes(): List<PaparazziShowkaseUIMode> = listOf(PaparazziShowkaseUIMode.DEFAULT)
    }

    fun takePaparazziSnapshot(
        paparazzi: Paparazzi,
        testPreview: PaparazziShowkaseTestPreview,
        direction: LayoutDirection,
        mode: PaparazziShowkaseUIMode
    ) {
        paparazzi.snapshot{
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
                LocalOnBackPressedDispatcherOwner provides object: OnBackPressedDispatcherOwner {
                    override fun getLifecycle() = lifecycleOwner.lifecycle

                    override fun getOnBackPressedDispatcher() = OnBackPressedDispatcher()
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

class ComponentPaparazziShowkaseTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : PaparazziShowkaseTestPreview {
    @Composable
    override fun Content() = showkaseBrowserComponent.component()
    override fun toString(): String =
        "${showkaseBrowserComponent.group}_${showkaseBrowserComponent.componentName}_" +
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
        "${showkaseBrowserColor.colorGroup}_${showkaseBrowserColor.colorName}"
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
        "${showkaseBrowserTypography.typographyGroup}_${showkaseBrowserTypography.typographyName}"
}

data class PaparazziShowkaseDeviceConfig(
    val uniqueIdentifier: String,
    val deviceConfig: DeviceConfig
) {
    override fun toString() = uniqueIdentifier
}

enum class PaparazziShowkaseUIMode {
    DEFAULT,
    DARK
}
