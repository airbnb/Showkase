package com.vinaygaba.showkase_component_impressions

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ImpressionCollector<T: Any>(
    val key: T,
    val coroutineScope: CoroutineScope,
    val onVisibilityEvent: (ShowkaseVisibilityEvent<T>) -> Unit
) {
    private val mutableImpressionEvents = MutableStateFlow<ShowkaseVisibilityEvent<T>?>(null)

    @OptIn(FlowPreview::class)
    val impressionEvents: Flow<ShowkaseVisibilityEvent<T>> =
        mutableImpressionEvents.filterNotNull().debounce(500)

    internal fun onLayoutCoordinatesChanged(
        visibilityPercentage: VisibilityMetadata,
    ) {
        publishImpressionEvent(visibilityPercentage)
    }

    internal fun publishImpressionEvent(
        visibilityMetadata: VisibilityMetadata,
    ) {
        coroutineScope.launch {
            mutableImpressionEvents.emit(
                ShowkaseVisibilityEvent(
                    key,
                    visibilityMetadata.visibilityPercentage,
                    visibilityMetadata.boundsInWindow,
                )
            )
        }
    }

    internal fun onDisposeEvent(
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