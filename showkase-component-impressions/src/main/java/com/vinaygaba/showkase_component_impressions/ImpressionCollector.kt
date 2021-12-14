package com.vinaygaba.showkase_component_impressions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ImpressionCollector<T: Any>(
    val key: T,
    val coroutineScope: CoroutineScope,
    val onVisibilityEvent: (ShowkaseVisibilityEvent<T>) -> Unit
): LifecycleEventObserver {
    private val mutableImpressionEvents = MutableStateFlow<ShowkaseVisibilityEvent<T>?>(null)

    @OptIn(FlowPreview::class)
    private val impressionEvents: Flow<ShowkaseVisibilityEvent<T>> =
        mutableImpressionEvents.filterNotNull().debounce(500)

    private var visibilityMetadata: VisibilityMetadata? by mutableStateOf(null)

    init {
        coroutineScope.launch {
            impressionEvents.collect { impressionData ->
                val impression = impressionData as ShowkaseVisibilityEvent<*>
                onVisibilityEvent(
                    ShowkaseVisibilityEvent(
                        impression.key as T,
                        impression.visibilityPercentage,
                        impression.boundsInWindow
                    )
                )
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                publishImpressionEvent(hidden)
            }
            Lifecycle.Event.ON_START -> {
                visibilityMetadata?.let {
                    publishImpressionEvent(it)
                }
            }
            else -> {
                // No-op
            }
        }
    }

    internal fun onLayoutCoordinatesChanged(
        visibilityPercentage: VisibilityMetadata,
    ) {
        visibilityMetadata = visibilityPercentage
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