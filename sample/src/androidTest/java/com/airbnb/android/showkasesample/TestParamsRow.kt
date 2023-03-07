package com.airbnb.android.showkasesample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(
    name = "test @PreviewParameter code generation",
    group = "test-preview-param-group"
)
@Composable
@Preview
fun TestParamsRow(@PreviewParameter(SamplePreviewParameterProvider::class) arg: String) {
    Box(modifier = Modifier.background(color = Color.DarkGray)) {
        Text(modifier = Modifier.padding(horizontal = 8.dp), text = arg, color = Color.White)
    }
}

class SamplePreviewParameterProvider : PreviewParameterProvider<String> {
    private val caseList: List<String> = listOf("error case", "success case", "not logged in case")
    override val count: Int = caseList.count()
    override val values: Sequence<String> = caseList.asSequence()
}