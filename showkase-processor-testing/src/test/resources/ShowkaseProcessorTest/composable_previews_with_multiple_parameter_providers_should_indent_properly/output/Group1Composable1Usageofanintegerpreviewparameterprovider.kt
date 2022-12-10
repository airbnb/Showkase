// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase_browser_testing.IntegerParameterProvider
import com.airbnb.android.showkase_browser_testing.TestComposable1
import kotlin.collections.List

public val Group1Composable1Usageofanintegerpreviewparameterprovider: List<ShowkaseBrowserComponent>
    = 
    IntegerParameterProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "Group1",
                    componentName = "Composable1 Usage of an integer preview parameter provider",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_browser_testing_null_Group1_Composable1Usageofanintegerpreviewparameterprovider_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable { TestComposable1(n = previewParam) }
                )
        }
        .toList()
