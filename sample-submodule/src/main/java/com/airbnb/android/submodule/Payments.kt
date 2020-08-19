package com.airbnb.android.submodule

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.CoreTextField
import androidx.compose.runtime.Composable
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.state
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseComponent

@Composable
fun CreditCardComponent(cardName: String) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.preferredWidth(300.dp).aspectRatio(16 / 9f),
                color = MaterialTheme.colors.primary
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    var textValue by state { "1234567812345678" }
                    CoreTextField(value = TextFieldValue(textValue),
                        modifier = Modifier.padding(16.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onPrimary,
                            fontFamily = FontFamily.Serif,
                            fontSize = 25.sp, fontWeight = FontWeight.Bold
                        ),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = CreditCardVisualTransformation(),
                        // Update value of textValue with the latest value of the text field
                        onValueChange = { newTextValue ->
                            textValue = newTextValue.text
                        }
                    )
                    Text(
                        text = cardName,
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            // Regex used to add a space after every 4 characters.
            AnnotatedString(text.text.replace(creditCardRegex, "$0 ")),
            creditCardOffsetMap
        )
    }
}

val creditCardRegex by lazy {
    "....".toRegex()
}

val creditCardOffsetMap = object : OffsetMap {
    override fun originalToTransformed(offset: Int) = offset + (offset / 4)
    override fun transformedToOriginal(offset: Int) = offset - (offset / 4)
}

@ShowkaseComponent("Credit Card Component", "Payments")
@Preview
@Composable
fun CreditCardComponentPreview() {
    CreditCardComponent("John Doe")
}
