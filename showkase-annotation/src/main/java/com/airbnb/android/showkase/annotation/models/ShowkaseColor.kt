package com.airbnb.android.showkase.annotation.models

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseColor(
    val name: String,
    val group: String,
)
