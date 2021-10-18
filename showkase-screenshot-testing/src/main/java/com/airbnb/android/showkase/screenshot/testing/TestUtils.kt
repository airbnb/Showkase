package com.airbnb.android.showkase.screenshot.testing

import android.content.Context
import android.os.Build

internal fun getScreenDensityName(context: Context): String {
    val density: Float = context.resources.displayMetrics.density
    return when {
        density >= 4.0 -> "xxxhdpi"
        density >= 3.0 -> "xxhdpi"
        density >= 2.0 -> "xhdpi"
        density >= 1.5 -> "hdpi"
        density >= 1.0 -> "mdpi"
        else -> "ldpi"
    }
}

internal fun getAndroidAPIVersion() = android.os.Build.VERSION.SDK_INT

internal fun getAndroidABI() = Build.SUPPORTED_ABIS[0]

internal fun getScreenDimensions(context: Context): Pair<Int, Int> {
    val width = context.resources.displayMetrics.widthPixels
    val height = context.resources.displayMetrics.heightPixels
    return width to height
}