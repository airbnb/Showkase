package com.airbnb.android.showkasesample

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable
@Composable
fun TestRow() {
    BasicText(text = "TestRow")
}