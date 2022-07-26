@file:Suppress("PackageNaming")
package com.airbnb.android.showkase_browser_testing

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.airbnb.android.showkase.annotation.ShowkaseIcon

class WrapperIconClass {
    @ShowkaseIcon(name = "Search", "Filled Icons")
    val searchIcon = Icons.Filled.Search

    @ShowkaseIcon(name = "Verified", group = "Verified Icons")
    @DrawableRes
    val verifiedIcon = R.drawable.ic_baseline_verified_user_24

}

object WrapperIconObject {
    @ShowkaseIcon(name = "Search From Object", "Filled Icons")
    val searchIcon = Icons.Filled.Search

    @ShowkaseIcon(name = "Verified From Object", group = "Verified Icons")
    @DrawableRes
    val verifiedIcon = R.drawable.ic_baseline_verified_user_24

}
