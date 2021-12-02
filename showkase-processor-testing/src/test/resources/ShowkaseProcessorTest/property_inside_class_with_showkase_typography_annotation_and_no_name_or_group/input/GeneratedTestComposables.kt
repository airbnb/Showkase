package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.airbnb.android.showkase.annotation.ShowkaseTypography

class WrapperClass {
    @ShowkaseTypography
    val title = TextStyle(
        fontFamily = FontFamily.Cursive
    )
}