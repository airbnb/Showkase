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
  numComposablesWithoutPreviewParameter = 4,
  numComposablesWithPreviewParameter = 0,
  numColors = 0,
  numTypography = 0,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>(
        ShowkaseBrowserComponent(
            group = "Custom Previews",
            componentName = "CustomInternalAnnotationPreviewCombined - Custom Preview One First",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_CustomPreviews_CustomInternalAnnotationPreviewCombined-CustomPreviewOneFirst_0_null""",
            isDefaultStyle = false,
            component = @Composable { CustomInternalAnnotationPreviewCombined() }),
        ShowkaseBrowserComponent(
            group = "Custom Previews",
            componentName = "CustomInternalAnnotationPreviewCombined - Custom Preview One Second",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_CustomPreviews_CustomInternalAnnotationPreviewCombined-CustomPreviewOneSecond_1_null""",
            isDefaultStyle = false,
            component = @Composable { CustomInternalAnnotationPreviewCombined() }),
        ShowkaseBrowserComponent(
            group = "Custom Previews",
            componentName = "CustomInternalAnnotationPreviewCombined - Custom Preview Two First",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_CustomPreviews_CustomInternalAnnotationPreviewCombined-CustomPreviewTwoFirst_0_null""",
            isDefaultStyle = false,
            component = @Composable { CustomInternalAnnotationPreviewCombined() }),
        ShowkaseBrowserComponent(
            group = "Custom Previews",
            componentName = "CustomInternalAnnotationPreviewCombined - Custom Preview Two Second",
            componentKDoc = "",
            componentKey =
                """com.airbnb.android.showkase_processor_testing_null_CustomPreviews_CustomInternalAnnotationPreviewCombined-CustomPreviewTwoSecond_1_null""",
            isDefaultStyle = false,
            component = @Composable { CustomInternalAnnotationPreviewCombined() })
      )

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
