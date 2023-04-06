// This is an auto-generated file. Please do not edit/modify this file.
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
  public override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> {

    return listOf<ShowkaseBrowserComponent>(
        group1name1,
        group2name2,
    )
  }

  public override fun getShowkaseColors(): List<ShowkaseBrowserColor> {

    return listOf<ShowkaseBrowserColor>(
        colorname,
    )
  }

  public override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> {

    return listOf<ShowkaseBrowserTypography>(
        typographyname,
    )
  }
}
