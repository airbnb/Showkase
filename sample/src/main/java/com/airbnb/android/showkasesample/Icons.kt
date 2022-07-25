package com.airbnb.android.showkasesample

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.airbnb.android.showkase.annotation.ShowkaseIcon

object Icons {
    @ShowkaseIcon(group = "Search Icons", name = "Search Icon Filled")
    val icon = Icons.Filled.Search

    @ShowkaseIcon(group = "Search Icons", name = "Search Icon Outlined")
    val searchIcon = Icons.Outlined.Search

    @ShowkaseIcon(group = "Search Icons", name = "BaseLine Verified User")
    @DrawableRes
    val iconVerifiedUser = R.drawable.ic_baseline_verified_user_24
}
