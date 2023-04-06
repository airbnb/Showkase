// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import kotlin.collections.List

public val TestComposablegroupname: List<ShowkaseBrowserComponent> = 
    ParameterProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "group",
                    componentName = "name",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_processor_testing.WrapperClass_TestComposable_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable {
                        WrapperClass().TestComposable(text = previewParam)
                    }
                )
        }
        .toList()
