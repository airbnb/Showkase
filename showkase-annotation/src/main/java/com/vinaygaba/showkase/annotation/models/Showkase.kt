package com.vinaygaba.showkase.annotation.models

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class Showkase(
    val name: String,
    val group: String,
    val widthDp: Int = -1,
    val heightDp: Int = -1
)
