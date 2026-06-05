package com.airbnb.android.showkasesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.airbnb.android.showkase.models.Showkase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            startActivity(Showkase.getBrowserIntent(this))
            finish()
        }
    }
}
