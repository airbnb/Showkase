package com.airbnb.android.showkase.annotation

/**
 * Used to annotate [androidx.compose.ui.graphics.Color] properties that should be presented inside
 * the Showkase browser. Here's how you would use it with your Color fields:
 *
 * @ShowkaseColor(name = "Name", group = "Group")
 * val redColor = Color.Red
 * 
 * @ShowkaseColor("Primary", "Light Colors")
 * val primaryColor = Color(0xFF6200EE)
 *
 * @param name The name that should be used to describe your color.
 * @param group The grouping key that will be used to group it with other color properties.
 * This is useful for better organization and discoverability of your colors.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseColor(
    val name: String = "",
    val group: String = "",
)
