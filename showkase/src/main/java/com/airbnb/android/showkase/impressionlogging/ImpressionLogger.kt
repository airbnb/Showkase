package com.airbnb.android.showkase.impressionlogging

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

typealias ImpressionId = String

fun Modifier.impressionLogging(
    id: ImpressionId,
    onVisibilityEvent: (ImpressionId, VisibilityEvent) -> Unit
) = composed {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val dummyValue = remember {
        lifecycle.addObserver(
            LifecycleEventObserver { owner, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        onVisibilityEvent(id, VisibilityEvent.INVISIBLE)
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        onVisibilityEvent(id, VisibilityEvent.INVISIBLE)
                    }
                }
            }
        )
        5
    }
    DisposableEffect(Unit) {
        onVisibilityEvent(id, VisibilityEvent.VISIBLE)
        onDispose { onVisibilityEvent(id, VisibilityEvent.INVISIBLE) }
    }
    this
//    this.onGloballyPositioned {
//        onVisibilityEvent(id, VisibilityEvent.VISIBLE)
//        Log.e("TAG","onGloballyPositionedCalled")
//        Log.e("TAG","it.isAttached ${it.isAttached}")
//        Log.e("TAG","it.positionInWindow ${it.positionInWindow().x} : ${it.positionInWindow().y}")
//    }
}

data class ImpressionMetadata(
    val id: String,
    val startTime: Long,
    val endTime: Long,
)

@Composable
fun TestScreen() {
    
    var showComposable by remember {
        mutableStateOf(false)
    }
    Column {
        Button(onClick = { showComposable = !showComposable }) {
            Text("Click Me")
        }


        if (!showComposable) {
            Text(
                text = "I'm now visible $showComposable",
                modifier = Modifier.impressionLogging(
                    id = "TextComposable",
                    onVisibilityEvent = { impressionId, visibilityEvent ->
                        Log.e("TAG","$impressionId $visibilityEvent")
                    }
                )
            )
        }
    }
}

enum class VisibilityEvent {
    VISIBLE,
    INVISIBLE
}