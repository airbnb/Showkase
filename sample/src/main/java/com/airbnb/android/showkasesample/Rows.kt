package com.airbnb.android.showkasesample

import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.ui.padding1x
import com.airbnb.android.showkase.ui.padding2x
import com.airbnb.android.showkase.ui.padding4x
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleRow(title: String, subtitle: String) {
    ShowkaseTheme {
        Card(
            shape = RoundedCornerShape(padding1x),
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding2x)
        ) {
            ListItem(text = {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, secondaryText = {
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 15.sp,
                        fontWeight = FontWeight.Light, color = Color.DarkGray
                    )
                )
            }, icon = {
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .background(color = Color.Gray)
                ) {}
            })
        }
    }
}

@Composable
fun TitleSubtitleThumbnailRow() {
    ShowkaseTheme {
        Card(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(padding2x),
            shape = RoundedCornerShape(padding1x)
        ) {
            ConstraintLayout {
                val (title, subtitle, image) = createRefs()

                Text("Title", style = TextStyle(
                    fontFamily = FontFamily.Serif, fontWeight =
                    FontWeight.W900, fontSize = 14.sp
                ), modifier = Modifier.constrainAs(title) {
                    start.linkTo(image.end, margin = padding4x)
                    top.linkTo(image.top)
                })

                Text("Subtitle", style = TextStyle(
                    fontFamily = FontFamily.Serif, fontWeight =
                    FontWeight.W900, fontSize = 14.sp
                ), modifier = Modifier.constrainAs(subtitle) {
                    bottom.linkTo(image.bottom)
                    start.linkTo(image.end, margin = padding4x)
                })

                Column(modifier = Modifier
                    .height(72.dp)
                    .width(72.dp)
                    .background(color = Color.Gray)
                    .constrainAs(image) {
                        centerVerticallyTo(parent)
                        start.linkTo(parent.start, margin = padding4x)
                    }
                ) {}
            }
        }
    }
}

@Composable
fun BottomLabelRow(title: String, subtitle: String, label: String) {
    ShowkaseTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .padding(padding4x)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(bottom = padding1x),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                modifier = Modifier.padding(bottom = padding2x),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            )
            Surface(
                shape = RoundedCornerShape(padding1x / 2),
                color = MaterialTheme.colors.primary
            ) {
                Text(
                    text = label.uppercase(Locale.getDefault()),
                    modifier = Modifier.padding(padding2x),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Thin
                    )
                )
            }
        }
    }
}

@Composable
fun ProductCardRow(imageUrl: String, title: String, subtitle: String) {
    ShowkaseTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
        ) {
            NetworkImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = title,
                modifier = Modifier.padding(bottom = padding1x, top = padding1x),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                modifier = Modifier.padding(bottom = padding2x),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            )
        }
    }
}

@ShowkaseComposable("Bottom Label Row", "Rows")
@Preview
@Composable
fun BottomLabelRowPreview() {
    BottomLabelRow(
        "This is a title",
        "This is a longer subtitle that can span multiple lines",
        "Label Text"
    )
}

/**
 * Use this card when you want to render a product with a hero image at the top.
 *
 * Example usage:
 *
 * ```
 * @Composable
 * fun MyComposable() {
 *     ProductCardRow()
 * }
 * ```
 */
@ShowkaseComposable("Product Card", "Rows")
@Preview
@Composable
fun ProductCardRowPreview() {
    ProductCardRow(
        "http://a0.muscache.com/im/pictures/ef0ee86b-feba-47f8-8882-014b2a7b6926.jpg?im_w=1200",
        "Title",
        LoremIpsum(4).values.joinToString()
    )
}

@ShowkaseComposable("Simple Row", "Rows")
@Preview
@Composable
fun SimpleRowPreview() {
    SimpleRow(
        "Iron Man",
        "Age: 43"
    )
}

@ShowkaseComposable("Title Subtitle with Thumbnail", "Rows")
@Preview
@Composable
fun TitleSubtitleThumbnailRowPreview() {
    Column {
        TitleSubtitleThumbnailRow()
    }
}

@ShowkaseComposable@ShowkaseComposable("Title Subtitle with Thumbnail and Back Handler", "Rows")
@Preview
@Composable
fun TitleSubtitleThumbnailRowWithBackHandlingPreview() {
    Column {
        TitleSubtitleThumbnailRow()
    }
    BackHandler(onBack = {})
}
