package com.airbnb.android.showkase.ui

import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

/**
 * Related discussion -
 * https://kotlinlang.slack.com/archives/CJLTWPH7S/p1591558155394500?thread_ts=1591558024.394400&cid=CJLTWPH7S
 */
private val LocalBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcherOwner?> { null }

private class ComposableBackHandler(enabled: Boolean) : OnBackPressedCallback(enabled) {
    lateinit var onBackPressed: () -> Unit

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}

@Composable
internal fun Handler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val dispatcher = (LocalBackPressedDispatcher.current ?: return).onBackPressedDispatcher
    val handler = remember { ComposableBackHandler(enabled) }
    DisposableEffect(dispatcher) {
        dispatcher.addCallback(handler)
        onDispose { handler.remove() }
    }
    LaunchedEffect(enabled) {
        handler.isEnabled = enabled
    }
    LaunchedEffect(onBackPressed) {
        handler.onBackPressed = onBackPressed
    }
}

// TODO(vinaygaba) - Replace with the version Compose just exposed in the activity-compose bindings
@Composable
internal fun BackButtonHandler(
    onBackPressed: () -> Unit,
) {
    var context = LocalContext.current
    // Inspired from https://cs.android.com/androidx/platform/frameworks/support/+/
    // androidx-master-dev:navigation/navigation-compose/src/main/java/androidx/navigation/
    // compose/NavHost.kt;l=88
    // This was necessary because using Jetpack Navigation does not allow typecasting a
    // NavBackStackEntry to LifecycleOwnerAmbient.
    while (context is ContextWrapper) {
        if (context is OnBackPressedDispatcherOwner) {
            break
        }
        context = context.baseContext
    }
    CompositionLocalProvider(
        LocalBackPressedDispatcher provides context as ComponentActivity
    ) {
        Handler {
            onBackPressed()
        }
    }
}
