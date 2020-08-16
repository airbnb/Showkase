package com.airbnb.android.showkasesample

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography

@ShowkaseTypography("","")
val headline = TextStyle(
    fontWeight = FontWeight.Light,
    fontSize = 96.sp,
    letterSpacing = (-1.5).sp
)


@ShowkaseTypography("","")
val color = TextStyle(
    color = Color.Black
)
