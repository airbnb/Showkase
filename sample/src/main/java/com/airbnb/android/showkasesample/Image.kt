package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints {
            val constrainScope = this
            var image by remember { mutableStateOf<ImageBitmap?>(null) }
            var drawable by remember { mutableStateOf<Drawable?>(null) }
            DisposableEffect(imageUrl) {
                val picasso = Picasso.get()
                val target = object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        drawable = placeHolderDrawable
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        drawable = errorDrawable
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        image = bitmap?.asImageBitmap()
                    }
                }

                picasso
                    .load(imageUrl)
                    .resize(constrainScope.constraints.maxWidth, constrainScope.constraints.maxHeight)
                    .centerCrop()
                    .into(target)

                onDispose {
                    image = null
                    drawable = null
                    picasso.cancelRequest(target)
                }
            }

            val theImage = image
            val theDrawable = drawable
            if (theImage != null) {
                Image(bitmap = theImage, contentDescription = null)
            } else if (theDrawable != null) {
                Canvas(modifier = modifier) {
                    drawIntoCanvas { canvas ->
                        theDrawable.draw(canvas.nativeCanvas)
                    }
                }
            }
        }
    }
}
