package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.airbnb.android.showkase.annotation.ShowkaseTypography

@ShowkaseComposable("name", "component")
@Composable
public fun TestComposable() {
    
}

public class WrapperClass {
    @ShowkaseTypography("name", "typography")
    public val title: TextStyle = TextStyle(
        fontFamily = FontFamily.Cursive
    )
}
