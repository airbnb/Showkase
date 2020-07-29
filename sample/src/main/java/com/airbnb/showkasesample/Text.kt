package com.airbnb.showkasesample

import androidx.compose.Composable
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.airbnb.showkase.annotation.models.Showkase

@Showkase(name = "Cursive Text Style", group = "Text")
@Preview(name = "Cursive Text Style", group = "Text")
@Composable
fun CursiveTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
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
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
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
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            )
        }
    }
}
