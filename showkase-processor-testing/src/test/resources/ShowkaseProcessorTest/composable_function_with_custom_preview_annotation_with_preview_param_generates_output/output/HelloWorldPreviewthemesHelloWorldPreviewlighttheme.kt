// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import kotlin.collections.List

public val HelloWorldPreviewthemesHelloWorldPreviewlighttheme: List<ShowkaseBrowserComponent> = 
    ParameterProvider()
        .values
        .iterator()
        .asSequence()
        .mapIndexed { index, previewParam ->
                ShowkaseBrowserComponent(
                    group = "themes",
                    componentName = "HelloWorldPreview - light theme",
                    componentKDoc = "",
                    componentKey =
                        """com.airbnb.android.showkase_processor_testing_HelloWorldPreview_null_themes_HelloWorldPreview-lighttheme_0_null_$index""",
                    isDefaultStyle = false,
                    component = @Composable { HelloWorldPreview(text = previewParam) }
                )
        }
        .toList()
