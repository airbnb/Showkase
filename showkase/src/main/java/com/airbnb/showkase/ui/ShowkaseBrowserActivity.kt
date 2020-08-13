package com.airbnb.showkase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.state
import androidx.compose.ui.platform.setContent
import com.airbnb.showkase.exceptions.ShowkaseException
import com.airbnb.showkase.models.ShowkaseBrowserColor
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseColorsProvider
import com.airbnb.showkase.models.ShowkaseComponentsProvider

/**
 * The activity that's responsible for showing all the @Composable components that were annotated
 * with the @Showkase annotation.
 */
class ShowkaseBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val classKey = intent.extras?.getString(SHOWKASE_ROOT_MODULE_KEY) ?: throw ShowkaseException(
            "Missing key in bundle. Please start this activity by using the intent returned by " +
                    "the ShowkaseBrowserActivity.getIntent() method."
        )
        setContent {
            val groupedComponentsMap = getGroupedComponentsMap(classKey)
            val groupedColorsMap = getGroupedColorsMap(classKey)
            var showkaseBrowserScreenMetadata = state { ShowkaseBrowserScreenMetadata() }
            when {
                groupedComponentsMap.isNotEmpty() || groupedColorsMap.isNotEmpty() -> {
                    ShowkaseBrowserApp(groupedComponentsMap, groupedColorsMap,
                        showkaseBrowserScreenMetadata)
                }
                else -> {
                    ShowkaseErrorScreen(
                        errorText = "There were no @Composable functions that were annotated with " +
                                "@Showkase. If you think this is a mistake, file an issue at " +
                                "https://github.com/airbnb/Showkase/issues"
                    )
                }
            }
        }
    }

    private fun getGroupedComponentsMap(classKey: String): Map<String, List<ShowkaseBrowserComponent>> {
        return try {
            val showkaseComponentProvider =
                Class.forName("$classKey$COMPONENT_AUTOGEN_CLASS_NAME").newInstance()

            (showkaseComponentProvider as ShowkaseComponentsProvider).getShowkaseComponents()
                .groupBy { it.group }
                
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    private fun getGroupedColorsMap(classKey: String): Map<String, List<ShowkaseBrowserColor>> {
        return try {
            val showkaseColorProvider =
                Class.forName("$classKey$COLOR_AUTOGEN_CLASS_NAME").newInstance()

            (showkaseColorProvider as ShowkaseColorsProvider).getShowkaseColors()
                .groupBy { it.colorGroup }

        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    companion object {
        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val COMPONENT_AUTOGEN_CLASS_NAME = "CodegenComponents"
        private const val COLOR_AUTOGEN_CLASS_NAME = "CodegenColors"

        /**
         * Returns the intent that the users of this library need to use for starting the
         * Showkase browser activity. Please make sure to use this instead of starting the
         * activity directly as it sets the right value in the bundle in order for the activity
         * to start correctly.
         * 
         * @param context Android context
         * @param rootModuleCanonicalName The canonical name of the implementation of 
         * ShowkaseRootModule.
         */
        fun getIntent(context: Context, rootModuleCanonicalName: String) =
            Intent(context, ShowkaseBrowserActivity::class.java).apply {
                putExtra(SHOWKASE_ROOT_MODULE_KEY, rootModuleCanonicalName)
            }
    }
}
