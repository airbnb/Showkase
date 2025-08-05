@file:Suppress("MaxLineLength", "PackageName")
package com.airbnb.android.showkase_processor_testing

import org.junit.Test

class ShowkaseProcessorTest : BaseProcessorTest() {

    @Test
    fun `composable with showkase annotation inside class compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable with preview annotation inside class compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `color with showkase color annotation inside class compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `textstyle with ShowkaseTypography annotation inside class compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with showkase annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color property with ShowkaseColor annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside object with showkase annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside object with preview annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `color property inside object with ShowkaseColor annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `textstyle property inside object with ShowkaseTypography annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside companion object with showkase annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside companion object with preview annotation compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function that has default parameters compiles ok`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `private composable with showkase annotation throws compilation error`() {
        assertCompilationFails("The methods annotated with ShowkaseComposable can't be private as " +
                "Showkase won't be able to access them otherwise.")
    }

    @Test
    fun `private composable with showkase annotation and skipPrivate option compiles ok`() {
        val options = mutableMapOf<String, String>()
        options["skipPrivatePreviews"] = "true"
        compileInputsAndVerifyOutputs(options = options)
    }

    @Test
    fun `private composable with preview annotation throws compilation error`() {
        assertCompilationFails("The methods annotated with Preview can't be private as Showkase won't be " +
                "able to access them otherwise.")
    }

    @Test
    fun `private composable with preview annotation and skipPrivate option compiles ok`() {
        val options = mutableMapOf<String, String>()
        options["skipPrivatePreviews"] = "true"
        compileInputsAndVerifyOutputs(options = options)
    }

    @Test
    fun `basic function annotated with showkase annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with ShowkaseComposable")
    }

    @Test
    fun `basic function annotated with preview annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with Preview")
    }

    @Test
    fun `class annotated with showkase annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with ShowkaseComposable")
    }

    @Test
    fun `class annotated with preview annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with Preview")
    }

    @Test
    fun `object annotated with showkase annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with ShowkaseComposable")
    }

    @Test
    fun `object annotated with preview annotation throws compilation error`() {
        assertCompilationFails("Only composable methods can be annotated with Preview")
    }

    @Test
    fun `composable function that has parameters with showkase annotation throws compilation error`() {
        assertCompilationFails(
            "Make sure that the @Composable functions that you annotate with the ShowkaseComposable" +
                    " annotation only have a single parameter that is annotated with @PreviewParameter."
        )
    }

    @Test
    fun `composable function that has non preview parameters and preview annotation throws Exception`() {
        assertCompilationFails("Make sure that the @Composable functions that you annotate with the Preview " +
                "annotation only have a single parameter that is annotated with @PreviewParameter.")
    }

    @Test
    fun `composable function with partial default parameters throws compilation error`() {
        assertCompilationFails("Make sure that the @Composable functions that you annotate with the Preview " +
                "annotation only have a single parameter that is annotated with @PreviewParameter.")
    }

    @Test
    fun `composable function with showkase annotation inside class with parameters throws compilation error`() {
        assertCompilationFails("Only classes that don't accept any constructor parameters can " +
                "hold a @Composable function that's annotated with the @ShowkaseComposable/@Preview " +
                "annotation")
    }

    @Test
    fun `composable previews with multiple parameter providers should indent properly`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function with preview annotation inside class with parameters throws compilation error`() {
        assertCompilationFails("Only classes that don't accept any constructor parameters can " +
                "hold a @Composable function that's annotated with the @ShowkaseComposable/@Preview " +
                "annotation")
    }

    @Test
    fun `non-long value annotated with ShowkaseColor annotation throws compilation error`() {
        assertCompilationFails("Only \"Color\" fields can be annotated with ShowkaseColor")
    }

    @Test
    fun `non-text style value annotated with ShowkaseTypography annotation throws compilation error`() {
        assertCompilationFails("Only \"TextStyle\" fields can be annotated with ShowkaseTypography")
    }

    @Test
    fun `class annotated with ShowkaseTypography annotation throws compilation error`() {
        assertCompilationFails("Only \"TextStyle\" fields can be annotated with ShowkaseTypography")
    }

    @Test
    fun `multiple classes with showkaseroot annotation throws compilation error`() {
        assertCompilationFails("Only one class in a module can be annotated with ShowkaseRoot")
    }

    @Test
    fun `class with no interface but showkaseroot annotation throws compilation error`() {
        assertCompilationFails("Only an implementation of com.airbnb.android.showkase.annotation.ShowkaseRootModule can be annotated with @ShowkaseRoot")
    }

    @Test
    fun `open class with no interface but ShowkaseScreenshoTest annotation throws compilation error`() {
        assertCompilationFails(
            "Only an implementation of com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest or com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest can be annotated with @ShowkaseScreenshot"
        )
    }

    @Test
    fun `closed class with right interface and showkasescreenshottest annotation throws compilation error`() {
        assertCompilationFails("Class annotated with ShowkaseScreenshot needs to be an abstract/open class")
    }

    @Test
    fun `closed class with PaparazziShowkaseScreenshotTest and ShowkaseScreensho annotation throws compilation error`() {
        assertCompilationFails("Class annotated with ShowkaseScreenshot needs to be an abstract/open class")
    }

    @Test
    fun `class implementing PaparazziShowkaseScreenshotTest but not companion object throws compilation error`() {
        assertCompilationFails("Classes implementing the com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest interface should have a companion object that implements the com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest.CompanionObject interface")
    }

    @Test
    fun `class implementing PaparazziShowkaseScreenshotTest and companion object implementing different interface throws compilation error`() {
        assertCompilationFails("Classes implementing the com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest interface should have a companion object that implements the com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest.CompanionObject interface")
    }

    @Test
    fun `top level composable function with showkase annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color property with ShowkaseColor annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable with wrapped color property with ShowkaseColor annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level textstyle property with ShowkaseTypography annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable with wrapped textstyle property with ShowkaseColor annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview parameter and showkase composable annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview parameter and preview annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with showkase and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with showkase and showkaseroot with width and height`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable function with preview and showkaseroot with width and height`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color property with showkasecolor and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level textstyle property with showkasetypography and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color property and composable function generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level textstyle property and composable function generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside class with showkase annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside object with showkase annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `color property inside class with showkasecolor annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `textstyle property inside class with showkasetypography annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `color property inside object with showkasecolor annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `textstyle property inside object with showkasetypography annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside companion object with showkase annotation and showkaseroot generates 1 file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `wrapped composable function with showkase annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `wrapped composable function with preview annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside object with showkase annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside object with preview annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside companion object with showkase annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function inside companion object with preview annotation generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `multiple composable functions with showkase annotations generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `multiple composable functions with preview annotations generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `multiple composable functions with preview and showkase annotations generates only metadata file`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function with both annotations gives priority to showkase annotation`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function with kdoc inside object with showkase annotation and showkaseroot generates 2 files`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with showkase annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with preview annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level property with showkase color annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level property with showkase typography annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside class with showkase annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside class with preview annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `property inside class with showkase color annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `property inside class with showkase typography annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside object with showkase annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside object with preview annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `property inside object with showkase color annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `property inside object with showkase typography annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside companion object with showkase annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `function inside companion object with preview annotation and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with showkase and showkaseroot and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with showkase and showkaseroot and no name`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with preview and showkaseroot and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `color property inside class with showkasecolor annotation and showkaseroot with no group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level textstyle property with showkasetypography and showkaseroot with no name`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with showkase composable and preview parameter and showkaseroot and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level function with preview and preview parameter and showkaseroot and no name or group`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `wrapped function with showkase composable and preview parameter and showkaseroot `() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `object function with preview annotation and preview parameter and showkaseroot`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `object function with preview annotation and preview parameter and showkaseroot and_long parameter provider name`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable and class with @ScreenshotTest generates screenshot test for composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level composable and class with @ScreenshotTest generates Paparazzi screenshot test for composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color and class with @ScreenshotTest generates screenshot test for composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level color and class with @ScreenshotTest generates paparazzi screenshot test for composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `top level textstyle and class with @ScreenshotTest generates screenshot test for composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `class with @ScreenshotTest only generates screenshot test for only non preview parameter composable`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `class with @ScreenshotTest generates screenshot test for all UI elements`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `class with @ScreenshotTest generates paparazzi screenshot test for all UI elements`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function with multiple preview functions compiles`() {
        // Testing only KAPT here since some of these previews are stacked.
        // This is yielding different output as repeatable annotations
        // are not yet supported by KAPT.
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KAPT))
    }

    @Test
    fun `composable function with multiple preview functions compiles ksp`() {
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KSP))
    }

    @Test
    fun `composable function with multiple preview annotations stacked generates output`() {
        // This functionality is only supported with KSP for now.
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KSP))
    }

    @Test
    fun `composable function with multiple showkasecomposable annotations stacked generates output`() {
        // This functionality is only supported with KSP for now.
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KSP))
    }

    @Test
    fun `composable function with custom preview annotation generates output`() {
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KAPT, Mode.KSP))
    }

    @Test
    fun `composable function with custom preview annotation with preview param generates output`() {
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KAPT, Mode.KSP))
    }

    @Test
    fun `composable function with repeatable custom preview annotation generates output`() {
        // This is only supported by KSP for now
        compileInputsAndVerifyOutputs(modes = listOf(Mode.KSP))
    }

    @Test
    fun `composable function with multiple repeatable custom preview annotation generates output`() {
        // This is only supported by KSP for now
        compileInputsAndVerifyOutputs(
            modes = listOf(Mode.KSP),
        )
    }

    @Test
    fun `top level composable function with showkase and showkaseroot with tags and metadata`() {
        compileInputsAndVerifyOutputs()
    }

    @Test
    fun `composable function with private custom preview annotation compiles with flag`() {
        val options = mutableMapOf<String, String>()
        options["skipPrivatePreviews"] = "true"
        compileInputsAndVerifyOutputs(options = options)
    }

    @Test
    fun `composable functions with preview annotation are skipped`() {
        val options = mutableMapOf<String, String>()
        options["requireShowkaseComposableAnnotation"] = "true"
        compileInputsAndVerifyOutputs(options = options)
    }
}
