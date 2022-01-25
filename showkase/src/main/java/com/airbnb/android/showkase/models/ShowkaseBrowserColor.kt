package com.airbnb.android.showkase.models

import androidx.compose.ui.graphics.Color

data class ShowkaseBrowserColor(
    val colorGroup: String,
    val colorName: String,
    val order: Int,
    val colorKDoc: String,
    val color: Color
) : Comparable<ShowkaseBrowserColor> {
    override fun compareTo(other: ShowkaseBrowserColor): Int {
        val compareOrder = order.compareTo(other.order)
        return if (compareOrder == 0) {
            colorName.compareTo(other.colorName)
        } else {
            compareOrder
        }
    }
}
