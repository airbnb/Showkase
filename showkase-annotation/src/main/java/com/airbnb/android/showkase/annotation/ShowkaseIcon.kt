package com.airbnb.android.showkase.annotation

/**
 * Used to annotate [androidx.compose.ui.graphics.vector.ImageVector] properties that should be presented inside
 * the Showkase browser. Here's how you would use it with your Icons:
 *
 * @ShowkaseIcon(name = "Name", group = "Group")
 * val searchIcon = Icons.Filled.Search
 *
 * @ShowkaseIcon("Filled Search", "Search")
 * val searchIcon = Icons.Filled.Search
 *
 * @param name The name that should be used to describe your `Icon` fields. If you don't pass
 * any value, the name of the icon field is used as the name.
 * @param group The grouping key that will be used to group it with other `Icon` fields. This is
 * useful for better organization and discoverability of your icons. If you don't pass any value
 * for the group, the name of the class that wraps this field is used as the group name. If the
 * field is a top level field, the icon is added to a "Default Group".
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class ShowkaseIcon(
    val name: String = "",
    val group: String = "",
)
