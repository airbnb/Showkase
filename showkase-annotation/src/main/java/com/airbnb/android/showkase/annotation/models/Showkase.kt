package com.airbnb.android.showkase.annotation.models

/**
 * Used to annotate @Composable functions that should be displayed inside the 
 * Showkase browser. Here's how you would use it with your @Composable function:
 * 
 * @Showkase(name = "Name", group = "Group")
 * @Composable
 * fun MyComposable() {
 *     .......
 *     .......
 * }
 * 
 * <p>
 * Note: Make sure that you add this annotation to only those functions that don't accept any 
 * parameters. If your function accepts a parameters, wrap it inside another  function that doesn't 
 * accept any parameters. 
 * 
 * For example, here is a @Composable function that requires parameters -
 *
 * @Composable
 * fun MyComposable(name: String) {
 *     .......
 *     .......
 * }
 * 
 * In order to make this function compatible with Showkase, you could further wrap this function 
 * inside a method that doesn't accept a parameters in the following way:
 * 
 * @Showkase(name = "Name", group = "Group")
 * @Composable
 * fun MyComposablePreview() {
 *     MyComposable("Name")
 * }
 * 
 * This requirement is even needed by the @Preview functions of Jetpack Compose. 
 * 
 * @param name The name that should be used to describe your @Composable function.
 * @param group The grouping key that will be used to group it with other @Composable functions. 
 * This is useful for better organization and discoverability of your components.
 * @param widthDp The width that your component will be rendered in inside the Showkase browser. 
 * Use this to restrict the size of your preview inside the Showkase browser.
 * @param heightDp The height that your component will be rendered in inside the Showkase browser.
 * Use this to restrict the size of your preview inside the Showkase browser.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class Showkase(
    val name: String,
    val group: String,
    val widthDp: Int = -1,
    val heightDp: Int = -1
)
