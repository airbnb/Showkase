package com.airbnb.android.showkase.impressionlogging

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
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
    onVisibilityEvent: (ImpressionId, VisibilityEvent, String) -> Unit
) = composed {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
//    val dummyValue = remember {
//        Log.e("TAG", "Calculating remember")
//        lifecycle.addObserver(
//            LifecycleEventObserver { owner, event ->
//                when (event) {
//                    Lifecycle.Event.ON_STOP -> {
//                        onVisibilityEvent(id, VisibilityEvent.INVISIBLE, "ON_STOP")
//                    }
//                    Lifecycle.Event.ON_RESUME -> {
//                        onVisibilityEvent(id, VisibilityEvent.VISIBLE, "ON_RESUME")
//                    }
//                }
//            }
//        )
//        5
//    }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
//                onVisibilityEvent(id, VisibilityEvent.VISIBLE, "ON_RESUME")
            } else if (event == Lifecycle.Event.ON_PAUSE) {
//                onVisibilityEvent(id, VisibilityEvent.INVISIBLE, "ON_PAUSE")
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
//            onVisibilityEvent(id, VisibilityEvent.INVISIBLE, "DISPOSABLE_EFFECT")
        }
    }
    this.onGloballyPositioned {
        onVisibilityEvent(id, VisibilityEvent.VISIBLE, "onGloballyPositioned ${it.positionInWindow().x} : ${it.positionInWindow().y}")
//        Log.e("TAG","it.isAttached ${it.isAttached}")
//        Log.e("TAG","it.positionInWindow ${it.positionInWindow().x} : ${it.positionInWindow().y}")
    }
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
    LazyColumn {

        for (i in 0 until 100) {
            item {
                Button(onClick = { showComposable = !showComposable }) {
                    Text("Click Me")
                }


                if (!showComposable) {
                    Text(
                        text = "$i I'm now visible $showComposable",
                        modifier = Modifier.impressionLogging(
                            id = "TextComposable",
                            onVisibilityEvent = { impressionId, visibilityEvent, string ->
                                Log.e("TAG", "$i $impressionId $visibilityEvent $string")
                            }
                        )
                    )
                }
            }
        }

    }
}

enum class VisibilityEvent {
    VISIBLE,
    INVISIBLE
}