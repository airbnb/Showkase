package com.vinaygaba.showcase.ui

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.onCommit
import androidx.compose.remember
import androidx.compose.staticAmbientOf
import androidx.ui.core.LifecycleOwnerAmbient

/**
 * Related discussion - 
 * https://kotlinlang.slack.com/archives/CJLTWPH7S/p1591558155394500?thread_ts=1591558024.394400&cid=CJLTWPH7S
 */
private val AmbientBackPressedDispatcher = staticAmbientOf<OnBackPressedDispatcherOwner?> { null }

private class ComposableBackHandler(enabled: Boolean) : OnBackPressedCallback(enabled) {
    lateinit var onBackPressed: () -> Unit

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}

@Composable
internal fun handler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val dispatcher = (AmbientBackPressedDispatcher.current ?: return).onBackPressedDispatcher
    val handler = remember { ComposableBackHandler(enabled) }
    onCommit(dispatcher) {
        dispatcher.addCallback(handler)
        onDispose { handler.remove() }
    }
    onCommit(enabled) {
        handler.isEnabled = enabled
    }
    onCommit(onBackPressed) {
        handler.onBackPressed = onBackPressed
    }
}

@Composable
internal fun BackButtonHandler(onBackPressed: () -> Unit) {
    Providers(
        AmbientBackPressedDispatcher provides LifecycleOwnerAmbient.current as ComponentActivity
    ) {
        handler {
            onBackPressed()
        }
    }
}
