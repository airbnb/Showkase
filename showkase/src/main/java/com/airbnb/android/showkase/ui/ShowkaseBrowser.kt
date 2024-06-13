package com.airbnb.android.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
import com.airbnb.android.showkase.models.ShowkaseProvider

private const val AUTOGEN_CLASS_NAME = "Codegen"

/**
 * composable entry point for Showkase
 *
 * @param classKey The name of the class with [@ShowkaseRoot] annotation
 *
 */
@Composable
fun ShowkaseBrowser(
    classKey: String,
) {
    val (
        groupedComponentsList,
        groupedColorsList,
        groupedTypographyList
    ) = getShowkaseProviderElements(classKey)

    val showkaseBrowserScreenMetadata =
        remember { mutableStateOf(ShowkaseBrowserScreenMetadata()) }
    when {
        groupedComponentsList.isNotEmpty() || groupedColorsList.isNotEmpty() ||
                groupedTypographyList.isNotEmpty() -> {
            ShowkaseBrowserApp(
                groupedComponentsList.groupBy { it.group },
                groupedColorsList.groupBy { it.colorGroup },
                groupedTypographyList.groupBy { it.typographyGroup },
                showkaseBrowserScreenMetadata
            )
        }

        else -> {
            ShowkaseErrorScreen(
                errorText = "There were no elements that were annotated with either " +
                        "@ShowkaseComposable, @ShowkaseTypography or @ShowkaseColor. If " +
                        "you think this is a mistake, file an issue at " +
                        "https://github.com/airbnb/Showkase/issues"
            )
        }
    }
}

private fun getShowkaseProviderElements(
    classKey: String,
): ShowkaseElementsMetadata {
    return try {
        val showkaseComponentProvider =
            Class.forName("$classKey${AUTOGEN_CLASS_NAME}").getDeclaredConstructor().newInstance()

        val showkaseMetadata = (showkaseComponentProvider as ShowkaseProvider).metadata()

        ShowkaseElementsMetadata(
            componentList = showkaseMetadata.componentList,
            colorList = showkaseMetadata.colorList,
            typographyList = showkaseMetadata.typographyList
        )
    } catch (exception: ClassNotFoundException) {
        ShowkaseElementsMetadata()
    }
}