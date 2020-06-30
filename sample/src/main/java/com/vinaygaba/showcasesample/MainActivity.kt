package com.vinaygaba.showcasesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showcase.ui.ShowcaseBrowserActivity
import com.vinaygaba.showcase.ui.ShowcaseBrowserActivity.Companion.SHOWCASE_ROOT_MODULE

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, ShowcaseBrowserActivity::class.java)
            intent.putExtra(SHOWCASE_ROOT_MODULE, RootModule::class.java.canonicalName)
            startActivity(intent)
        }
    }
}
