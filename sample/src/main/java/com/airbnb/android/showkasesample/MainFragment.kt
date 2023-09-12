package com.airbnb.android.showkasesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.ui.ShowkaseBrowserFragment

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val context = requireContext()
        return ComposeView(context).apply {
            setContent {
                MainScreen(
                    navigateToShowkaseActivity = { startActivity(Showkase.getBrowserIntent(context)) },
                    navigateToShowkaseFragment = {
                        val fragment = ShowkaseBrowserFragment.newInstance(RootModule::class.java.name)
                        (activity as? MainActivity)?.replaceFragment(fragment, "ShowkaseBrowserFragment")
                    }
                )
            }
        }
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}