package com.airbnb.showkase.annotation.models

import kotlin.reflect.KClass

/**
 * Used by the annotation processor internally to generate metadata files. This won't ever be 
 * needed by any users of Showkase. 
 * 
 * @param moduleName Name of the module that the @Composable function belongs to
 * @param packageName Name of the package that the @Composable function was declared in
 * @param composableMethodName Method name of the @Composable function
 * @param showkaseComposableName User defined name for the @Composable component to render inside
 * the Showkase browser
 * @param showkaseComposableGroup User defined grouping key that will be used to group it with  
 * other @Composable functions.
 * @param enclosingClass If the @Composable function is defined inside a class, the KClass of the
 * wrapper class is stored in the array. Otherwise the array is empty.
 * @param showkaseComposableWidthDp The width that your component will be rendered in inside the 
 * Showkase browser. Use this to restrict the size of your preview inside the Showkase browser.
 * @param showkaseComposableHeightDp The height that your component will be rendered in inside the
 * Showkase browser. Use this to restrict the size of your preview inside the Showkase browser.
 */
@Retention(AnnotationRetention.RUNTIME)
@Suppress("LongParameterList")
annotation class ShowkaseCodegenMetadata(
    val moduleName: String,
    val packageName: String,
    val composableMethodName: String,
    val showkaseComposableName: String,
    val showkaseComposableGroup: String,
    val showkaseComposableKDoc: String,
    val enclosingClass: Array<KClass<*>> = [],
    val showkaseComposableWidthDp: Int = -1,
    val showkaseComposableHeightDp: Int = -1,
    val insideWrapperClass: Boolean = false,
    val insideObject: Boolean = false
)
