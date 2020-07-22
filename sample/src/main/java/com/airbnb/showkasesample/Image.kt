package com.airbnb.showkasesample

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.WithConstraints
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Image
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.layout.fillMaxWidth
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun NetworkImage(imageUrl: String,
                 modifier: Modifier = Modifier.fillMaxWidth()
) {
    Box(
        modifier = modifier,
        gravity = ContentGravity.Center
    ) {
        WithConstraints {
            val constrainScope = this
            var image by state<ImageAsset?> { null }
            var drawable by state<Drawable?> { null }
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
                    drawCanvas { canvas, _ ->
                        theDrawable.draw(canvas.nativeCanvas)
                    }
                }
            }
        }
    }
}
