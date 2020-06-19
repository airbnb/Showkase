package com.vinaygaba.showcase.annotation.models

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class ShowcaseCodegen(
    val name: String,
    val group: String,
    val widthDp: Int = -1,
    val heightDp: Int = -1,
    val packageName: String, 
    val composableMethodName: String, 
    val enclosingClass: Array<KClass<*>> = []
)
