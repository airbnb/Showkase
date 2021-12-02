package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

@ShowkaseComposable("name", "group")
@Composable
fun TestComposable() {
    
}

@ShowkaseTypography("name", "group")
val title = TextStyle(
    fontFamily = FontFamily.Cursive
)