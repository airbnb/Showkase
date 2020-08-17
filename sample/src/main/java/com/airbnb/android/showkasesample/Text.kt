package com.airbnb.android.showkasesample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.Showkase

/**
 * This component shows some static text in cursive text style. 
 * 
 * Example usage:
 *
 * ```
 * @Composable
 * fun MyComposable() {
 *     CursiveTextComponentPreview()
 * }
 * ```
 */
@Showkase(name = "Cursive Text Style", group = "Text")
@Preview
@Composable
fun CursiveTextComponent() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth().padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive)
            )
        }
    }
}

@Showkase(name = "Serif Text Style", group = "Text")
@Preview
@Composable
fun SerifTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth().padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif)
            )
        }
    }
}

@Showkase(name = "Sans Serif Text Style", group = "Text")
@Preview
@Composable
fun SansSerifTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth().padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            )
        }
    }
}
