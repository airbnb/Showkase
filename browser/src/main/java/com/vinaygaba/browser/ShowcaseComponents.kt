package com.vinaygaba.browser

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.tooling.preview.Preview
import com.vinaygaba.annotation.Showcase

val componentsMap: Map<String, List<@Composable() () -> Unit>> = mutableMapOf<String, List<@Composable() () -> Unit>>(
    "Plus" to listOf<@Composable() () -> Unit>(@Composable { TextPreview1() }),
    "Payments" to listOf<@Composable() () -> Unit>(@Composable { TextPreview1() }, @Composable { TextPreview2() }, @Composable { TextPreview3() }, @Composable { TextPreview4() }),
    "Lux" to listOf<@Composable() () -> Unit>(@Composable { TextPreview3() }, @Composable { TextPreview4() })
)

val componentList = listOf<ShowcaseCodegenMetadata>(
    ShowcaseCodegenMetadata("Plus", "Component1", @Composable { TextPreview1() }),
    ShowcaseCodegenMetadata("Payments", "Component2", @Composable { TextPreview1() }),
    ShowcaseCodegenMetadata("Payments", "Component3", @Composable { TextPreview2() }),
    ShowcaseCodegenMetadata("Payments", "Component4", @Composable { TextPreview3() })
)

@Composable
fun Preview() {
    TextPreview1()
}

@Preview
@Composable
fun TextPreview1() {
    Text(text = "Hello")
}

@Preview
@Composable
fun TextPreview2() {
    Text(text = "World!")
}

@Preview
@Composable
fun TextPreview3() {
    Text(text = "How")
}

@Preview
@Composable
fun TextPreview4() {
    Text(text = "Are?")
}
