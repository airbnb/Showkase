package com.airbnb.android.showkase.annotation

import kotlin.reflect.KClass

/**
 * Used by the annotation processor internally to generate metadata files. This won't ever be 
 * needed by any users of Showkase. 
 * 
 * @param packageSimpleName Name of the module that the @Composable function belongs to
 * @param packageName Name of the package that the @Composable function was declared in
 * @param showkaseElementName Method name of the @Composable function
 * @param showkaseName User defined name for the @Composable component to render inside
 * the Showkase browser
 * @param showkaseGroup User defined grouping key that will be used to group it with  
 * other @Composable functions.
 * @param enclosingClass If the @Composable function is defined inside a class, the KClass of the
 * wrapper class is stored in the array. Otherwise the array is empty.
 * @param showkaseWidthDp The width that your component will be rendered in inside the 
 * Showkase browser. Use this to restrict the size of your preview inside the Showkase browser.
 * @param showkaseHeightDp The height that your component will be rendered in inside the
 * Showkase browser. Use this to restrict the size of your preview inside the Showkase browser.
 */
@Retention(AnnotationRetention.RUNTIME)
@Suppress("LongParameterList")
annotation class ShowkaseCodegenMetadata(
    val packageSimpleName: String,
    val packageName: String,
    val showkaseElementName: String,
    val showkaseName: String,
    val showkaseGroup: String,
    val showkaseKDoc: String,
    val showkaseMetadataType: String,
    val enclosingClass: Array<KClass<*>> = [],
    val showkaseWidthDp: Int = -1,
    val showkaseHeightDp: Int = -1,
    val insideWrapperClass: Boolean = false,
    val insideObject: Boolean = false,
    val previewParameterClass: Array<KClass<*>> = [],
)
