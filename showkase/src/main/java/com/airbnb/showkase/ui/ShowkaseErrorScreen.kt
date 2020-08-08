package com.airbnb.showkase.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.ui.unit.dp

@Composable
internal fun ShowkaseErrorScreen(errorText: String) {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Snackbar(modifier = Modifier.padding(16.dp),
            text = {
                Text(
                    text = errorText,
                    modifier = Modifier.padding(4.dp)
                )
            })
    }
}
