// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 0,
  numComposablesWithPreviewParameter = 1,
  numColors = 0,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
      ).apply {
          addAll(groupname)
      }

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
      )

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
      )

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
