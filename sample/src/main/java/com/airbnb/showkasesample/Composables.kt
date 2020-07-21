package com.airbnb.showkasesample

import androidx.animation.FastOutLinearInEasing
import androidx.animation.FloatPropKey
import androidx.animation.Infinite
import androidx.animation.transitionDefinition
import androidx.compose.Composable
import androidx.compose.frames.modelListOf
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.animation.Transition
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.tag
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ColorFilter
import androidx.ui.graphics.imageFromResource
import androidx.ui.graphics.drawscope.rotate
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet
import androidx.ui.layout.FlowRow
import androidx.ui.layout.MainAxisAlignment
import androidx.ui.layout.SizeMode
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredSize
import androidx.ui.layout.preferredWidth
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.Card
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.Surface
import androidx.ui.material.darkColorPalette
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Add
import androidx.ui.material.icons.filled.Done
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.material.lightColorPalette
import androidx.ui.res.loadVectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseRoot
import com.airbnb.showkase.annotation.models.ShowkaseRootModule

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", style = TextStyle(fontSize = 15.sp))
}

@Showcase(name = "Cursive Text Style", group = "Text Style")
@Preview
@Composable
fun CursiveTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)
    
    ShowcaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, 
                    fontFamily = FontFamily.Cursive))
        }
    }
}

@Showcase(name = "Serif Text Style", group = "Text Style")
@Preview
@Composable
fun SerifTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowcaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif))
        }
    }
}

@Showcase(name = "Sans Serif Text Style", group = "Text Style")
@Preview
@Composable
fun SansSerifTextComponentPreview() {
    val context = ContextAmbient.current
    val string = context.getString(R.string.app_name)

    ShowcaseTheme {
        Card {
            Text(text = string, modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif))
        }
    }
}

/**
 * PropKeys are used in Jetpack Compose animations to hold properties that are going to be
 * updated by the animation transitions. In this example, we use a [FloatPropKey] to hold a float
 * value that represents the value of rotation.
 */
private val rotation = FloatPropKey()

/**
 * Animations in Jetpack Compose use the [Transition] composable. This transition API depends on
 * transitions between states & changing values, similar to how ValueAnimators worked in the older
 * Android UI Toolkit. Transition's do not care about the actual implementation itself, they just
 * allow you to manipulate and transition between different states and also let you specify what
 * the interpolation, duration & behavior of these transitions should be like. Read through the
 * comments below to understand this better.
 */
private val rotationTransitionDefinition = transitionDefinition {
    // We define a transitionDefinition that's meant to be an exhaustive list of all states &
    // state transitions that are a part of your animation. Below, we define two states - state 0
    // & state 360. For each state, we also define the value of the properties when they are in
    // the respective state. For example - for state A, we assign the rotation prop the value 0f
    // and for state B, we assign the rotation prop the value 360f.
    state("A"){ this[rotation] = 0f }
    state("B") { this[rotation] = 360f }

    // Here we define the transition spec i.e what action do we need to do as we transition from
    // one state to another. Below, we define a TransitionSpec for the transition
    // state A -> state B.
    transition("A" to "B") {
        // For the transition from state A -> state B, we assign a AnimationBuilder to the
        // rotation prop where we specify how we want to update the value of the rotation prop
        // between state A & B, what the duration of this animation should be, what kind of
        // interpolator to use for the animation & how many iterations of this animation are needed.
        // Since we want the rotation to be continous, we use the repeatable AnimationBuilder and
        // set the iterations to Infinite.
        rotation using repeatable {
            animation = tween {
                duration = 3000
                easing = FastOutLinearInEasing
            }
            iterations = Infinite
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun RotatingSquareComponent() {
    // Box is a predefined convenience composable that allows you to apply common draw & layout
    // logic. We give it a ContentGravity of Center to ensure the children of this composable
    // are placed in its center. In addition we also pass a few modifiers to it.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, as the Box composable to
    // occupy the entire available height & width using Modifier.fillMaxSize().
    Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center, children = {
        // Transition composable creates a state-based transition using the animation configuration
        // defined in [TransitionDefinition]. In the example below, we use the
        // rotationTransitionDefinition that we discussed above and also specify the initial
        // state of the animation & the state that we intend to transition to. The expectation is
        // that the transitionDefinition allows for the transition from the "initialState" to the
        // "toState".
        Transition(
            definition = rotationTransitionDefinition,
            initState = "A",
            toState = "B"
        ) { state ->
            // We use the Canvas composable that gives you access to a canvas that you can draw
            // into. We also pass it a modifier.

            // You can think of Modifiers as implementations of the decorators pattern that are used
            // to modify the composable that its applied to. In this example, we assign a size
            // of 200dp to the Canvas using Modifier.preferredSize(200.dp).
            Canvas(modifier = Modifier.preferredSize(200.dp)) {
                rotate(state[rotation]) {
                    drawRect(color = Color(255, 138, 128))
                }
            }
        }
    })
}

/**
 * Android Studio lets you preview your composable functions within the IDE itself, instead of
 * needing to download the app to an Android device or emulator. This is a fantastic feature as you
 * can preview all your custom components(read composable functions) from the comforts of the IDE.
 * The main restriction is, the composable function must not take any parameters. If your composable
 * function requires a parameter, you can simply wrap your component inside another composable
 * function that doesn't take any parameters and call your composable function with the appropriate
 * params. Also, don't forget to annotate it with @Preview & @Composable annotations.
 */

@Showcase(group = "Rotating Animation", name = "Animations")
@Preview
@Composable
internal fun RotatingSquareComponentPreview() {
    RotatingSquareComponent()
}

//class WrapperClass {
//    @Showcase(group = "Group3", name = "Test Component", widthDp = 10, heightDp = 10)
//    @Composable
//    fun TestComponent() {
//        val vectorAsset = loadVectorResource(id = R.drawable.ic_baseline_verified_user_24)
//        vectorAsset.resource.resource?.let {
//            Image(asset = it, colorFilter = ColorFilter.tint(Color.Black))
//        }
//    }
//}

val listItems = listOf("Games", "Apps", "Movies", "Books")

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    // Reacting to state changes is the core behavior of Compose. We use the state composable
    // that is used for holding a state value in this composable for representing the current
    // value of the selectedIndex. Any composable that reads the value of counter will be recomposed
    // any time the value changes. This ensures that only the composables that depend on this
    // will be redraw while the rest remain unchanged. This ensures efficiency and is a
    // performance optimization. It is inspired from existing frameworks like React.
    var selectedIndex by state { 0 }
    // BottomNavigation is a component placed at the bottom of the screen that represents primary
    // destinations in your application.


    ShowcaseTheme {
        BottomNavigation(modifier = Modifier.padding(16.dp)) {
            listItems.forEachIndexed { index, label ->
                // A composable typically used as part of BottomNavigation. Since BottomNavigation
                // is usually used to represent primary destinations in your application,
                // BottomNavigationItem represents a singular primary destination in your application.
                BottomNavigationItem(
                    icon = {
                        // Simple composable that allows you to draw an icon on the screen. It
                        // accepts a vector asset as the icon.
                        Icon(asset = Icons.Filled.Favorite)
                    },
                    text = {
                        // Text is a predefined composable that does exactly what you'd expect it to -
                        // display text on the screen. It allows you to customize its appearance using the
                        // style property.
                        Text(text = label)
                    },
                    // Update the selected index when the BottomNavigationItem is clicked
                    selected = selectedIndex == index,
                    onSelected = { selectedIndex = index }
                )
            }
        }
    }
}

@Showcase(name = "Bottom Navigation Bar", group = "Navigation")
@Preview
@Composable
fun BottomNavigationAlwaysShowLabelComponentPreview() {
    BottomNavigationAlwaysShowLabelComponent()
}

@Composable
fun ShowcaseTheme(children: @Composable()() -> Unit) {
    val light = lightColorPalette()
    val dark = darkColorPalette()
    val colors = if (isSystemInDarkTheme()) { dark } else { light }
    MaterialTheme(colors = colors) {
        children()
    }
}

data class Person(
    val name: String,
    val age: Int,
    val profilePictureUrl: String? = null
)

@Composable
fun SuperheroComponent() {
    val person = Person("Iron Man", 43, "https://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55.jpg")
    ShowcaseTheme {
        Card(shape = RoundedCornerShape(4.dp),
            modifier = Modifier.fillMaxWidth() + Modifier.padding(8.dp)) {
            // ListItem is a predefined composable that is a Material Design implementation of [list
            // items](https://material.io/components/lists). This component can be used to achieve the
            // list item templates existing in the spec
            ListItem(text = {
                // The Text composable is pre-defined by the Compose UI library; you can use this
                // composable to render text on the screen
                Text(
                    text = person.name,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, secondaryText = {
                Text(
                    text = "Age: ${person.age}",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 15.sp,
                        fontWeight = FontWeight.Light, color = Color.DarkGray
                    )
                )
            }, icon = {
                person.profilePictureUrl?.let { imageUrl ->
                    // Look at the implementation of this composable in ImageActivity to learn
                    // more about its implementation. It uses Picasso to load the imageUrl passed
                    // to it.
                    Box(
                        modifier = Modifier.preferredWidth(60.dp) + Modifier.preferredHeight(60.dp) + 
                                Modifier.drawBackground(Color.Gray)
                    )
                }
            })
        }
    }
}

@Showcase("Superhero List Item", "List Items")
@Preview
@Composable
fun SuperherComponentPreview() {
    SuperheroComponent()
}

data class Amenity(
    val name: String,
    val iconUrl: String
)

fun getAmenityList() = listOf(
    Amenity("Elevator", ""),
    Amenity("Washer/Dryer", ""),
    Amenity("Wheelchair Access", ""),
    Amenity("Dogs Ok", ""),
    Amenity("Smoke Detector", ""),
    Amenity("Wifi", ""),
    Amenity("Television", ""),
    Amenity("Coffee Maker", ""),
    Amenity("Hair Dryer", ""),
    Amenity("Iron", "")
)

val colors = listOf(
    Color(0xFFffd7d7.toInt()),
    Color(0xFFffe9d6.toInt()),
    Color(0xFFfffbd0.toInt()),
    Color(0xFFe3ffd9.toInt()),
    Color(0xFFd0fff8.toInt())
)

@Composable
fun SimpleFlowRow(amenityList: List<Amenity>) {
    // Reacting to state changes is the core behavior of Compose. We use the state composable
    // that is used for holding a state value in this composable for representing the current
    // value of whether the checkbox is checked. Any composable that reads the value of "selectedIndices"
    // will be recomposed any time the value changes. This ensures that only the composables that
    // depend on this will be redraw while the rest remain unchanged. This ensures efficiency and
    // is a performance optimization. It is inspired from existing frameworks like React.
    val selectedIndices by state { modelListOf<Int>() }
    // Box is a predefined convenience composable that allows you to apply common draw & layout
    // logic. In addition we also pass a few modifiers to it.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In the example below, we configure the
    // Box to occupy the entire available width using the Modifier.fillMaxSize() modifier and 
    // also give it a padding of 4 dp.
    ShowcaseTheme {
        Box(modifier = Modifier.padding(4.dp) + Modifier.fillMaxSize() + Modifier) {
            // FlowRow is a pre-defined composable that places its children in a horizontal flow 
            // similar to the Row composable. However, its different from the Row composable in that if 
            // the horizontal space is not sufficient for all the children in one row, it 
            // overflows to more rows. 
            // mainAxisAlignment is the alignment in the horizontal direction
            // crossAxisSpacing is the spacing between rows in the vertical direction
            // mainAxisSpacing is the spacing between the children in the same row
            FlowRow(
                mainAxisAlignment = MainAxisAlignment.Center,
                crossAxisSpacing = 16.dp,
                mainAxisSpacing = 16.dp,
                mainAxisSize = SizeMode.Expand
            ) {
                amenityList.forEachIndexed { index, amenity ->
                    // Clickable wraps the child composable and enables it to react to a click through 
                    // the onClick callback similar to the onClick listener that we are accustomed to on 
                    // Android. Here, we just add the current index to the selectedIndices set every
                    // time a user taps on it.
                    Clickable(onClick = { selectedIndices.add(index) }) {
                        // Text is a predefined composable that does exactly what you'd expect it to - 
                        // display text on the screen. It allows you to customize its appearance using 
                        // style, fontWeight, fontSize, etc.
                        Text(
                            text = if (selectedIndices.contains(index)) "✓ ${amenity.name}" else amenity.name,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.drawBackground(
                                color = colors[index % colors.size], shape = RoundedCornerShape(15.dp)) +
                                    Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Showcase(name = "Filter Chips", group = "Chips")
@Preview
@Composable
fun SimpleFlowRowPreview() {
    SimpleFlowRow(getAmenityList())
}


// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun SimpleConstraintLayoutComponent() {
    // Ambient is an implicit way to pass values down the compose tree. Typically, we pass values
    // down the compose tree by passing them as parameters. This makes it easy to have fairly
    // modular and reusable components that are easy to test as well. However, for certain types
    // of data where multiple components need to use it, it makes sense to have an implicit way
    // to access this data. For such scenarios, we use Ambients. In this example, we use the
    // ContextAmbient to get hold of the Context object. In order to get access to the latest
    // value of the Ambient, use the "current" property eg - ContextAmbient.current. Some other
    // examples of common Ambient's are TextInputServiceAmbient, DensityAmbient,
    // CoroutineContextAmbient, etc.
    val resources = ContextAmbient.current.resources

    // Card composable is a predefined composable that is meant to represent the card surface as
    // specified by the Material Design specification. We also configure it to have rounded
    // corners and apply a modifier.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In the example below, we configure the Card
    // composable to have a padding of 8dp, height of 120 dp & specify it occupy the entire
    // available width.
    ShowcaseTheme {
        Card(modifier = Modifier.preferredHeight(120.dp) + Modifier.fillMaxWidth() + Modifier.padding(8.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            // ConstraintLayout is a composable that positions its children based on the constraints
            // we specify in its scope. We specify constraints using ConstraintSet
            ConstraintLayout(ConstraintSet {
                // In order to specify constraints, we use tags and assign constraints to the tags.
                // In order to apply these constraints to a composable(view/layout), we reference
                // these tags to impose the respective constraint on that composable. Look at how
                // each of these tags are being reference below using the Modifier.tag modifier.
                val title = tag("title")
                val subtitle = tag("subtitle")
                val image = tag("image")

                // We apply the constraints to tags using ConstraintLayout related helper functions.
                image.apply {
                    // We want to vertically center the image tag
                    centerVertically()
                    // Constraint the left edge of image tag to the left edge of the parent
                    left constrainTo parent.left
                    // Add a margin of 16 dp to the left edge of image tag
                    left.margin = 16.dp
                }

                title.apply {
                    // Constraint the left edge of the title to the right edge of the image
                    left constrainTo image.right
                    // Add a margin of 16 dp to the left edge
                    left.margin = 16.dp
                    // Constraint the top edge of the title to the top edge of the image
                    top constrainTo image.top
                }

                subtitle.apply {
                    // Constraint the bottom edge of the subtitle to the bottom edge of the image
                    bottom constrainTo image.bottom
                    // Constraint the left edge of the subtitle to the right edge of the image
                    left constrainTo image.right
                    // Add a margin of 16 dp to the left edge
                    left.margin = 16.dp
                }

            }) {
                // This is where we specify the children of the ConstraintLayout composable.

                // Text is a predefined composable that does exactly what you'd expect it to -
                // display text on the screen. It allows you to customize its appearance using the
                // style property.

                // In order to apply the constraints that we defined above, we make use of the
                // Modifier.tag modifier and use the same key that we passed when creating the tags to
                // reference the appropriate constraint. This way, the corresponding constraints are
                // applied to the composable referencing it.

                // You can think of Modifiers as implementations of the decorators pattern that are used to
                // modify the composable that its applied to. Some examples of modifiers are
                // Modifier.tag, Modifier.padding, Modifier.preferredHeight, Modifier.preferredWidth,
                // etc
                Text("Title", style = TextStyle(fontFamily = FontFamily.Serif, fontWeight =
                FontWeight.W900, fontSize = 14.sp), modifier = Modifier.tag("title"))
                Text("Subtitle", style = TextStyle(fontFamily = FontFamily.Serif, fontWeight =
                FontWeight.W900, fontSize = 14.sp), modifier = Modifier.tag("subtitle"))
                Box(modifier = Modifier.tag("image") + Modifier.preferredHeight(72.dp) +
                        Modifier.preferredWidth(72.dp)) {
                    Image(imageFromResource(resources, R.drawable.lenna))
                }
            }
        }
    }
}

@Showcase("List Component", "List Items")
@Preview("Simple constraint layout example")
@Composable
fun SimpleConstraintLayoutComponentPreview() {
    Column {
        SimpleConstraintLayoutComponent()
    }
}

@ShowkaseRoot
class RootModule: ShowkaseRootModule
