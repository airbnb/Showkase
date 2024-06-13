package com.airbnb.android.showkase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.android.showkase.exceptions.ShowkaseException

/**
 * The activity that's responsible for showing all the UI elements that were annotated
 * with the Showkase related annotations.
 */
class ShowkaseBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val classKey = intent.extras?.getString(SHOWKASE_ROOT_MODULE_KEY) ?: throw ShowkaseException(
            "Missing key in bundle. Please start this activity by using the intent returned by " +
                    "the ShowkaseBrowserActivity.getIntent() method."
        )
        setContent {
            ShowkaseBrowser(classKey = classKey)
        }
    }

    companion object {

        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"

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
