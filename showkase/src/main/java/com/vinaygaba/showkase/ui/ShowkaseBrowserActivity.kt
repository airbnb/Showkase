package com.vinaygaba.showkase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.state
import androidx.ui.core.setContent
import com.vinaygaba.showkase.models.ShowkaseBrowserComponent
import com.vinaygaba.showkase.models.ShowkaseBrowserScreenMetadata
import com.vinaygaba.showkase.models.ShowkaseComponentsProvider

class ShowkaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val classKey = intent.extras?.getString(SHOWKASE_ROOT_MODULE_KEY) ?: return 
        setContent {
            val groupedComponentsMap = getGroupedComponentsMap(classKey)
            var showkaseBrowserScreenMetadata = state { ShowkaseBrowserScreenMetadata() }
            when {
                groupedComponentsMap.isNotEmpty() -> {
                    ShowkaseBrowserApp(groupedComponentsMap, showkaseBrowserScreenMetadata)
                }
                else -> {
                    ShowkaseErrorScreen(
                        errorText = "There were no @Composable functions that were annotated with " +
                                "@Showkase. If you think this is a mistake, file an issue at " +
                                "https://github.com/vinaygaba/Showkase/issues"
                    )
                }
            }
        }
    }

    private fun getGroupedComponentsMap(classKey: String): Map<String, List<ShowkaseBrowserComponent>> {
        return try {
            val showkaseComponentProvider = Class.forName("$classKey$AUTOGEN_CLASS_NAME").newInstance()
            
            val componentList = (showkaseComponentProvider as ShowkaseComponentsProvider).getShowkaseComponents()
            componentList.groupBy { it.group }
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    companion object {
        const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val AUTOGEN_CLASS_NAME = "CodegenComponents"
    }
}
