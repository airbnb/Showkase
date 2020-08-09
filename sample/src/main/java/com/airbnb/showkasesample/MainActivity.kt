package com.airbnb.showkasesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.airbnb.showkase.ui.ShowkaseBrowserActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            startActivity(
                ShowkaseBrowserActivity.getIntent(this, RootModule::class.java.canonicalName!!)
            )
        }
    }
}
