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
  override fun getShowkaseComponents(): List<ShowkaseBrowserComponent> {

    return listOf<ShowkaseBrowserComponent>(
        TestComposable1group1name1,
        TestComposable2group2name2,
    )
  }

  override fun getShowkaseColors(): List<ShowkaseBrowserColor> {

    return listOf<ShowkaseBrowserColor>(
        redcolorname,
    )
  }

  override fun getShowkaseTypography(): List<ShowkaseBrowserTypography> {

    return listOf<ShowkaseBrowserTypography>(
        titletypographyname,
    )
  }
}
