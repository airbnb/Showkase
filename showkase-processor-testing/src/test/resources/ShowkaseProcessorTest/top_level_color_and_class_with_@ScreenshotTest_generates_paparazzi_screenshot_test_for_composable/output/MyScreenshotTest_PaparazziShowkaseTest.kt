// This is an auto-generated file. Please do not edit/modify this file.
import androidx.compose.ui.unit.LayoutDirection
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.screenshot.testing.paparazzi.ColorPaparazziShowkaseTestPreview
import com.airbnb.android.showkase.screenshot.testing.paparazzi.ComponentPaparazziShowkaseTestPreview
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseDeviceConfig
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseTestPreview
import com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseUIMode
import com.airbnb.android.showkase.screenshot.testing.paparazzi.TypographyPaparazziShowkaseTestPreview
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameter.TestParameterValuesProvider
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import getMetadata
import kotlin.collections.List
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
public class MyScreenshotTest_PaparazziShowkaseTest : MyScreenshotTest() {
  @get:Rule
  public val paparazzi: Paparazzi = providePaparazzi()

  @Test
  public fun test_previews(
    @TestParameter(valuesProvider = PaparazziShowkasePreviewProvider::class)
        elementPreview: PaparazziShowkaseTestPreview,
    @TestParameter(valuesProvider = PaparazziShowkaseDeviceConfigProvider::class)
        config: PaparazziShowkaseDeviceConfig,
    @TestParameter(valuesProvider = PaparazziShowkaseLayoutDirectionProvider::class)
        direction: LayoutDirection,
    @TestParameter(valuesProvider = PaparazziShowkaseUIModeProvider::class)
        uiMode: PaparazziShowkaseUIMode,
  ) {
    paparazzi.unsafeUpdateConfig(config.deviceConfig.copy(softButtons = false))
    takePaparazziSnapshot(paparazzi, elementPreview, direction, uiMode)
  }

  private object PaparazziShowkasePreviewProvider : TestParameter.TestParameterValuesProvider {
    override fun provideValues(): List<PaparazziShowkaseTestPreview> {
      val metadata = Showkase.getMetadata()
      val components = metadata.componentList.map(::ComponentPaparazziShowkaseTestPreview)
      val colors = metadata.colorList.map(::ColorPaparazziShowkaseTestPreview)
      val typography = metadata.typographyList.map(::TypographyPaparazziShowkaseTestPreview)
      return components + colors + typography
    }
  }

  private object PaparazziShowkaseDeviceConfigProvider : TestParameter.TestParameterValuesProvider {
    override fun provideValues(): List<PaparazziShowkaseDeviceConfig> = deviceConfigs()
  }

  private object PaparazziShowkaseLayoutDirectionProvider :
      TestParameter.TestParameterValuesProvider {
    override fun provideValues(): List<LayoutDirection> = layoutDirections()
  }

  private object PaparazziShowkaseUIModeProvider : TestParameter.TestParameterValuesProvider {
    override fun provideValues(): List<PaparazziShowkaseUIMode> = uiModes()
  }
}
