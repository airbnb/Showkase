package com.airbnb.android.showkasesample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    navigateToShowkaseActivity: () -> Unit,
    navigateToShowkaseFragment: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = navigateToShowkaseActivity) {
            Text(text = "Open Showkase activity")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = navigateToShowkaseFragment) {
            Text(text = "Open Showkase fragment")
        }
    }
}