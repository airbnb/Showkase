package com.vinaygaba.showcase.annotation.models

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Suppress("LongParameterList")
annotation class ShowcaseCodegenMetadata(
    val moduleName: String,
    val packageName: String,
    val composableMethodName: String,
    val showcaseComposableName: String,
    val showcaseComposableGroup: String,
    val enclosingClass: Array<KClass<*>> = [],
    val showcaseComposableWidthDp: Int = -1,
    val showcaseComposableHeightDp: Int = -1
)
