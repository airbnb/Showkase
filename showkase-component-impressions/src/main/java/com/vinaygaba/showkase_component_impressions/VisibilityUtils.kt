package com.vinaygaba.showkase_component_impressions

import android.view.View
import androidx.annotation.FloatRange
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow

internal fun LayoutCoordinates.isOnScreen(
    @FloatRange(from = 0.0, to = 1.0) visibilityThreshold: Float,
    parentView: View,
): Boolean {
    return visibilityPercentage(parentView) >= visibilityThreshold
}

private fun LayoutCoordinates.visibilityPercentage(view: View): Float {
    if (!isAttached) return 0f
    // Window relative bounds of our compose root view that are visible on the screen
    val globalRootRect = android.graphics.Rect()
    if (!view.getGlobalVisibleRect(globalRootRect)) {
        return 0f
    }

    val bounds = boundsInWindow()

    val visibleArea = bounds.height * bounds.width
    val totalArea = size.width * size.height
    if (totalArea == 0) {
        return 0f
    }
    return visibleArea / totalArea
}