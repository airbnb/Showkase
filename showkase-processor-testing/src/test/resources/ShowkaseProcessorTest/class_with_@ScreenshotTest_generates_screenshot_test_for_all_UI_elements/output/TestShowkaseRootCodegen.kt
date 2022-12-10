// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 2,
  numComposablesWithPreviewParameter = 0,
  numColors = 1,
  numTypography = 1,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
        comairbnbandroidshowkaseprocessortestinggroup1name1,
        comairbnbandroidshowkaseprocessortestinggroup2name2,
      )

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
        comairbnbandroidshowkaseprocessortestingcolorname,
      )

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
        comairbnbandroidshowkaseprocessortestingtypographyname,
      )

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
