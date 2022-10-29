package com.airbnb.android.showkasesample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.android.submodule.showkasesample.CircleShapePreview
import com.airbnb.android.submodule.two.showkasesample.SquarePreview

@Preview(name = "Square 200 by 200", group = "Shape", widthDp = 200, heightDp = 200)
@Preview(name = "Square 400 by 400", group = "Shape", widthDp = 400, heightDp = 400)
annotation class ShapePreview

@Preview(
    name = "Phone 200 by 200",
    group = "Shape",
    widthDp = 200,
    heightDp = 200,
    device = Devices.PHONE
)
@Preview(
    name = "Tablet 400 by 400",
    group = "Shape",
    widthDp = 400,
    heightDp = 400,
    device = Devices.TABLET
)
annotation class DevicePreview

class ShapeColorProvider : PreviewParameterProvider<Color> {
    override val values: Sequence<Color>
        get() = sequenceOf(
            Color.Red, Color.Blue
        )

    override val count: Int
        get() = super.count
}

@CircleShapePreview
@Composable
fun CirclePreview(
    @PreviewParameter(ShapeColorProvider::class) color: Color
) {
    Box(
        Modifier
            .clip(CircleShape)
            .size(100.dp)
            .background(color)
    )
}

/**
 * This is some KDoc to show how to implement our nice custom
 * composable
 */
@Preview(name = "Desktop Preview", group = "Shape", device = Devices.DESKTOP)
@DevicePreview
@ShapePreview
@Composable
fun RoundRectanglePreview(
    @PreviewParameter(ShapeColorProvider::class) color: Color
) {
    Box(
        Modifier
            .clip(CircleShape)
            .size(width = 100.dp, height = 50.dp)
            .background(color = color)
    )
}

@ShapePreview
@SquarePreview
@Composable
fun SquarePreview(
    @PreviewParameter(ShapeColorProvider::class) color: Color,
) {
    Box(
        Modifier
            .background(color)
            .fillMaxSize()
            .clip(RectangleShape)
    )
}
