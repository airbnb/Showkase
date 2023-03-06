package ShowkaseProcessorTest.custom_annotation_missing.input

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
public annotation class CustomAnnotation(
    val name: String = "",
    val group: String = "",
    val styleName: String = "",
    val additionalField: String = "",
//    val componentName: String = "",
//    val collection: String = "",
//    val testName: String = "",
//    val variantName: String = "",
    val widthDp: Int = -1,
    val heightDp: Int = -1,
    val skip: Boolean = false,
    val defaultStyle: Boolean = false
)

public class Composables {
    @CustomAnnotation("name1", "group1", additionalField = "custom field")
    @Composable
    public fun TestComposable1() {

    }

    @ShowkaseComposable("name1", "group1")
    @Composable
    public fun TestComposable2() {

    }

    @CustomAnnotation("name3", "group3")
    @Composable
    public fun TestComposable3() {

    }
}