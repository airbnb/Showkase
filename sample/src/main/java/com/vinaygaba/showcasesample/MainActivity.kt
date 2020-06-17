package com.vinaygaba.showcasesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showcase.ShowcaseCodegenComponents
import com.vinaygaba.showcase.ui.ShowcaseBrowserActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, ShowcaseBrowserActivity::class.java)
            startActivity(intent)
            
        }
    }
}
