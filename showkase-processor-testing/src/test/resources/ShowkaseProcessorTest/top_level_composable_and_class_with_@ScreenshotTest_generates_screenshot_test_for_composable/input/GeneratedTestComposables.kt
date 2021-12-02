package com.airbnb.android.showkase_processor_testing

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name= "name1", group = "group1")
@Composable
fun TestComposable1() {
    
}

@ShowkaseComposable(name= "name2", group = "group2")
@Composable
fun TestComposable2() {
    
}