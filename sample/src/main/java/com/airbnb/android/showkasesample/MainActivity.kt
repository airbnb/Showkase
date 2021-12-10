package com.airbnb.android.showkasesample

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import com.airbnb.android.showkase.models.Showkase
import com.vinaygaba.showkase_component_impressions.ShowkaseVisibilityEvent
import com.vinaygaba.showkase_component_impressions.visibilityEvents

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicComposableWithVisibilityToggle()
        }
    }
}

@Composable
internal fun BasicComposableWithVisibilityToggle() {
    var counter by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    val list = remember { mutableStateListOf<String>() }
//    var impressionData: ImpressionData? by remember {
//        mutableStateOf(null)
//    }
    Column {
        if(visible) {
            BasicText(
                text = "Testing Impression Events",
                modifier = Modifier.visibilityEvents(
                    key = "key",
                    onVisibilityChanged = { event ->
//                    impressionData = ImpressionData(key, visibilityPercentage, bounds)
                            Log.e("TAG", "$counter ${event.visibilityPercentage}")
                            list.add("$counter ${event.visibilityPercentage}")
                            counter++
                    }
                )
            )
        }

        list.forEach {
            BasicText(
                text = it,
                modifier = Modifier.clickable { visible = !visible }
            )
        }

    }
}