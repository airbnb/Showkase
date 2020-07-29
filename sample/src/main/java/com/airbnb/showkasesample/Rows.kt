package com.airbnb.showkasesample

import androidx.compose.Composable
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredWidth
import androidx.ui.material.Card
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.datasource.LoremIpsum
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.airbnb.showkase.annotation.models.Showkase

@Composable
fun SimpleRow(title: String, subtitle: String) {
    ShowkaseTheme {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.fillMaxWidth() + Modifier.padding(8.dp)
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
                    modifier = Modifier.preferredWidth(60.dp) +
                            Modifier.preferredHeight(60.dp) +
                            Modifier.drawBackground(Color.Gray)
                )
            })
        }
    }
}

@Composable
fun TitleSubtitleThumbnailRow() {
    ShowkaseTheme {
        Card(
            modifier = Modifier.preferredHeight(120.dp) + Modifier.fillMaxWidth() + Modifier.padding(
                8.dp
            ),
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

                Box(modifier = Modifier.preferredHeight(72.dp) + Modifier.preferredWidth(72.dp) +
                        Modifier.drawBackground(Color.Gray) +
                        Modifier.constrainAs(image) {
                            centerVerticallyTo(parent)
                            start.linkTo(parent.start, margin = 16.dp)
                        }) {

                }

            }
        }
    }
}

@Composable
fun BottomLabelRow(title: String, subtitle: String, label: String) {
    ShowkaseTheme {
        Column(
            modifier = Modifier.fillMaxWidth() +
                    Modifier.drawBackground(MaterialTheme.colors.surface) +
                    Modifier.padding(16.dp)
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
            modifier = Modifier.fillMaxWidth() +
                    Modifier.drawBackground(MaterialTheme.colors.surface)
        ) {
            NetworkImage(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxWidth() + Modifier.preferredHeight(200.dp)
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

//@Showkase("Bottom Label Row", "Rows")
@Preview("Bottom Label Row", "Rows")
@Composable
fun BottomLabelRowPreview() {
    BottomLabelRow(
        "This is a title",
        "This is a longer subtitle that can span multiple lines",
        "Label Text"
    )
}

//@Showkase("Product Card", "Rows")
@Preview("Product Card", "Rows")
@Composable
fun ProductCardRowPreview() {
    ProductCardRow(
        "http://a0.muscache.com/im/pictures/ef0ee86b-feba-47f8-8882-014b2a7b6926.jpg?im_w=1200",
        "Title",
        LoremIpsum(4).values.joinToString()
    )
}

//@Showkase("Simple Row", "Rows")
@Preview("Simple Row", "Rows")
@Composable
fun SimpleRowPreview() {
    SimpleRow(
        "Iron Man",
        "Age: 43"
    )
}

//@Showkase("Title Subtitle with Thumbnail", "Rows")
@Preview("Title Subtitle with Thumbnail", "Rows")
@Composable
fun TitleSubtitleThumbnailRowPreview() {
    Column {
        TitleSubtitleThumbnailRow()
    }
}
