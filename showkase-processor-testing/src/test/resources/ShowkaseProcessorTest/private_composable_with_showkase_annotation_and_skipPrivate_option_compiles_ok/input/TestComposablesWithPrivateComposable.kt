package ShowkaseProcessorTest.private_composable_with_showkase_annotation_and_skipPrivate_option_compiles_ok.input

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

public class Composables {
    @ShowkaseComposable("name1", "group1")
    @Composable
    public fun TestComposable1() {

    }

    @ShowkaseComposable("name2", "group2")
    @Composable
    private fun TestComposable2() {

    }

    @ShowkaseComposable("name3", "group3")
    @Composable
    public fun TestComposable3() {

    }
}