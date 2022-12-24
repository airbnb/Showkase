//package com.airbnb.android.showkase.screenshot.testing.paparazzi
//
//import androidx.compose.ui.unit.LayoutDirection
//import app.cash.paparazzi.DeviceConfig
//import com.airbnb.android.showkase.models.Showkase
//import com.airbnb.android.showkase.screenshot.testing.paparazzi.sample.MyPaparazziShowkaseScreenshotTest
//import com.airbnb.android.showkase.screenshot.testing.paparazzi.sample.getMetadata
//import com.google.testing.junit.testparameterinjector.TestParameter
//import com.google.testing.junit.testparameterinjector.TestParameterInjector
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(TestParameterInjector::class)
//class GeneratedMyPaparazziFileShowkase: PaparazziShowkaseScreenshotTest() {
//    @get:Rule
//    val paparazzi = providePaparazzi()
//
//    object ShowkasePaparazziPreviewProvider: TestParameter.TestParameterValuesProvider {
//        override fun provideValues(): List<PaparazziShowkaseTestPreview> {
//            val metadata = Showkase.getMetadata()
//            val components = metadata.componentList.map(::ComponentPaparazziShowkaseTestPreview)
//            val colors = metadata.colorList.map(::ColorPaparazziShowkaseTestPreview)
//            val typography = metadata.typographyList.map(::TypographyPaparazziShowkaseTestPreview)
//
//            return components + colors + typography
//
//        }
//    }
//
//    object ShowkasePaparazziDeviceConfigProvider: TestParameter.TestParameterValuesProvider {
//        override fun provideValues(): List<DeviceConfig> {
//            return deviceConfigs()
//        }
//    }
//
//    object ShowkasePaparazziLayoutDirectionProvider: TestParameter.TestParameterValuesProvider {
//        override fun provideValues(): List<LayoutDirection> {
//            return layoutDirections()
//        }
//    }
//
//    @Test
//    fun test_previews(
//        @TestParameter(valuesProvider = ShowkasePaparazziPreviewProvider::class) elementPreview: PaparazziShowkaseTestPreview,
//        @TestParameter(valuesProvider = ShowkasePaparazziDeviceConfigProvider::class) config: DeviceConfig,
//    ) {
//        paparazzi.unsafeUpdateConfig(config.copy(softButtons = false))
//        takePaparazziSnapshot(paparazzi, elementPreview)
//    }
//}