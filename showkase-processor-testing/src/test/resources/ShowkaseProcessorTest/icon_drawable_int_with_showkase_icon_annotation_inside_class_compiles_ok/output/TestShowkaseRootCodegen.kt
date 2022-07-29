// This is an auto-generated file. Please do not edit/modify this file.
package ShowkaseProcessorTest.icon_image_vector_with_showkase_icon_annotation_inside_object_compiles_ok.input

import com.airbnb.android.showkase.`annotation`.ShowkaseRootCodegen
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserIcon
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.models.ShowkaseProvider
import com.airbnb.android.showkase_processor_testing.Composables
import kotlin.collections.List

@ShowkaseRootCodegen(
  numComposablesWithoutPreviewParameter = 0,
  numComposablesWithPreviewParameter = 0,
  numColors = 0,
  numTypography = 0,
  numIcons = 1,
)
public class TestShowkaseRootCodegen : ShowkaseProvider {
  public val componentList: List<ShowkaseBrowserComponent> =
      mutableListOf<ShowkaseBrowserComponent>()

  public val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

  public val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

  public val iconList: List<ShowkaseBrowserIcon> = listOf<ShowkaseBrowserIcon>(
        ShowkaseBrowserIcon(
            group = "group",
            name = "name",
            kDoc = "",
            drawableRes = Composables().verifiedUserIcon,)
      )

  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> = componentList

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> = colorList

  public override fun getShowkaseIcons(): List<ShowkaseBrowserIcon> = iconList

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> = typographyList
}
