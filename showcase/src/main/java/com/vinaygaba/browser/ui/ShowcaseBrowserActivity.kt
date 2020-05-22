package com.vinaygaba.browser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.*
import com.vinaygaba.browser.models.ShowcaseBrowserScreenMetadata
import com.vinaygaba.browser.models.ShowcaseCodegenMetadata
import com.vinaygaba.browser.models.ShowcaseCurrentScreen

class ShowcaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val groupedComponentsMap = getGroupedComponentsMap()
            ShowcaseBrowserApp(groupedComponentsMap)
        }
    }

    private fun getGroupedComponentsMap(): Map<String, List<ShowcaseCodegenMetadata>> {
        val showcaseComponentsClass = Class.forName("$CODEGEN_PACKAGE_NAME.$AUTOGEN_CLASS_NAME")
        val componentList = showcaseComponentsClass.getDeclaredField("componentList").apply {
            isAccessible = true
        }
        val result =
            componentList.get(showcaseComponentsClass.newInstance()) as List<ShowcaseCodegenMetadata>
        return result.groupBy { it.group }
    }

    override fun onBackPressed() {
        when (ShowcaseBrowserScreenMetadata.currentScreen) {
            ShowcaseCurrentScreen.GROUPS -> {
                finish()
            }
            ShowcaseCurrentScreen.GROUP_COMPONENTS -> {
                ShowcaseBrowserScreenMetadata.currentScreen =
                    ShowcaseCurrentScreen.GROUPS
                ShowcaseBrowserScreenMetadata.currentGroup = null
                ShowcaseBrowserScreenMetadata.currentComponent = null
            }
            ShowcaseCurrentScreen.COMPONENT_DETAIL -> {
                ShowcaseBrowserScreenMetadata.currentScreen =
                    ShowcaseCurrentScreen.GROUP_COMPONENTS
                ShowcaseBrowserScreenMetadata.currentComponent = null
            }
        }
    }

    companion object {
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcasecodegen"
        const val AUTOGEN_CLASS_NAME = "ShowcaseComponents"
    }
}
