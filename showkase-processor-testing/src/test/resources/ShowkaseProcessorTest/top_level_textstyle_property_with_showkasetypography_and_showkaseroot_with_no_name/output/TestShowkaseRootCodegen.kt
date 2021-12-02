// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 0,
  numComposablesWithPreviewParameter = 0,
  numColors = 0,
  numTypography = 1
)
class TestShowkaseRootCodegen : ShowkaseProvider {
  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()

  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
        ShowkaseBrowserTypography(
            typographyGroup = "group",
            typographyName = "Title",
            typographyKDoc = "",
            textStyle = title)
      )

  override fun getShowkaseComponents() = componentList

  override fun getShowkaseColors() = colorList

  override fun getShowkaseTypography() = typographyList
}
