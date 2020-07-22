package com.airbnb.submodule

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.input.KeyboardType
import androidx.ui.input.OffsetMap
import androidx.ui.input.TextFieldValue
import androidx.ui.input.TransformedText
import androidx.ui.input.VisualTransformation
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.aspectRatio
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.layout.preferredWidth
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.text.AnnotatedString
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.airbnb.showkase.annotation.models.Showkase

@Composable
fun CreditCardComponent(cardName: String) {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.preferredWidth(300.dp) + Modifier.aspectRatio(16 / 9f),
                color = MaterialTheme.colors.primary
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    var textValue by state { "1234567812345678" }
                    TextField(value = TextFieldValue(textValue),
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
            AnnotatedString(text.text.replace("....".toRegex(), "$0 ")),
            creditCardOffsetMap
        )
    }
}

val creditCardOffsetMap = object : OffsetMap {
    override fun originalToTransformed(offset: Int) = offset + (offset / 4)
    override fun transformedToOriginal(offset: Int) = offset - (offset / 4)
}

@Showkase("Credit Card Component", "Payments")
@Preview
@Composable
fun CreditCardComponentPreview() {
    CreditCardComponent("John Doe")
}
