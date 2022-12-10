package com.airbnb.android.showkasesample

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

val test: Sequence<ShowkaseBrowserComponent> = ParameterProvider()
.values
.iterator()
.asSequence()
.mapIndexed { index, previewParam ->
    ShowkaseBrowserComponent(
        group = "Text",
        componentName = "H6 Text Row",
        componentKDoc = "",
        componentKey =
        """com.airbnb.android.showkasesample_null_Text_H6TextRow_0_null_$index""",
        isDefaultStyle = false,
        component = @Composable { H6TextRowComponentPreview(person = previewParam)
        }
    )
}

fun main() {

}
