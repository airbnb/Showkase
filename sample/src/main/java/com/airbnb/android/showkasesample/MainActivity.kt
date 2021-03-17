package com.airbnb.android.showkasesample

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            startActivity(ShowkaseMethods.createShowkaseBrowserIntent(this))
        }
    }
}
