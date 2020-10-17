package com.airbnb.android.submodule

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.CoreTextField
import androidx.compose.runtime.Composable
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.ui.tooling.preview.PreviewParameter
import androidx.ui.tooling.preview.PreviewParameterProvider
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding4x

@Composable
fun CreditCardComponent(cardName: String) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(), 
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(padding4x),
                modifier = Modifier.preferredWidth(300.dp).aspectRatio(16 / 9f),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    var textValue by remember { mutableStateOf("1234567812345678") }
                    CoreTextField(value = TextFieldValue(textValue),
                        modifier = Modifier.padding(padding4x),
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
                        modifier = Modifier.padding(padding4x),
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

@ShowkaseComposable("Credit Card Component", "Payments")
@Preview
@Composable
fun CreditCardComponentPreview() {
    CreditCardComponent("John Doe")
}


data class BankHeader(
    val name: String,
    val age: Int
)

class NewParameterProvider : PreviewParameterProvider<BankHeader> {
    override val values: Sequence<BankHeader>
        get() = sequenceOf(
            BankHeader("Citi", 12),
            BankHeader("Goldman Sachs", 20)
        )

    override val count: Int
        get() = super.count

}

@Preview(group = "Payments")
@Composable
fun BankHeaderPreview(
    @PreviewParameter(provider = NewParameterProvider::class) bankHeader: BankHeader
) {
    MaterialTheme {
        Card {
            Text(
                text = bankHeader.name,
                modifier = Modifier.fillMaxWidth().padding(padding4x),
                style = MaterialTheme.typography.h2
            )
        }
    }
}
