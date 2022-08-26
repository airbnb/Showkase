// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import com.airbnb.android.showkase_browser_testing.CharParameterProvider
import com.airbnb.android.showkase_browser_testing.IntegerParameterProvider
import com.airbnb.android.showkase_browser_testing.TestComposable1
import com.airbnb.android.showkase_browser_testing.TestComposable2
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 0,
  numComposablesWithPreviewParameter = 2,
  numColors = 0,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>().apply {
          IntegerParameterProvider()
              .values
              .iterator()
              .asSequence()
              .forEachIndexed { index, previewParam ->
                  add(
                      ShowkaseBrowserComponent(
                          group = "Group1",
                          componentName =
                              "Composable1 Usage of an integer preview parameter provider",
                          componentKDoc = "",
                          componentKey =
                              """com.airbnb.android.showkase_browser_testing_null_Group1_Composable1Usageofanintegerpreviewparameterprovider_null_$index""",
                          isDefaultStyle = false,
                          component = @Composable { TestComposable1(n = previewParam) }
                      )
                  )
              }
          CharParameterProvider()
              .values
              .iterator()
              .asSequence()
              .forEachIndexed { index, previewParam ->
                  add(
                      ShowkaseBrowserComponent(
                          group = "Group1",
                          componentName = "Composable2 Usage of a char preview parameter provider",
                          componentKDoc = "",
                          componentKey =
                              """com.airbnb.android.showkase_browser_testing_null_Group1_Composable2Usageofacharpreviewparameterprovider_null_$index""",
                          isDefaultStyle = false,
                          component = @Composable { TestComposable2(c = previewParam) }
                      )
                  )
              }
      }

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
