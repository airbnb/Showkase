package com.vinaygaba.showkase_component_impressions

import android.view.View
import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow

internal val defaultVisibilityMetadata = VisibilityMetadata(0f, Rect(0f, 0f,0f,0f))

internal fun LayoutCoordinates.visibilityPercentage(view: View): VisibilityMetadata {
    if (!isAttached) return defaultVisibilityMetadata
    // Window relative bounds of our compose root view that are visible on the screen
    val globalRootRect = android.graphics.Rect()
    if (!view.getGlobalVisibleRect(globalRootRect)) {
        return defaultVisibilityMetadata
    }

    val bounds = boundsInWindow()

    val visibleArea = bounds.height * bounds.width
    val totalArea = size.width * size.height
    if (totalArea == 0) {
        return defaultVisibilityMetadata
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