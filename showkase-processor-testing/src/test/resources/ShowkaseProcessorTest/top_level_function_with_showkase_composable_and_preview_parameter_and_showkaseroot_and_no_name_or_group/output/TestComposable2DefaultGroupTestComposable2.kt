// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import kotlin.collections.List

public val TestComposable2DefaultGroupTestComposable2: List<ShowkaseBrowserComponent> = 
    ParameterProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "Default Group",
                    componentName = "TestComposable2",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_processor_testing_TestComposable2_null_DefaultGroup_TestComposable2_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable { TestComposable2(text = previewParam) }
                )
        }
        .toList()
