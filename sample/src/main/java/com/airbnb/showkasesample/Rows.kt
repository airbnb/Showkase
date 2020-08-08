package com.airbnb.showkasesample

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.drawBackground
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.datasource.LoremIpsum
import com.airbnb.showkase.annotation.models.Showkase

@Composable
fun SimpleRow(title: String, subtitle: String) {
    ShowkaseTheme {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
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
                Box(
                    modifier = Modifier.preferredWidth(60.dp)
                        .preferredHeight(60.dp)
                        .background(color = Color.Gray)
                )
            })
        }
    }
}

@Composable
fun TitleSubtitleThumbnailRow() {
    ShowkaseTheme {
        Card(
            modifier = Modifier.preferredHeight(120.dp)
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            ConstraintLayout {
                val (title, subtitle, image) = createRefs()

                Text("Title", style = TextStyle(
                    fontFamily = FontFamily.Serif, fontWeight =
                    FontWeight.W900, fontSize = 14.sp
                ), modifier = Modifier.constrainAs(title) {
                    start.linkTo(image.end, margin = 16.dp)
                    top.linkTo(image.top)
                })

                Text("Subtitle", style = TextStyle(
                    fontFamily = FontFamily.Serif, fontWeight =
                    FontWeight.W900, fontSize = 14.sp
                ), modifier = Modifier.constrainAs(subtitle) {
                    bottom.linkTo(image.bottom)
                    start.linkTo(image.end, margin = 16.dp)
                })

                Box(modifier = Modifier.preferredHeight(72.dp)
                    .preferredWidth(72.dp)
                    .background(color = Color.Gray)
                    .constrainAs(image) {
                        centerVerticallyTo(parent)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomLabelRow(title: String, subtitle: String, label: String) {
    ShowkaseTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(bottom = 4.dp),
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
                modifier = Modifier.padding(bottom = 8.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            )
            Surface(
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colors.primary
            ) {
                Text(
                    text = label.toUpperCase(),
                    modifier = Modifier.padding(8.dp),
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
            modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colors.surface)
        ) {
            NetworkImage(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxWidth().preferredHeight(200.dp)
            )
            Text(
                text = title,
                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp),
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
                modifier = Modifier.padding(bottom = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            )

        }
    }
}

@Showkase("Bottom Label Row", "Rows")
@Preview
@Composable
fun BottomLabelRowPreview() {
    BottomLabelRow(
        "This is a title",
        "This is a longer subtitle that can span multiple lines",
        "Label Text"
    )
}

@Showkase("Product Card", "Rows")
@Preview
@Composable
fun ProductCardRowPreview() {
    ProductCardRow(
        "http://a0.muscache.com/im/pictures/ef0ee86b-feba-47f8-8882-014b2a7b6926.jpg?im_w=1200",
        "Title",
        LoremIpsum(4).values.joinToString()
    )
}

@Showkase("Simple Row", "Rows")
@Preview
@Composable
fun SimpleRowPreview() {
    SimpleRow(
        "Iron Man",
        "Age: 43"
    )
}

@Showkase("Title Subtitle with Thumbnail", "Rows")
@Preview
@Composable
fun TitleSubtitleThumbnailRowPreview() {
    Column {
        TitleSubtitleThumbnailRow()
    }
}
