package com.airbnb.android.showkase.annotation

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ShowkaseRootCodegen(
    val numComposablesWithoutPreviewParameter: Int,
    val numComposablesWithPreviewParameter: Int,
    val numColors: Int,
    val numTypography: Int
)
