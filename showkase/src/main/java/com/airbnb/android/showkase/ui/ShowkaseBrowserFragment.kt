package com.airbnb.android.showkase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.airbnb.android.showkase.exceptions.ShowkaseException

/**
 * The activity that's responsible for showing all the UI elements that were annotated
 * with the Showkase related annotations.
 */
class ShowkaseBrowserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val classKey = arguments?.getString(SHOWKASE_ROOT_MODULE_KEY) ?: throw ShowkaseException(
            "Missing key in bundle. Please start this activity by using the intent returned by " +
                    "the ShowkaseBrowserActivity.getIntent() method."
        )

        return ComposeView(requireContext()).apply {
            setContent {
                ShowkaseBrowser(classKey = classKey)
            }
        }
    }

    companion object {

        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"

        /**
         * Create a new instance of the ShowkaseBrowserFragment with the specified
         * root module canonical name.
         *
         * @param rootModuleCanonicalName The canonical name of the implementation of
         *                               ShowkaseRootModule.
         * @return A new instance of ShowkaseBrowserFragment with the given arguments.
         */
        fun newInstance(rootModuleCanonicalName: String): ShowkaseBrowserFragment {
            val args = Bundle()
            args.putString(SHOWKASE_ROOT_MODULE_KEY, rootModuleCanonicalName)
            val fragment = ShowkaseBrowserFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
