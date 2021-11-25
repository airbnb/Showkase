package com.vinaygaba.showkase_component_impressions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ImpressionCollector<T>(
    lifecycle: Lifecycle
) {
    private val coroutineScope = lifecycle.coroutineScope
    private val mutableImpressionEvents = MutableStateFlow<Any?>(null)

    val impressionEvents: Flow<Any> = mutableImpressionEvents.filterNotNull()

    fun onCoordinatesUpdated(
        key: T,
        isVisible: Boolean?,
    ) {
        val visibilityEvent = if (isVisible == true) ShowkaseVisibilityEvent.VISIBLE else ShowkaseVisibilityEvent.INVISIBLE
        publishImpressionEvent(key, visibilityEvent)
    }

    fun publishImpressionEvent(
        key: T,
        visibilityEvent: ShowkaseVisibilityEvent
    ) {
        coroutineScope.launch {
            mutableImpressionEvents.emit(ImpressionData(key, visibilityEvent))
        }
    }
}