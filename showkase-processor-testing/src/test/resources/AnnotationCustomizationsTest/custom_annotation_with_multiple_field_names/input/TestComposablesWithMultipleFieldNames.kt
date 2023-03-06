package ShowkaseProcessorTest.custom_annotation_with_multiple_field_names.input

import androidx.compose.runtime.Composable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
public annotation class CustomAnnotation(
    val label: String = "",
    val secondLabel: String = "",
    val category: String = "",
    val secondCategory: String = "",
    val style: String = "",
    val secondStyle: String = "",
    val widthDp: Int = -1,
    val heightDp: Int = -1,
    val skip: Boolean = false,
    val defaultStyle: Boolean = false
)

public class Composables {
    @CustomAnnotation(secondLabel = "name1", secondCategory = "group1", secondStyle = "style1")
    @Composable
    public fun TestComposable1() {

    }

    @CustomAnnotation(label = "name3",  secondLabel = "unused", secondCategory = "group3")
    @Composable
    public fun TestComposable3() {

    }
}