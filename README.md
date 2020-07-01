# Showkase


## Installation

Using Showkase is really straightforward and takes less than a couple minutes to get started.

**Step 1**: Add the dependency to your project `build.gradle` file. If you have a multi-module 
setup, add this dependency to all the modules that have a `@Composable` function that should also be
 displayed inside the Showkase browser.

```
implementation "com.airbnb:showkase:0.1.0-alpha"
kapt "com.airbnb:showkcase-processor:0.1.0-alpha"
```

**Step 2**: Add the @Showkase annotation to the `@Composable` function/components that should be a 
part of the Showkase browser. 

```
@Showkase(name="Name of component", group="Grouping Name")
```

**Step 3**: Define an implementation of `ShowkaseRootModule` in your root module. If your setup 
involves only a single module, add this implementation in that module. Ensure that this 
implementation is also annotated with the `@ShowkaseRoot` annotation.

```
@ShowkaseRoot
class MyRootModule: ShowkaseRootModule
```

**Step 4**: Showkase is now ready for use! Just start the `ShowcaseBrowserActivity` to access it. 
Typically you would start this activity from the debug menu of your app but you are free to start 
this from any place you like! `ShowcaseBrowserActivity` comes with a nice helper function that 
returns the intent you need to start. Just pass in the context & the `canonicalName` of the root 
module you created in `Step3`.
 

```
startActivity(
    ShowkaseBrowserActivity.getIntent(this, MyRootModule::class.java.canonicalName!!)
)
```

