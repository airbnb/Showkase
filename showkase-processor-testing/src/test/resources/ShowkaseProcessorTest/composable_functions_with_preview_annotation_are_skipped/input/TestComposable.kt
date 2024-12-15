package ShowkaseProcessorTest.composable_functions_with_preview_annotation_are_skipped.input

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.ui.tooling.preview.Preview

@ShowkaseComposable(name = "name1", group = "group1")
@Composable
public fun TestShowkaseComposable1() {

}

@ShowkaseComposable(name = "name2", group = "group2")
@Composable
public fun TestShowkaseComposable2() {

}

@Preview(name = "name3", group = "group3")
@Composable
public fun TestPreviewComposable3() {

}

@Preview(name = "name4", group = "group4")
@ShowkaseComposable(name = "name4", group = "group4")
@Composable
public fun TestShowkaseComposable4() {

}
