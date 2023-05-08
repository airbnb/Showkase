// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing_my_very_long_name

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import kotlin.collections.List

public val TestComposablegroupname: List<ShowkaseBrowserComponent> = 
    MyVeryLongPackageNameViewStateSomethingSomethingFunnyStuffProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "group",
                    componentName = "name",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_processor_testing_my_very_long_name.WrapperClass_TestComposable_com.airbnb.android.showkase_processor_testing_my_very_long_name.WrapperClass_group_name_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable {
                        WrapperClass.TestComposable(text = previewParam)
                    }
                )
        }
        .toList()
