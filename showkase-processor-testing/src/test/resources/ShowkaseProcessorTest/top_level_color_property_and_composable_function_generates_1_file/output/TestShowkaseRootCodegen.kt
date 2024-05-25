// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 1,
  numComposablesWithPreviewParameter = 0,
  numColors = 1,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> {

    return listOf<ShowkaseBrowserComponent>(
        TestComposablecomponentname,
    )
  }

  override fun getShowkaseColors(): List<ShowkaseBrowserColor> {

    return listOf<ShowkaseBrowserColor>(
        redcolorname,
    )
  }

  override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> {

    return listOf<ShowkaseBrowserTypography>(
    )
  }
}
