package com.airbnb.android.showkase_browser_testing

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(
    "Composable1 Usage of an integer preview parameter provider",
    "Group1"
)@Composable
internal fun TestComposable1(
    @PreviewParameter(provider = IntegerParameterProvider::class) n: Int
) {
    BasicText(text = "Test Composable1, parameterized with $n")
}

@ShowkaseComposable(
    "Composable2 Usage of a char preview parameter provider",
    "Group1"
)
@Composable
internal fun TestComposable2(
    @PreviewParameter(provider = CharParameterProvider::class) c: Char
) {
    BasicText(text = "Test Composable2, parameterized with $c")
}

internal class IntegerParameterProvider : PreviewParameterProvider<Int> {
    override val values = (0..9).asSequence()
}

internal class CharParameterProvider : PreviewParameterProvider<Char> {
    override val values = ('a'..'z').asSequence()
}
