package com.airbnb.showkase.annotation.models

/**
 * Used to annotate the [ShowkaseRootModule] implementation class. This is needed to let Showkase
 * know more about the module that is going to be the root module for aggregating all the
 * @Composable functions across all the different modules(if you are using a multi-module
 * project). You are allowed to have only one @ShowkaseRoot per module.
 * 
 * <p>
 * Here's an example of how you would use it:
 *
 * @ShowkaseRoot
 * fun MyRootModule: ShowkaseRootModule
 * 
 * <p>
 * The root module that you declare is also important to start the Showkase browser in order to 
 * view your composables. You will have to pass in the canonical name of this implementation when
 * starting the [ShowkaseBrowserActiity]. Here's is how you would do it:
 * 
 * startActivity(ShowkaseBrowserActivity.getIntent(this, MyRootModule::class.java.canonicalName!!))
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ShowkaseRoot
