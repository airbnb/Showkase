package com.airbnb.android.showkasesample

import androidx.compose.material.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding4x
import com.airbnb.android.submodule.showkasesample.LocalePreview

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
@ShowkaseComposable(name = "Cursive Text Style", group = "Text")
@Preview
@Composable
fun CursiveTextComponent() {
    val context = LocalContext.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(
                text = string, modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
            )
        }
    }
}

@ShowkaseComposable(name = "Serif Text Style", group = "Text")
@Preview
@Composable
fun SerifTextComponentPreview() {
    val context = LocalContext.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(
                text = string, modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            )
        }
    }
}

@ShowkaseComposable(name = "Sans Serif Text Style", group = "Text")
@Preview
@Composable
fun SansSerifTextComponentPreview() {
    val context = LocalContext.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(
                text = string, modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            )
        }
    }
}

@ShowkaseComposable(name = "H4 Text Row", group = "Text")
@Composable
fun H4TextRowComponentPreview() {
    val context = LocalContext.current
    val string = context.getString(R.string.app_name)

    ShowkaseTheme {
        Card {
            Text(
                text = string,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = Material.h4
            )
        }
    }
}

data class Person(
    val name: String,
    val age: Int
)

class ParameterProvider : PreviewParameterProvider<Person> {
    override val values: Sequence<Person>
        get() = sequenceOf(
            Person("John", 12),
            Person("Doe", 20)
        )

    override val count: Int
        get() = super.count
}

@Composable
fun H6TextRowComponent(text: String) {
    ShowkaseTheme {
        Card {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding4x),
                style = Material.h6
            )
        }
    }
}

@Preview(name = "H6 Text Row", group = "Text")
@Composable
fun H6TextRowComponentPreview(
    age: String = "15",
    @PreviewParameter(provider = ParameterProvider::class) person: Person
) {
    H6TextRowComponent(person.name)
}

// This is here for testing, generating this code into function names would
// fail dex generation
@Preview(name = "H6 Text Row & special chars !@#$%^&*()_+", group = "Text")
@Composable
fun H6TextRowComponentPreviewWithSpecialCharInPreview(
    age: String = "15",
    @PreviewParameter(provider = ParameterProvider::class) person: Person
) {
    H6TextRowComponent(person.name)
}

@LocalePreview
@Composable
fun EnglishText() {
    Text(text = "Some english text")
}
