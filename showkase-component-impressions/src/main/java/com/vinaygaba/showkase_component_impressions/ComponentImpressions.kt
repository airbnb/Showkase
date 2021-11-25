package com.vinaygaba.showkase_component_impressions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.flow.collect

internal data class ImpressionData<T>(
    val key: T,
    val visibilityEvent: ShowkaseVisibilityEvent
)

@Composable
fun <T> Modifier.visibilityImpressions(
    key: T,
    onVisibilityEvent: (T, ShowkaseVisibilityEvent, String) -> Unit,
    visibilityThreshold: Float = 0.5f
) = composed {
    val view = LocalView.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val impressionCollector = remember { ImpressionCollector<T>(lifecycle) }
    var isVisible: Boolean? by remember { mutableStateOf(null) }

    registerLifecycleImpressionEvents(key, lifecycle, impressionCollector)
    registerDisposeImpressionEvents(key, impressionCollector)
    collectImpressionEvents(key, impressionCollector, onVisibilityEvent)

    onGloballyPositioned { layoutCoordinates ->
        isVisible = layoutCoordinates.isOnScreen(
            parentView = view,
            visibilityThreshold = visibilityThreshold
        )
        impressionCollector.onCoordinatesUpdated(key, isVisible)
    }
}

@Composable
private fun <T> registerDisposeImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>
) {
    DisposableEffect(key) {
        onDispose {
            impressionCollector.publishImpressionEvent(
                key,
                ShowkaseVisibilityEvent.INVISIBLE
            )
        }
    }
}

@Composable
private fun <T> collectImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>,
    onVisibilityEvent: (T, ShowkaseVisibilityEvent, String) -> Unit
) {
    LaunchedEffect(key) {
        impressionCollector.impressionEvents.collect { impressions ->
            val impression = impressions as ImpressionData<*>
            onVisibilityEvent(
                impression.key as T,
                impression.visibilityEvent,
                impression.visibilityEvent.name
            )
        }
    }
}

@Composable
private fun <T> registerLifecycleImpressionEvents(
    key: T,
    lifecycle: Lifecycle,
    impressionCollector: ImpressionCollector<T>,
) {
    remember {
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP  -> {
                        impressionCollector.publishImpressionEvent(key, ShowkaseVisibilityEvent.INVISIBLE)
                    }
                    Lifecycle.Event.ON_RESUME, Lifecycle.Event.ON_START -> {
                        impressionCollector.publishImpressionEvent(key, ShowkaseVisibilityEvent.VISIBLE)
                    }
                    else -> {}
                }
            }
        )
    }
}