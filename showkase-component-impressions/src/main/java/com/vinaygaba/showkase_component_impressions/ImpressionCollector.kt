package com.vinaygaba.showkase_component_impressions

import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ImpressionCollector<T>(
    val coroutineScope: CoroutineScope,
    val onVisibilityEvent: (T, Float, Rect) -> Unit
) {
    private val mutableImpressionEvents = MutableStateFlow<ImpressionData<T>?>(null)

    val impressionEvents: Flow<ImpressionData<T>> = mutableImpressionEvents.filterNotNull().debounce(200)

    internal fun onLayoutCoordinatesChanged(
        key: T,
        visibilityPercentage: VisibilityMetadata,
    ) {
        publishImpressionEvent(key, visibilityPercentage)
    }

    internal fun publishImpressionEvent(
        key: T,
        visibilityMetadata: VisibilityMetadata,
    ) {
        coroutineScope.launch {
            mutableImpressionEvents.emit(
                ImpressionData(
                    key,
                    visibilityMetadata.visibilityPercentage,
                    visibilityMetadata.boundsInWindow
                )
            )
        }
    }

    internal fun publishDisposeEvent(
        key: T,
        visibilityMetadata: VisibilityMetadata,
    ) {
        onVisibilityEvent(key, visibilityMetadata.visibilityPercentage, visibilityMetadata.boundsInWindow)
    }
}