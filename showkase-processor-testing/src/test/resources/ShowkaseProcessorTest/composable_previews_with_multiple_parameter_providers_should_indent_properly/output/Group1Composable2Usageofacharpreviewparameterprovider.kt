// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase_browser_testing.CharParameterProvider
import com.airbnb.android.showkase_browser_testing.TestComposable2
import kotlin.collections.List

public val Group1Composable2Usageofacharpreviewparameterprovider: List<ShowkaseBrowserComponent> = 
    CharParameterProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "Group1",
                    componentName = "Composable2 Usage of a char preview parameter provider",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_browser_testing_null_Group1_Composable2Usageofacharpreviewparameterprovider_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable { TestComposable2(c = previewParam) }
                )
        }
        .toList()
