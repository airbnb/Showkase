package com.vinaygaba.annotation

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class Showcase(
    val name: String = "",
    val group: String = ""
)
