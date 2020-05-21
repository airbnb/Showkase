package com.vinaygaba.annotation

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class Showcase(
    val name: String = "",
    val group: String = "",
    val apiLevel: Int = -1,
    val theme: String = "",
    // TODO(mount): Make this Dp when they are inline classes
    val widthDp: Int = -1,
    // TODO(mount): Make this Dp when they are inline classes
    val heightDp: Int = -1,
    val locale: String = "",
    val fontScale: Float = 1f,
    val showDecoration: Boolean = false,
    val showBackground: Boolean = false
)
