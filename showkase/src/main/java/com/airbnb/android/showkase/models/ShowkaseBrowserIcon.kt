package com.airbnb.android.showkase.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class ShowkaseBrowserIcon(
    val group: String,
    val name: String,
    val kDoc: String,
    val imageVector: ImageVector? = null,
    @DrawableRes val drawableRes: Int? = null
)