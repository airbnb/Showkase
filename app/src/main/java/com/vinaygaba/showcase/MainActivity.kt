package com.vinaygaba.showcase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.vinaygaba.annotation.Showcase
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Showcase(name = "Test Name1", apiLevel = 24, group = "Group1", fontScale = 1f)
@Preview()
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Greeting("Android1")
    }
}

@Showcase(name = "Test Name2", apiLevel = 24, group = "Group1", fontScale = 1f)
@Preview
@Composable
fun Preview2() {
    MaterialTheme {
        Greeting("Android2")
    }
}


