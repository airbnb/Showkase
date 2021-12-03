package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

@ShowkaseComposable(name = "name1", group = "group1")
@Composable
fun TestComposable1() {
    
}

@ShowkaseComposable(name = "name2", group = "group2")
@Composable
fun TestComposable2() {
    
}

@ShowkaseColor("name", "group")
val red = Color(0xffff0000)

@ShowkaseTypography("name", "group")
val title = TextStyle(
    fontFamily = FontFamily.Cursive
)