// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import com.airbnb.android.showkase_browser_testing.TestComposable1Group1Composable1Usageofanintegerpreviewparameterprovider
import com.airbnb.android.showkase_browser_testing.TestComposable2Group1Composable2Usageofacharpreviewparameterprovider
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 0,
  numComposablesWithPreviewParameter = 2,
  numColors = 0,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> {

    return mutableListOf<ShowkaseBrowserComponent>(
    ).apply {
        addAll(TestComposable1Group1Composable1Usageofanintegerpreviewparameterprovider)
        addAll(TestComposable2Group1Composable2Usageofacharpreviewparameterprovider)
    }
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
