package com.airbnb.android.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar

@Composable
internal fun ShowkaseErrorScreen(errorText: String) {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Snackbar(
            modifier = Modifier.padding(padding4x),
            content = {
                Text(
                    text = errorText,
                    modifier = Modifier.padding(padding1x)
                )
            })
    }
}
