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
  numComposablesWithoutPreviewParameter = 2,
  numComposablesWithPreviewParameter = 0,
  numColors = 0,
  numTypography = 0
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
        ShowkaseBrowserComponent(
            group = "group1",
            componentName = "name1",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_group1_name1_null""",
            isDefaultStyle = false,
            component = @Composable { TestComposable1() }),
        ShowkaseBrowserComponent(
            group = "group2",
            componentName = "name2",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_group2_name2_null""",
            isDefaultStyle = false,
            component = @Composable { TestComposable2() })
      )

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

  public override fun getShowkaseComponents() = componentList

  public override fun getShowkaseColors() = colorList

  public override fun getShowkaseTypography() = typographyList
}
