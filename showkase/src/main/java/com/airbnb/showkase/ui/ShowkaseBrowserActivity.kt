package com.airbnb.showkase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.state
import androidx.compose.ui.platform.setContent
import com.airbnb.showkase.exceptions.ShowkaseException
import com.airbnb.showkase.models.ShowkaseBrowserComponent
import com.airbnb.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.showkase.models.ShowkaseProvider

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
            var showkaseBrowserScreenMetadata = state { ShowkaseBrowserScreenMetadata() }
            when {
                groupedComponentsMap.isNotEmpty() -> {
                    ShowkaseBrowserApp(groupedComponentsMap, showkaseBrowserScreenMetadata)
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
                Class.forName("$classKey$AUTOGEN_CLASS_NAME").newInstance()

            (showkaseComponentProvider as ShowkaseProvider).getShowkaseComponents()
                .groupBy { it.group }
                
        } catch (exception: ClassNotFoundException) {
            mapOf()
        }
    }

    companion object {
        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val AUTOGEN_CLASS_NAME = "Codegen"

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
