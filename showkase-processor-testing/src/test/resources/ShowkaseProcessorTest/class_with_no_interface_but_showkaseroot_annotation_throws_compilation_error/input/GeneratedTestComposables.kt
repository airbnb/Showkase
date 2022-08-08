import com.airbnb.android.showkase.annotation.ShowkaseComposable
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule

@ShowkaseComposable("name", "group")
@Composable
public fun TestComposable() {
    
}

@ShowkaseRoot
public class TestClass() {

}