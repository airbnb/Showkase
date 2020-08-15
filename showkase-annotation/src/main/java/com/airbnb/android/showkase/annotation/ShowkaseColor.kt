package com.airbnb.android.showkase.annotation

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseColor(
    val name: String,
    val group: String,
)
