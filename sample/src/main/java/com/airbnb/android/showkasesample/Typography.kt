package com.airbnb.android.showkasesample

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography

@ShowkaseTypography("h1", "Material Design")
val h1 = TextStyle(
fontWeight = FontWeight.Light,
fontSize = 96.sp,
letterSpacing = (-1.5).sp
)

@ShowkaseTypography("h2", "Material Design")
val h2 = TextStyle(
fontWeight = FontWeight.Light,
fontSize = 60.sp,
letterSpacing = (-0.5).sp
)

@ShowkaseTypography("h3", "Material Design")
val h3 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 48.sp,
letterSpacing = 0.sp
)

@ShowkaseTypography("h4", "Material Design")
val h4 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 34.sp,
letterSpacing = 0.25.sp
)

@ShowkaseTypography("h5", "Material Design")
val h5 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 24.sp,
letterSpacing = 0.sp
)

@ShowkaseTypography("h6", "Material Design")
val h6 = TextStyle(
fontWeight = FontWeight.Medium,
fontSize = 20.sp,
letterSpacing = 0.15.sp
)

@ShowkaseTypography("subtitle1", "Material Design")
val subtitle1 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 16.sp,
letterSpacing = 0.15.sp
)

@ShowkaseTypography("subtitle2", "Material Design")
val subtitle2 = TextStyle(
fontWeight = FontWeight.Medium,
fontSize = 14.sp,
letterSpacing = 0.1.sp
)

@ShowkaseTypography("body1", "Material Design")
val body1 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 16.sp,
letterSpacing = 0.5.sp
)

@ShowkaseTypography("body2", "Material Design")
val body2 = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 14.sp,
letterSpacing = 0.25.sp
)

@ShowkaseTypography("button", "Material Design")
val button = TextStyle(
fontWeight = FontWeight.Medium,
fontSize = 14.sp,
letterSpacing = 1.25.sp
)

@ShowkaseTypography("caption", "Material Design")
val caption = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 12.sp,
letterSpacing = 0.4.sp
)

@ShowkaseTypography("overline", "Material Design")
val overline = TextStyle(
fontWeight = FontWeight.Normal,
fontSize = 10.sp,
letterSpacing = 1.5.sp
)
