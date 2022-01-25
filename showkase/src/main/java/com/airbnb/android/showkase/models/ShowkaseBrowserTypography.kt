package com.airbnb.android.showkase.models

import androidx.compose.ui.text.TextStyle

data class ShowkaseBrowserTypography(
    val typographyGroup: String,
    val typographyName: String,
    val order: Int,
    val typographyKDoc: String,
    val textStyle: TextStyle,
) : Comparable<ShowkaseBrowserTypography> {
    override fun compareTo(other: ShowkaseBrowserTypography): Int {
        val compareOrder = order.compareTo(other.order)
        return if (compareOrder == 0) {
            typographyName.compareTo(other.typographyName)
        } else {
            compareOrder
        }
    }
}
