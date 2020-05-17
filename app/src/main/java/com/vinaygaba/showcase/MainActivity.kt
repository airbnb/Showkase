package com.vinaygaba.showcase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.browser.ShowcaseBrowserActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, ShowcaseBrowserActivity::class.java)
            startActivity(intent)
        }
    }
}


