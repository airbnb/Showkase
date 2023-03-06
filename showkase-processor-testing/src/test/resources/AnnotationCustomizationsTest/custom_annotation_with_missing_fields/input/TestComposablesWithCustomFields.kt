package ShowkaseProcessorTest.custom_annotation_with_missing_fields.input

import androidx.compose.runtime.Composable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
public annotation class CustomAnnotation(
    val label: String = "",
    val category: String = "",
    val style: String = "",
)

public class Composables {
    @CustomAnnotation("name1", "group1", style = "style1")
    @Composable
    public fun TestComposable1() {

    }

    @CustomAnnotation("name3", "group3")
    @Composable
    public fun TestComposable3() {

    }
}