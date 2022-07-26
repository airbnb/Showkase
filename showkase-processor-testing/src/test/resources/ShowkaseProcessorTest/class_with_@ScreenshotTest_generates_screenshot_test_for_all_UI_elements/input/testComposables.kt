package com.airbnb.android.showkase_processor_testing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseIcon
import com.airbnb.android.showkase.annotation.ShowkaseTypography

@ShowkaseComposable(name = "name1", group = "group1")
@Composable
public fun TestComposable1() {
    
}

@ShowkaseComposable(name = "name2", group = "group2")
@Composable
public fun TestComposable2() {
    
}

@ShowkaseColor("name", "color")
public val red: Color = Color(0xffff0000)

@ShowkaseTypography("name", "typography")
public val title: TextStyle = TextStyle(
    fontFamily = FontFamily.Cursive
)

public object WrappedClass {

    @ShowkaseIcon("name", "icon")
    public val icon: ImageVector = Icons.Filled.Close
}
