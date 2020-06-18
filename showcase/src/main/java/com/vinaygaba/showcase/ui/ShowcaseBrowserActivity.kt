package com.vinaygaba.showcase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.models.ShowcaseComponentsProvider

class ShowcaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val groupedComponentsMap = getGroupedComponentsMap()
            when {
                groupedComponentsMap.isNotEmpty() -> {
                    ShowcaseBrowserApp(groupedComponentsMap)
                }
                else -> {
                    ShowcaseErrorScreen(
                        errorText = "There were no @Composable functions that were annotated with " +
                                "@Showcase. If you think this is a mistake, file an issue at " +
                                "https://github.com/vinaygaba/Showcase/issues"
                    )
                }
            }
        }
    }

    private fun getGroupedComponentsMap(): Map<String, List<ShowcaseCodegenMetadata>> {
        return try {
            val showcaseComponentProvider =
                Class.forName("$CODEGEN_PACKAGE_NAME.$AUTOGEN_CLASS_NAME").newInstance()
            val componentList = (showcaseComponentProvider as ShowcaseComponentsProvider).getShowcaseComponents()
            componentList.groupBy { it.group }
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    companion object {
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcase"
        const val AUTOGEN_CLASS_NAME = "ShowcaseCodegenComponents"
    }
}
