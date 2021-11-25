package com.airbnb.android.showkasesample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.android.showkase.models.Showkase
import com.vinaygaba.showkase_component_impressions.TestScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            startActivity(Showkase.getBrowserIntent(this))
            TestScreen()
        }
    }
}
