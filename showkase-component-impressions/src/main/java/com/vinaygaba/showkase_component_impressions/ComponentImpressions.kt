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

internal data class ImpressionData<T>(
    val key: T,
    val visibilityPercentage: Float,
    val boundsInWindow: Rect
)

/**
 *
 */
@OptIn(FlowPreview::class)
fun <T> Modifier.visibilityImpressions(
    key: T,
    onVisibilityEvent: (T, Float, Rect) -> Unit,
) = composed {
    val view = LocalView.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()
    val visibilityEvent by rememberUpdatedState(newValue = onVisibilityEvent)
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

@SuppressLint("ComposableNaming")
@FlowPreview
@Composable
private fun <T> collectImpressionEvents(
    key: T,
    impressionCollector: ImpressionCollector<T>,
    onVisibilityEvent: (T, Float, Rect) -> Unit
) {
    LaunchedEffect(key) {
        impressionCollector.impressionEvents
            .collect { impressionData ->
            val impression = impressionData as ImpressionData<*>
            onVisibilityEvent(
                impression.key as T,
                impression.visibilityPercentage,
                impression.boundsInWindow
            )
        }
    }
}

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