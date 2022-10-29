@file:Suppress("PackageNaming")
package com.airbnb.android.showkase_browser_testing

import android.content.res.Configuration
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.google.android.material.composethemeadapter.MdcTheme
import com.vinaygaba.showkase_browser_testing_submodule.CustomPreviewAnnotation
import com.vinaygaba.showkase_browser_testing_submodule.two.SquarePreview

@ShowkaseComposable("Composable1", "Group1")
@Composable
fun TestComposable1() {
    MdcTheme {
        BasicText(text = "Test Composable1")
    }
}

@ShowkaseComposable("Composable2", "Group1")
@Composable
fun TestComposable2() {
    BasicText(text = "Test Composable2")
}

@ShowkaseComposable("Composable3", "Group2")
@Composable
fun TestComposable3() {
    BasicText(text = "Test Composable3")
}

@ShowkaseComposable("Composable4", "Group3")
@Composable
fun TestComposable4() {
    BasicText(text = "Test Composable4")
}

@ShowkaseComposable(
    "Composable6 Button Component wih a lot of extra stuff like spacing and such",
    "Group3"
)
@Composable
fun TestComposable6() {
    BasicText(text = "Test Composable6")
}

class WrapperComposableClass {
    @ShowkaseComposable("Composable5", "Group3")
    @Composable
    fun TestComposable5() {
        BasicText(text = "Test Composable5")
    }
}

@ShowkaseComposable("Composable7", "A Stacked Annotations")
@ShowkaseComposable("Composable8", "A Stacked Annotations")
@Composable
fun TestComposable7() {
    BasicText(text = "Test Composable7and8")
}

@Preview("Composable9", "A Stacked Annotations")
@Preview("Composable10", "A Stacked Annotations")
@Composable
fun TestComposable8() {
    BasicText(text = "Test Composable9and10")
}

@Preview(
    name = "Custom Text Light",
    group = "Button",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Custom Text Dark",
    group = "Button",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class CustomButtonPreview

@CustomButtonPreview
@Composable
fun PreviewCustomText() {
    BasicText(text = "MultiPreviewAnnotation!")
}

@SquarePreview
@CustomPreviewAnnotation
@Composable
fun PreviewCustomTextWithCustomAnnotationFromOtherModule() {
    BasicText(text = "MultiPreviewAnnotation from other module!")
}


@Preview(
    name = "Custom Text Dark From Combined Previews",
    group = "Chips",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@CustomButtonPreview
@CustomPreviewAnnotation
@Composable
fun PreviewCombinedCustomAnnotation() {
    BasicText(text = "Combined multi preview annotation test")
}

@ShowkaseRoot
class MyRootModule: ShowkaseRootModule
