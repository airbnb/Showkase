package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable

@ShowkaseComposable("name", "component")
@Composable
public fun TestComposable() {
    
}

@ShowkaseTypography("name", "typography")
public val title: TextStyle = TextStyle(
    fontFamily = FontFamily.Cursive
)
