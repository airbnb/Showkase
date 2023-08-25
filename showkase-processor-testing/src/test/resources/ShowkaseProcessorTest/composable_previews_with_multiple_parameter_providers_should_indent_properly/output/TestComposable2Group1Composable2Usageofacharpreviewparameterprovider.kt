// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_browser_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import kotlin.collections.List

public val TestComposable2Group1Composable2Usageofacharpreviewparameterprovider:
    List<ShowkaseBrowserComponent> = 
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
                        """com.airbnb.android.showkase_browser_testing_TestComposable2_null_Group1_Composable2Usageofacharpreviewparameterprovider_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable { TestComposable2(c = previewParam) }
                )
        }
        .toList()
