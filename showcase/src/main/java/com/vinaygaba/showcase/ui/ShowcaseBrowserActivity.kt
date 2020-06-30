package com.vinaygaba.showcase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.state
import androidx.ui.core.setContent
import com.vinaygaba.showcase.models.ShowcaseBrowserComponent
import com.vinaygaba.showcase.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.showcase.models.ShowcaseComponentsProvider

class ShowcaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val classKey = intent.extras?.getString(SHOWCASE_ROOT_MODULE) ?: return 
        setContent {
            val groupedComponentsMap = getGroupedComponentsMap(classKey)
            val showcaseBrowserScreenMetadata = state { ShowcaseBrowserScreenMetadata() }
            when {
                groupedComponentsMap.isNotEmpty() -> {
                    ShowcaseBrowserApp(groupedComponentsMap, showcaseBrowserScreenMetadata)
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

    private fun getGroupedComponentsMap(classKey: String): Map<String, List<ShowcaseBrowserComponent>> {
        return try {
            val showcaseComponentProvider = Class.forName("$classKey$AUTOGEN_CLASS_NAME").newInstance()
            
            val componentList = (showcaseComponentProvider as ShowcaseComponentsProvider).getShowcaseComponents()
            componentList.groupBy { it.group }
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    companion object {
        const val SHOWCASE_ROOT_MODULE = "SHOWCASE_ROOT_MODULE"
        private const val AUTOGEN_CLASS_NAME = "CodegenComponents"
    }
}
