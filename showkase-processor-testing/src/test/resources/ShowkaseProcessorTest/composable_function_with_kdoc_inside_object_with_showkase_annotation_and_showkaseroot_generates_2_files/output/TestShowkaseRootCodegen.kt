// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 1,
  numComposablesWithPreviewParameter = 0,
  numColors = 0,
  numTypography = 0
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
        ShowkaseBrowserComponent(
            group = "group",
            componentName = "name",
            componentKDoc = """
            |This component shows some static text in cursive text style.
            |
            | Example usage:
            |
            | ```
            | @Composable
            | fun MyComposable() { CursiveTextComponentPreview() }
            | ```
            """.trimMargin(),
            componentKey =
                """com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name_null""",
            isDefaultStyle = false,
            component = @Composable {
                WrapperClass.TestComposable()
            })
      )

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
