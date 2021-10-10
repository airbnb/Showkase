package com.airbnb.android.showkase.annotation


/**
 * Used to annotate the [ShowkaseScreenshotTest] implementation class. This is needed to let
 * Showkase know that it needs to generate screenshot tests for your UI elements that are integrated
 * with Showkase.
 *
 * <p>
 * Here's an example of how you would typically use it:
 *
 * @ShowkaseScreenshotTest
 * abstract class MyScreenshotTest: ShowkaseScreenshotModule {
 *   override fun onScreenshot(
 *       id: String,
 *       name: String,
 *       group: String,
 *       screenshotType: ShowkaseScreenshotType,
 *       screenshotBitmap: Bitmap
 *   ) {
 *       // Here you do the action you want to take with the screenshot.
 *   }
 * }
 *
 * </p>
 *
 * Note: you should add this class to the androidTest sourceSet as that's where your testing
 * dependencies will exists otherwise the generate test won't compile.Additionally,Its important
 * that the class you annotate with [ShowkaseScreenshot] is either abstract or open as Showkase
 * generates a class that extends this class in order to get access to theonScreenshot method.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ShowkaseScreenshot
