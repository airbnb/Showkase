package com.vinaygaba.showcase.test1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.vinaygaba.annotation.Showcase
import com.vinaygaba.browser.ShowcaseBrowserApp
import composableMap

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ShowcaseBrowserApp(groupedComponentMap = composableMap)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Showcase(name = "Test Name1", apiLevel = 24, group = "Group1", fontScale = 1f)
@Preview
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


