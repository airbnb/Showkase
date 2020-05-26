package com.vinaygaba.showcase.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.padding
import androidx.ui.material.Snackbar
import androidx.ui.unit.dp

@Composable
fun ShowcaseErrorScreen(errorText: String) {
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
