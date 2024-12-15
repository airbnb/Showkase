package com.airbnb.android.showkase.annotation

/**
 * Used to annotate [androidx.compose.ui.text.TextStyle] properties that should be presented inside
 * the Showkase browser. Here's how you would use it with your TextStyle fields:
 *
 *  @ShowkaseTypography(name = "Name", group = "Group")
 *  val h1 = TextStyle(
 *      fontWeight = FontWeight.Light,
 *      fontSize = 96.sp,
 *      letterSpacing = (-1.5).sp
 *  )
 *
 * @param name The name that should be used to describe your `TextStyle` fields. If you don't
 * pass any value, the name of the textStyle field is used as the name.
 * @param group The grouping key that will be used to group it with other `TextStyle` fields.
 * This is useful for better organization and discoverability of your typography. If you don't
 * pass any value for the group, the name of the class that wraps this field is used as the group
 * name. If the field is a top level field, the textStyle is added to a "Default Group".
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseTypography(
    val name: String = "",
    val group: String = "",
)
