
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

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
