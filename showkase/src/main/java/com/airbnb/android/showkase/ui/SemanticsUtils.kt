package com.airbnb.android.showkase.ui

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

object SemanticsUtils {
    val LineCountKey = SemanticsPropertyKey<Int>("lineCount")
    var SemanticsPropertyReceiver.lineCountVal by LineCountKey
}
