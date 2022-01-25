package com.airbnb.android.showkase.models

import androidx.compose.runtime.Composable

// TODO(vinaygaba): Move it to a different module that has Android/Compose dependencies hoooked up. 
// This was added here only because this module has compose dependencies.
data class ShowkaseBrowserComponent(
    val componentKey: String,
    val group: String,
    val componentName: String,
    val order: Int,
    val componentKDoc: String,
    val component: @Composable () -> Unit,
    val styleName: String? = null,
    val isDefaultStyle: Boolean = false,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
) : Comparable<ShowkaseBrowserComponent> {
    override fun compareTo(other: ShowkaseBrowserComponent): Int {
        val compareOrder = order.compareTo(other.order)
        return if (compareOrder == 0) {
            componentName.compareTo(other.componentName)
        } else {
            compareOrder
        }
    }
}
