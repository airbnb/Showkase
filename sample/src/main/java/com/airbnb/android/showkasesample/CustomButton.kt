package com.airbnb.android.showkasesample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    size: ButtonSize = ButtonSize.Large,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(getPadding(size)),
            style = TextStyle(color = Color.White)
        )
    }
}

fun getPadding(
    size: ButtonSize
) = when (size) {
    ButtonSize.Large -> PaddingValues(vertical = 16.dp, horizontal = 32.dp)
    ButtonSize.Medium -> PaddingValues(vertical = 14.dp, horizontal = 24.dp)
    ButtonSize.Small -> PaddingValues(vertical = 7.dp, horizontal = 16.dp)
}

@ShowkaseComposable(name = "CustomButton", group = "Buttons", defaultStyle = true)
@Composable
fun PreviewCustomButtonDefault() {
    CustomButton(
        text = "Button",
        onClick = { }
    )
}

@ShowkaseComposable(name = "CustomButton", group = "Buttons", styleName = "Medium")
@Composable
fun PreviewCustomButtonMedium() {
    CustomButton(
        text = "Button",
        onClick = { },
        size = ButtonSize.Medium
    )
}

@ShowkaseComposable(name = "CustomButton", group = "Buttons", styleName = "Small")
@Composable
private fun PreviewCustomButtonSmall() {
    CustomButton(
        text = "Button",
        onClick = { },
        size = ButtonSize.Small
    )
}

@Suppress("MatchingDeclarationName")
enum class ButtonSize {
    Large,
    Medium,
    Small
}
