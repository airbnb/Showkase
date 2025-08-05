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
  numColors = 0,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> {

    return listOf<ShowkaseBrowserComponent>(
        TestComposable1group1name1,
        TestComposable2group2name2,
    )
  }

  override fun getShowkaseColors(): List<ShowkaseBrowserColor> {

    return listOf<ShowkaseBrowserColor>(
    )
  }

  override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> {

    return listOf<ShowkaseBrowserTypography>(
    )
  }
}
