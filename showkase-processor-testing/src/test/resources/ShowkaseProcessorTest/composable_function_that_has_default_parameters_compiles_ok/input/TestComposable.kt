package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "name", group = "group")
@Composable
public fun TestComposableWithDefaultParameters(
    age: Int = 5,
    @PreviewParameter(provider = NewParameterProvider::class) bankHeader: BankHeader
) {

}

public class NewParameterProvider : PreviewParameterProvider<BankHeader> {
    override val values: Sequence<BankHeader>
        get() = sequenceOf(
            BankHeader("Citi", 12),
            BankHeader("Goldman Sachs", 20)
        )

    override val count: Int
        get() = super.count

}

public data class BankHeader(
    public val name: String,
    public val age: Int,
)