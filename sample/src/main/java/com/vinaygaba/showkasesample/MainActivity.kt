package com.vinaygaba.showkasesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showkase.ui.ShowkaseBrowserActivity

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
