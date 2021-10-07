package com.airbnb.android.showkase.annotation

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ShowkaseRootCodegen(
    val noOfComposablesWithoutPreviewParameter: Int,
    val noOfComposablesWithPreviewParameter: Int,
    val noOfColors: Int,
    val noOfTypography: Int
)
