// This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing_my_very_long_name

import android.content.Context
import android.content.Intent
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
import com.airbnb.android.showkase.models.ShowkaseProvider
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity

/**
 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
 */
public fun Showkase.getBrowserIntent(context: Context): Intent {
    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
    intent.putExtra("SHOWKASE_ROOT_MODULE",
        "com.airbnb.android.showkase_processor_testing_my_very_long_name.TestShowkaseRoot")
    return intent
}

/**
 * Helper function that's give's you access to Showkase metadata. This contains data about the
 * composables, colors and typography in your codebase that's rendered in showkase.
 */
public fun Showkase.getMetadata(): ShowkaseElementsMetadata {
    try {
      val showkaseComponentProvider =
          Class.forName("com.airbnb.android.showkase_processor_testing_my_very_long_name.TestShowkaseRootCodegen").newInstance()
          as ShowkaseProvider
      return showkaseComponentProvider.metadata()
    } catch(exception: ClassNotFoundException) {
      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
    }
}
