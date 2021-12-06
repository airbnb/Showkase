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
 */
data class ShowkaseVisibilityEvent<T>(
    val key: T,
    val visibilityPercentage: Float,
    val boundsInWindow: Rect
)

/**
 * Use this modifier to get visibility events for a given Composable. It emits visibility events
 * when the composable is added to the composition (visible), when its removed from the
 * composition(invisible), when the activity is backgrounded(visible) and when the activity is
 * foregrounded(visible). In addition,
 *
 * @param key Unique identifier for a given composable function that you use this modifier on
 * @param onVisibilityChanged Callback that's called when the visibility of a composable function
 * changes. This event has information about the key of the composable, the percentage of composable
 * that's visible on the screen and the bounds of the composable.
 */
@OptIn(FlowPreview::class)
fun <T> Modifier.visibilityEvents(
    key: T,
    onVisibilityChanged: (ShowkaseVisibilityEvent<T>) -> Unit,
) = composed {
    val view = LocalView.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()
    val visibilityEvent by rememberUpdatedState(newValue = onVisibilityChanged)
    val impressionCollector = remember(key) { ImpressionCollector<T>(scope, visibilityEvent) }
    var visibilityMetadata: VisibilityMetadata? by remember { mutableStateOf(null) }

    registerLifecycleImpressionEvents(key, lifecycle, impressionCollector, visibilityMetadata)
    registerDisposeImpressionEvents(key, impressionCollector, lifecycle)
    collectImpressionEvents(key, impressionCollector, visibilityEvent)

    onGloballyPositioned { layoutCoordinates ->
        visibilityMetadata = layoutCoordinates.visibilityPercentage(view = view)
        impressionCollector.onLayoutCoordinatesChanged(key, visibilityMetadata!!)
    }
}

/**
 * Used for handling the use case where a composable function is not in composition anymore i.e is
 * invisible.
 */
@SuppressLint("ComposableNaming")
@Composable
private fun <T> registerDisposeImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>,
    lifecycle: Lifecycle
) {
    DisposableEffect(key, lifecycle) {
        onDispose {
            impressionCollector.publishDisposeEvent(
                key,
                defaultVisibilityMetadata,
            )
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
private fun <T> collectImpressionEvents(
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

/**
 * Used for handling the use case where the activity/app is backgrounded or foregrounded.
 */
@SuppressLint("RememberReturnType", "ComposableNaming")
@Composable
private fun <T> registerLifecycleImpressionEvents(
    key: T,
    lifecycle: Lifecycle,
    impressionCollector: ImpressionCollector<T>,
    visibility: VisibilityMetadata?,
) {
    remember(key1 = key, key2 = visibility) {
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP  -> {
                        impressionCollector.publishImpressionEvent(key, defaultVisibilityMetadata)
                    }
                    Lifecycle.Event.ON_RESUME, Lifecycle.Event.ON_START -> {
                        visibility?.let {
                            impressionCollector.publishImpressionEvent(key, it)
                        }
                    }
                    else -> {
                        // No-op
                    }
                }
            }
        )
    }
}