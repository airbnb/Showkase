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
  numTypography = 1
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
        ShowkaseBrowserComponent(
            group = "component",
            order = 2147483647,
            componentName = "name",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_component_name_null""",
            isDefaultStyle = false,
            component = @Composable { TestComposable() })
      )

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
        ShowkaseBrowserTypography(
            typographyGroup = "typography",
            typographyName = "name",
            order = 2147483647,
            typographyKDoc = "",
            textStyle = title)
      )

  public override fun getShowkaseComponents() = componentList

  public override fun getShowkaseColors() = colorList

  public override fun getShowkaseTypography() = typographyList
}
