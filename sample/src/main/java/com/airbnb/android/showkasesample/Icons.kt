package com.airbnb.android.showkasesample

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import com.airbnb.android.showkase.annotation.ShowkaseIcon

object Icons {
    @ShowkaseIcon(group = "Search Icons", name = "Search Icon Filled")
    val icon = Icons.Filled.Search

    @ShowkaseIcon(group = "Search Icons", name = "Search Icon Outlined")
    val searchIcon = Icons.Outlined.Search

    @ShowkaseIcon(group = "Baseline Icons", name = "BaseLine Verified User")
    @DrawableRes
    val iconVerifiedUser = R.drawable.ic_baseline_verified_user_24

    @ShowkaseIcon(group = "Launcher Icons", name = "Launcher Foreground")
    @DrawableRes
    val launcherForeground = R.drawable.ic_launcher_foreground

    @ShowkaseIcon(group = "Close Icons", name = "Close Icon")
    val closeIcon = Icons.Filled.Close

    @ShowkaseIcon(group = "Close Icons", name = "Close Icon Outlined")
    val closeIconOutlined = Icons.Outlined.Close

    @ShowkaseIcon(group = "Arrow Icons", name = "Arrow Back")
    val iconArrowBack = Icons.Filled.ArrowBack

    @ShowkaseIcon(group = "Arrow Icons", name = "Arrow Forward")
    val iconArrowForward = Icons.Filled.ArrowForward

    @ShowkaseIcon(group = "Arrow Icons", name = "Arrow DropDown")
    val iconArrowDropDown = Icons.Filled.ArrowDropDown

    @ShowkaseIcon(group = "Arrow Icons", name = "Arrow Play")
    val iconArrowPlay = Icons.Filled.PlayArrow
}
