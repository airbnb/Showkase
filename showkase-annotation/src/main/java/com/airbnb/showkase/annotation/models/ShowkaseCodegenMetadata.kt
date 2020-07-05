package com.airbnb.showkase.annotation.models

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Suppress("LongParameterList")
annotation class ShowkaseCodegenMetadata(
    val moduleName: String,
    val packageName: String,
    val composableMethodName: String,
    val showkaseComposableName: String,
    val showkaseComposableGroup: String,
    val enclosingClass: Array<KClass<*>> = [],
    val showkaseComposableWidthDp: Int = -1,
    val showkaseComposableHeightDp: Int = -1
)
