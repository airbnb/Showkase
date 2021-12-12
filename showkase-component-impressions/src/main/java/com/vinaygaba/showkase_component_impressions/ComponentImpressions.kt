package com.vinaygaba.showkase_component_impressions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

/**
 * Passed in the callback of the [visibilityEvents] Modifier. Contains information about the
 * visibility of a given composable function that uses the visibilityEvents Modifier.
 *
 * @param key The unique identifier that you passed with the [visibilityEvents] modifier.
 * @param visibilityPercentage A value between 0.0 and 1.0 that represents how much a given composable
 * was visible on the screen. 0.0 represents that it wasn't visible on the screen and 1.0 represents
 * that it was completely visible on the screen.
 * @param boundsInWindow A [Rect] object that contains information about the bounds of a given
 * composable. When the bounds are (0, 0, 0, 0), it means that the composable was not visible on
 * screen.
 */
data class ShowkaseVisibilityEvent<T : Any>(
    val key: T,
    val visibilityPercentage: Float,
    val boundsInWindow: Rect,
)

/**
 * Use this modifier to get visibility events for a given Composable. It emits visibility events
 * when the composable is added to the composition (visible), when its removed from the
 * composition(invisible), when the activity is backgrounded(invisible) and when the activity is
 * foregrounded(visible). In addition,
 *
 * @param key Unique identifier for a given composable function that you use this modifier on
 * @param onVisibilityChanged Callback that's called when the visibility of a composable function
 * changes.

 */
@OptIn(FlowPreview::class)
fun <T : Any> Modifier.visibilityEvents(
    key: T,
    onVisibilityChanged: (ShowkaseVisibilityEvent<T>) -> Unit,
) = composed {
    val view = LocalView.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()
    val visibilityEventCallback by rememberUpdatedState(newValue = onVisibilityChanged)
    val impressionCollector =
        remember(key) { ImpressionCollector<T>(key, scope, visibilityEventCallback) }
    var visibilityMetadata: VisibilityMetadata? by remember { mutableStateOf(null) }

    registerDisposeImpressionEvents(key, impressionCollector, lifecycle, visibilityMetadata)
    collectImpressionEvents(key, impressionCollector, visibilityEventCallback)

    onGloballyPositioned { layoutCoordinates ->
        visibilityMetadata = layoutCoordinates.visibilityPercentage(view = view).also {
            impressionCollector.onLayoutCoordinatesChanged(it)
        }
    }
}

/**
 * Used for handling the use case where a composable function is not in composition anymore i.e is
 * invisible.
 */
@SuppressLint("ComposableNaming", "RememberReturnType")
@Composable
private fun <T : Any> registerDisposeImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>,
    lifecycle: Lifecycle,
    visibility: VisibilityMetadata?
) {
    val updatedVisibilityMetadata by rememberUpdatedState(newValue = visibility)
    DisposableEffect(key) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    impressionCollector.publishImpressionEvent(hidden)
                }
                Lifecycle.Event.ON_START -> {
                    updatedVisibilityMetadata?.let {
                        impressionCollector.publishImpressionEvent(it)
                    }
                }
                else -> {
                    // No-op
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            impressionCollector.onDisposeEvent(hidden)
            lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Collects the visibility change events from the internal coroutine flow and invokes the
 * onVisibilityEvent that the user passed.
 */
@SuppressLint("ComposableNaming")
@FlowPreview
@Composable
private fun <T : Any> collectImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>,
    onVisibilityEvent: (ShowkaseVisibilityEvent<T>) -> Unit
) {
    LaunchedEffect(key) {
        impressionCollector.impressionEvents
            .collect { impressionData ->
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
