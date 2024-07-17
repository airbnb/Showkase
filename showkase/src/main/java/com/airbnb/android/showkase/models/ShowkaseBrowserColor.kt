package com.airbnb.android.showkase.models

import androidx.compose.ui.graphics.Color

data class ShowkaseBrowserColor(
    val colorGroup: String,
    val colorName: String,
    val colorKDoc: String,
    val color: Color
) : ShowkaseBrowserElement {
    override val group: String = colorGroup
    override val name: String = colorName
    override val kDoc: String = colorKDoc
}
