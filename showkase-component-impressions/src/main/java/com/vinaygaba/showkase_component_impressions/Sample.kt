package com.vinaygaba.showkase_component_impressions

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp

@Composable
fun TestScreen() {
    var showComposable by remember {
        mutableStateOf(false)
    }
    Column {
        ClickableText(text = AnnotatedString("Click Me"), onClick = { showComposable = !showComposable })
        if (!showComposable) {
            BasicText(
                text = "I'm now visible $showComposable",
                modifier = Modifier.visibilityImpressions(
                    key = "TextComposable",
                    onVisibilityEvent = { impressionId, visibilityPercentage ->
                        Log.e("TAG", "$impressionId $visibilityPercentage")
                    }
                )

            )

//            BasicText(
//                text = "I'm now visible $showComposable",
//                modifier = Modifier
//                    .defaultMinSize(1920.dp)
//                    .visibilityImpressions(
//                    key = "TextComposable2",
//                    onVisibilityEvent = { impressionId, visibilityPercentage ->
//                        Log.e("TAG", "$impressionId $visibilityPercentage")
//                    }
//                )
//
//            )
        }
    }
//        val listState = rememberLazyListState()
//    LazyColumn(state = listState) {
//        listState.firstVisibleItemIndex
//
//        for (i in 0 until 100) {
//            item {
//                ClickableText(text = AnnotatedString("Click Me"), onClick = { showComposable = !showComposable })
//
//                if (!showComposable) {
//                    BasicText(
//                        text = "$i I'm now visible $showComposable",
//                        modifier = Modifier.visibilityImpressions(
//                            key = "TextComposable $i",
//                            onVisibilityEvent = { impressionId, visibilityEvent, string ->
//                                Log.e("TAG", "$i $impressionId $visibilityEvent $string")
//                            }
//                        )
//                        modifier = Modifier.impression(
//                            key = "TextComposable$i",
//                            onImpression = { key ->
//                                Log.e("TAG", "$i $key")
//                            }
//                        )
//                    )
//                }
//            }
//        }
//    }
}




//                        modifier = Modifier.impression(
//                            key = "TextComposable$i",
//                            onImpression = { key ->
//                                Log.e("TAG", "$i $key")
//                            }
//                        )


//    val listState = rememberLazyListState()
//    LazyColumn(state = listState) {
//        listState.firstVisibleItemIndex
//
//        for (i in 0 until 100) {
//            item {
//                Button(onClick = { showComposable = !showComposable }) {
//                    Text("Click Me")
//                }
//
//                if (!showComposable) {
//                    Text(
//                        text = "$i I'm now visible $showComposable",
//                        modifier = Modifier.impressionLogger(
//                            key = "TextComposable $i",
//                            onVisibilityEvent = { impressionId, visibilityEvent, string ->
//                                Log.e("TAG", "$i $impressionId $visibilityEvent $string")
//                            }
//                        )
////                        modifier = Modifier.impression(
////                            key = "TextComposable$i",
////                            onImpression = { key ->
////                                Log.e("TAG", "$i $key")
////                            }
////                        )
//                    )
//                }
//            }
//        }
//}