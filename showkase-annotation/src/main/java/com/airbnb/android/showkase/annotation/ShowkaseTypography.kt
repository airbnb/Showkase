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
 * @param name The name that should be used to describe your text style.
 * @param group The grouping key that will be used to group it with other text style properties.
 * This is useful for better organization and discoverability of your text styles.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseTypography(
    val name: String = "",
    val group: String = "",
)
