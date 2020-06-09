package com.vinaygaba.showcase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata

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
            val showcaseComponentsClass = Class.forName("$CODEGEN_PACKAGE_NAME.$AUTOGEN_CLASS_NAME")
            val componentList = showcaseComponentsClass.getDeclaredField("componentList").apply {
                isAccessible = true
            }
            val result =
                componentList.get(showcaseComponentsClass.newInstance()) as List<ShowcaseCodegenMetadata>
            result.groupBy { it.group }
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

//    override fun onBackPressed() {
//        val currentScreen = ShowcaseBrowserScreenMetadata.currentScreen
//        val isSearchActive = ShowcaseBrowserScreenMetadata.isSearchActive
//        when {
//            isSearchActive -> {
//                ShowcaseBrowserScreenMetadata.isSearchActive = false
//                ShowcaseBrowserScreenMetadata.searchQuery = null
//            }
//            currentScreen == ShowcaseCurrentScreen.GROUPS -> {
//                finish()
//            }
//            currentScreen == ShowcaseCurrentScreen.GROUP_COMPONENTS -> {
//                ShowcaseBrowserScreenMetadata.currentScreen =
//                    ShowcaseCurrentScreen.GROUPS
//                ShowcaseBrowserScreenMetadata.currentGroup = null
//                ShowcaseBrowserScreenMetadata.currentComponent = null
//            }
//            currentScreen == ShowcaseCurrentScreen.COMPONENT_DETAIL -> {
//                ShowcaseBrowserScreenMetadata.currentScreen =
//                    ShowcaseCurrentScreen.GROUP_COMPONENTS
//                ShowcaseBrowserScreenMetadata.currentComponent = null
//            }
//        }
//    }

    companion object {
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcase"
        const val AUTOGEN_CLASS_NAME = "ShowcaseCodegenComponents"
    }
}
