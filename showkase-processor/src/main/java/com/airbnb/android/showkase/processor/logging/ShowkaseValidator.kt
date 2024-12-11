package com.airbnb.android.showkase.processor.logging

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XType
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.compat.XConverters.toJavac
import androidx.room.compiler.processing.isField
import androidx.room.compiler.processing.isLong
import androidx.room.compiler.processing.isMethod
import androidx.room.compiler.processing.isTypeElement
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.processor.ScreenshotTestType
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.COMPOSABLE_SIMPLE_NAME
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.PREVIEW_PARAMETER_SIMPLE_NAME
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.isJavac
import com.airbnb.android.showkase.processor.utils.findAnnotationBySimpleName
import com.airbnb.android.showkase.processor.utils.kotlinMetadata
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserProperties
import kotlinx.metadata.Flag
import kotlinx.metadata.KmFunction
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element
import kotlin.contracts.contract

internal class ShowkaseValidator(private val environment: XProcessingEnv) {

    private val colorType by lazy { environment.requireType("androidx.compose.ui.graphics.Color") }

    @Suppress("ThrowsCount")
    internal fun validateComponentElementOrSkip(
        element: XElement,
        annotationName: String,
        skipPrivatePreviews: Boolean = false
    ): Boolean {
        contract {
            returns() implies (element is XMethodElement)
        }
        when {
            !element.isMethod() -> {
                throw ShowkaseProcessorException(
                    "Only composable methods can be annotated with $annotationName",
                    element
                )
            }

            skipPrivatePreviews && element.isPrivate() -> return true
            // Only check simple name to avoid costly type resolution
            element.findAnnotationBySimpleName(COMPOSABLE_SIMPLE_NAME) == null -> {
                throw ShowkaseProcessorException(
                    "Only composable methods can be annotated with $annotationName",
                    element
                )
            }

            element.isPrivate() -> {
                throw ShowkaseProcessorException(
                    "The methods annotated with " +
                            "$annotationName can't be private as Showkase won't be able to access " +
                            "them otherwise. If you'd like to skip this check and ignore the private " +
                            "previews, kindly pass skipPrivatePreviews=true as an annotation processor option." +
                            "To learn more about how to set this option, read the Showkase README here- " +
                            "https://github.com/airbnb/Showkase/blob/master/README.md",
                    element
                )
            }
            // Validate that only a single parameter is passed to these functions. In addition,
            // the parameter should be annotated with @PreviewParameter.
            validateComposableParameter(element) -> {
                throw ShowkaseProcessorException(
                    "Make sure that the @Composable functions that you annotate with" +
                            " the $annotationName annotation only have a single parameter that is" +
                            " annotated with @PreviewParameter.",
                    element
                )
            }

            else -> {
                return false
            }
        }
    }

    // This should check if it is an annotation that's annotated with @Preview or @ShowkaseComposable annotation
    internal fun checkElementIsAnnotationClass(element: XElement): Boolean {
        contract {
            returns(true) implies (element is XTypeElement)
        }
        return element.isTypeElement() && element.isAnnotationClass()
    }

    // We only allow composable functions who's previews meet the following criteria:
    // 1. Has no parameters
    // 2. If it has parameters:
    // 2a. All parameters have default values
    // 2b. At most one parameters is annotated with @PreviewParameter
    // This is in line with the support that @Preview provides for Android Studio previews.
    private fun validateComposableParameter(
        element: XMethodElement,
    ): Boolean {
        // Return true if there are any non-default parameters passed to the composable function or
        // if there's more than one parameter that's annotated with @PreviewParameter
        return when {
            element.parameters.isEmpty() -> false
            // If the user is using kapt, we need to leverage kotlin metadata library to get
            // information about the default values of the method parameters. This is because
            // using "hasDefaultValue" on the parameter returns false for top level functions
            // when using kapt. Hence we opted to leverage the kotlin metadata library to add
            // proper support.
            element.isJavac() &&
                    element.toJavac().enclosingElement.validateKaptComposableParameter(element) -> false
            // If the user is using ksp, we had an easier way to do the same check so we avoid
            // using the kotlin metadata library and instead rely on the information provided by
            // the XProcessing library
            !element.isJavac() && element.validateKspComposableParameters() -> false
            else -> true
        }
    }

    private fun Element.validateKaptComposableParameter(composableMethodElement: XMethodElement) =
        when (val metadata = kotlinMetadata()) {
            is KotlinClassMetadata.FileFacade -> metadata.toKmPackage().functions.validateKaptComposableParameter(
                composableMethodElement
            )

            is KotlinClassMetadata.Class -> metadata.toKmClass().functions.validateKaptComposableParameter(
                composableMethodElement
            )

            else -> false
        }

    private fun MutableList<KmFunction>.validateKaptComposableParameter(
        composableMethodElement: XMethodElement,
    ): Boolean {
        // Get the kotlin metadata information for a given composable function being processed.
        val composableFunctionMetadata =
            this.find { it.name == composableMethodElement.name } ?: return false

        // Divide the parameter list of a given composable function into parameters that are
        // not annotated with @PreviewParameter and ones that are annotated with it.
        val (nonPreviewParameterParameters, previewParameterParameters) =
            composableMethodElement.parameters.partition {
                it.getAllAnnotations().none {
                    it.name == PREVIEW_PARAMETER_SIMPLE_NAME
                }
            }

        // Get the kotlin metadata information for parameters that are not annotated with @PreviewParameter
        val nonPreviewParameterParametersMetadata =
            composableFunctionMetadata.valueParameters.filter { metadata ->
                nonPreviewParameterParameters.any { metadata.name == it.name }
            }

        // Enforce that all parameters have default values and at most one parameters is annotated
        // with @PreviewParameter
        return nonPreviewParameterParametersMetadata.all {
            Flag.ValueParameter.DECLARES_DEFAULT_VALUE(it.flags)
        } && previewParameterParameters.size <= 1
    }

    private fun XMethodElement.validateKspComposableParameters(): Boolean {
        // Divide the parameter list of a given composable function into parameters that are
        // not annotated with @PreviewParameter and ones that are annotated with it.
        val (nonPreviewParameterParameters, previewParameterParameters) =
            parameters.partition { parameter ->
                parameter.getAllAnnotations().none { annotation ->
                    annotation.name == PREVIEW_PARAMETER_SIMPLE_NAME
                }
            }

        // Enforce that all parameters have default values and at most one parameters is annotated
        // with @PreviewParameter
        return nonPreviewParameterParameters.all { it.hasDefaultValue } &&
                previewParameterParameters.size <= 1
    }

    internal fun validateColorElement(
        element: XElement,
        annotationName: String
    ) {
        contract {
            returns() implies (element is XFieldElement)
        }

        if (!element.isField()) {
            throw ShowkaseProcessorException(
                "Only \"Color\" fields can be annotated with $annotationName",
                element
            )
        }

        when (environment.backend) {
            XProcessingEnv.Backend.JAVAC -> {
                // Kapt can't see that the original type is a value class, it just sees the raw
                // type of the Color value class which is a Long
                if (element.type.isLong()) return
            }

            XProcessingEnv.Backend.KSP -> {
                if (element.type.rawType == colorType.rawType) return
            }
        }

        throw ShowkaseProcessorException(
            "Only \"Color\" fields can be annotated with $annotationName",
            element
        )

        // TODO(vinay.gaba) Also add the private modifier check. Unfortunately, the java code
        //  for this element adds a private modifier since it's a field. Potentially use
        //  kotlinMetadata to enforce this check.
    }

    internal fun validateTypographyElement(
        element: XElement,
        annotationName: String,
        textStyleType: XType,
    ) {
        contract {
            returns() implies (element is XFieldElement)
        }
        when {
            !element.isField() -> {
                throw ShowkaseProcessorException(
                    "Only \"TextStyle\" fields can be annotated with $annotationName",
                    element
                )
            }

            !element.type.isSameType(textStyleType) -> {
                throw ShowkaseProcessorException(
                    "Only \"TextStyle\" fields can be annotated with $annotationName",
                    element
                )
            }
            // TODO(vinay.gaba) Also add the private modifier check. Unfortunately, the java code
            //  for this element adds a private modifier since it's a field. Potentially use
            //  kotlinMetadata to enforce this check.
            else -> {
            }
        }
    }

    internal fun validateShowkaseRootElement(
        elementSet: Set<XElement>,
        environment: XProcessingEnv
    ) {
        if (elementSet.isEmpty()) return

        val showkaseRootAnnotationName = ShowkaseRoot::class.java.simpleName

        when {
            elementSet.size != 1 -> {
                throw ShowkaseProcessorException(
                    "Only one class in a module can be annotated with $showkaseRootAnnotationName",
                    elementSet.first()
                )
            }

            else -> {
                // Safe to do this as we've ensured that there's only one element in this set
                val element = elementSet.first()

                requireClass(element, showkaseRootAnnotationName)
                requireInterface(
                    element = element,
                    interfaceType = environment
                        .requireType(ShowkaseRootModule::class),
                    annotationName = showkaseRootAnnotationName,
                )
            }
        }
    }

    private fun requireClass(
        element: XElement,
        showkaseRootAnnotationName: String,
    ) {
        contract {
            returns() implies (element is XTypeElement)
        }
        if (!element.isTypeElement()) {
            throw ShowkaseProcessorException(
                "Only classes can be annotated with @$showkaseRootAnnotationName",
                element
            )
        }
    }

    @Suppress("LongParameterList")
    private fun requireInterface(
        element: XTypeElement,
        interfaceType: XType,
        annotationName: String,
    ) {
        if (!interfaceType.isAssignableFrom(element.type)) {
            throw ShowkaseProcessorException(
                "Only an implementation of ${interfaceType.typeName} can be annotated " +
                        "with @$annotationName",
                element
            )
        }
    }

    fun validateEnclosingClass(
        enclosingClass: XTypeElement?,
    ) {
        if (enclosingClass == null) return

        enclosingClass.getConstructors().forEach { constructor ->
            if (constructor.parameters.isNotEmpty()) {
                throw ShowkaseProcessorException(
                    "Only classes that don't accept any constructor parameters can " +
                            "hold a @Composable function that's annotated with the " +
                            "@${ShowkaseComposable::class.java.simpleName}/@Preview annotation",
                    enclosingClass
                )
            }
        }
    }

    internal fun validateShowkaseTestElement(
        elements: Collection<XTypeElement>,
        environment: XProcessingEnv,
    ): ScreenshotTestType? {
        if (elements.isEmpty()) return null

        val showkaseScreenshotAnnotationName = ShowkaseScreenshot::class.java.simpleName

        when {
            elements.size > 1 -> {
                throw ShowkaseProcessorException(
                    "Only a single class can be annotated with $showkaseScreenshotAnnotationName",
                    elements.first()
                )
            }

            else -> {
                // Safe to do this as we've ensured that there's only one element in this set
                val element = elements.first()
                val showkaseScreenshotTestTypeMirror = environment
                    .requireType(SHOWKASE_SCREENSHOT_TEST_CLASS_NAME)

                // Validate that the class annotated with @ShowkaseScreenshotTest is an abstract/open
                // class
                requireOpenClass(element, showkaseScreenshotAnnotationName)

                // Validate that the class annotated with @ShowkaseScreenshot extends the
                // ShowkaseScreenshotTest interface
                val isShowkaseScreenshotTest =
                    showkaseScreenshotTestTypeMirror.isAssignableFrom(element.type)

                return if (isShowkaseScreenshotTest) {
                    ScreenshotTestType.SHOWKASE
                } else if (
                    environment.findType(PAPARAZZI_SHOWKASE_SCREENSHOT_TEST_CLASS_NAME)
                        ?.isAssignableFrom(element.type) == true
                ) {
                    val paparazziShowkaseScreenshotTestTypeMirror = environment
                        .requireType(PAPARAZZI_SHOWKASE_SCREENSHOT_TEST_CLASS_NAME)
                    validatePaparazziShowkaseScreenshotTest(
                        environment, element,
                        paparazziShowkaseScreenshotTestTypeMirror
                    )

                    ScreenshotTestType.PAPARAZZI_SHOWKASE
                } else {
                    throw ShowkaseProcessorException(
                        "Only an implementation of com.airbnb.android.showkase.screenshot.testing" +
                                ".ShowkaseScreenshotTest or com.airbnb.android.showkase.screenshot" +
                                ".testing.paparazzi.PaparazziShowkaseScreenshotTest can be annotated " +
                                "with @$showkaseScreenshotAnnotationName",
                        element
                    )
                }

                // TODO(vinaygaba): Validate that the passed root class is annotated with @ShowkaseRoot
                // and implements [ShowkaseRootModule]
            }
        }
    }

    private fun validatePaparazziShowkaseScreenshotTest(
        environment: XProcessingEnv,
        element: XTypeElement,
        paparazziShowkaseScreenshotTestTypeMirror: XType
    ) {
        val paparazziShowkaseScreenshotTestCompanionType = environment
            .requireType(PAPARAZZI_SHOWKASE_SCREENSHOT_TEST_COMPANION_CLASS_NAME)

        val companionObjectTypeElements = element.getEnclosedTypeElements().filter {
            it.isCompanionObject()
        }
        val errorMessage =
            "Classes implementing the ${paparazziShowkaseScreenshotTestTypeMirror.typeName} " +
                    "interface should have a companion object that implements the " +
                    "${paparazziShowkaseScreenshotTestCompanionType.typeName} interface."
        if (companionObjectTypeElements.isEmpty()) {
            throw ShowkaseProcessorException(
                errorMessage,
                element
            )
        }

        if (!paparazziShowkaseScreenshotTestCompanionType
                .isAssignableFrom(companionObjectTypeElements[0].type)
        ) {
            throw ShowkaseProcessorException(
                errorMessage,
                element
            )
        }
    }

    private fun requireOpenClass(
        element: XTypeElement,
        annotationName: String,
    ) {
        if (element.isFinal()) {
            throw ShowkaseProcessorException(
                "Class annotated with $annotationName needs to be an abstract/open class.",
                element
            )
        }
    }

    internal fun validateShowkaseComponents(
        componentsMetadata: ShowkaseBrowserProperties
    ) {
        val components = componentsMetadata.componentsWithPreviewParameters +
                componentsMetadata.componentsWithoutPreviewParameters
        val groupedComponents = components.groupBy { it.group }
        groupedComponents.forEach { groupEntry ->
            val groupedByNameComponents = groupEntry.value.groupBy { it.name }
            groupedByNameComponents.forEach { nameEntry ->
                // Verify that there's at most 1 default style for a given component
                if (nameEntry.value.filter { it.isDefaultStyle }.size > 1) {
                    throw ShowkaseProcessorException(
                        "Multiple styles for component: ${nameEntry.key} are current set as default style. " +
                                "Only one style is allowed to be the default style"
                    )
                }
            }
        }
    }

    companion object {
        private const val SHOWKASE_SCREENSHOT_TEST_CLASS_NAME =
            "com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest"
        private const val PAPARAZZI_SHOWKASE_SCREENSHOT_TEST_CLASS_NAME =
            "com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest"
        private const val PAPARAZZI_SHOWKASE_SCREENSHOT_TEST_COMPANION_CLASS_NAME =
            "com.airbnb.android.showkase.screenshot.testing.paparazzi.PaparazziShowkaseScreenshotTest.CompanionObject"
    }
}
