package com.vinaygaba.showkase_component_impressions

import android.view.View
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow

internal val hidden = VisibilityMetadata(0f, Rect(0f, 0f,0f,0f))

internal fun LayoutCoordinates.visibilityPercentage(view: View): VisibilityMetadata {
    if (!isAttached) return hidden
    // Window relative bounds of our compose root view that are visible on the screen
    val globalRootRect = android.graphics.Rect()
    if (!view.getGlobalVisibleRect(globalRootRect)) {
        return hidden
    }

    val bounds = boundsInWindow()

    val visibleArea = bounds.height * bounds.width
    val totalArea = size.width * size.height
    if (totalArea == 0) {
        return hidden
    }
    return VisibilityMetadata(
        visibilityPercentage = visibleArea / totalArea,
        boundsInWindow = bounds
    )
}

internal data class VisibilityMetadata(
    val visibilityPercentage: Float,
    val boundsInWindow: Rect
)