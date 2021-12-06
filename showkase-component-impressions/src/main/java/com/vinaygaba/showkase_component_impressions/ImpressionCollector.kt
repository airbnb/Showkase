package com.vinaygaba.showkase_component_impressions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ImpressionCollector<T>(
    val coroutineScope: CoroutineScope,
    val onVisibilityEvent: (ShowkaseVisibilityEvent<T>) -> Unit
) {
    private val mutableImpressionEvents = MutableStateFlow<ShowkaseVisibilityEvent<T>?>(null)

    @OptIn(FlowPreview::class)
    val impressionEvents: Flow<ShowkaseVisibilityEvent<T>> =
        mutableImpressionEvents.filterNotNull().debounce(200)

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
                ShowkaseVisibilityEvent(
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
        onVisibilityEvent(
            ShowkaseVisibilityEvent(
                key,
                visibilityMetadata.visibilityPercentage,
                visibilityMetadata.boundsInWindow
            )
        )
    }
}