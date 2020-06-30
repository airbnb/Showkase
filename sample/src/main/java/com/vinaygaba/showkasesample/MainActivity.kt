package com.vinaygaba.showkasesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.vinaygaba.showkase.ui.ShowkaseBrowserActivity
import com.vinaygaba.showkase.ui.ShowkaseBrowserActivity.Companion.SHOWKASE_ROOT_MODULE_KEY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, ShowkaseBrowserActivity::class.java)
            intent.putExtra(SHOWKASE_ROOT_MODULE_KEY, RootModule::class.java.canonicalName)
            startActivity(intent)
        }
    }
}
