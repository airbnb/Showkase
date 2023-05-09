package com.airbnb.android.showkase.models

import androidx.compose.ui.text.TextStyle

data class ShowkaseBrowserTypography(
    val typographyGroup: String,
    val typographyName: String,
    val typographyKDoc: String,
    val textStyle: TextStyle,
): ShowkaseBrowserElement {
    override val group: String = typographyGroup
    override val name: String = typographyName
    override val kDoc: String = typographyKDoc
}
