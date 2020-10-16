package com.airbnb.android.showkasesample

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WithConstraints {
            val constrainScope = this
            var image by remember { mutableStateOf<ImageAsset?>(null) }
            var drawable by remember { mutableStateOf<Drawable?>(null) }
            onCommit(imageUrl) {
                val picasso = Picasso.get()
                val target = object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        drawable = placeHolderDrawable
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        drawable = errorDrawable
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        image = bitmap?.asImageAsset()
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
                Image(asset = theImage)
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
