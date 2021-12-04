package com.airbnb.android.showkase.processor.logging

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XType
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.isField
import androidx.room.compiler.processing.isLong
import androidx.room.compiler.processing.isMethod
import androidx.room.compiler.processing.isTypeElement
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.COMPOSABLE_SIMPLE_NAME
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.PREVIEW_PARAMETER_SIMPLE_NAME
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.utils.findAnnotationBySimpleName
import kotlin.contracts.contract

internal class ShowkaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateComponentElement(
        element: XElement,
        annotationName: String
    ) {
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
                            "them otherwise.",
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
            }
        }
    }

    internal fun validateComposableParameter(
        element: XMethodElement,
    ): Boolean {
        // Return true if more than one parameter was passed to the @Composable function or if
        // the parameter that was passed is not annotated with @PreviewParameter.
        return when (element.parameters.size) {
            0 -> false
            1 -> {
                return element.parameters
                    .single()
                    .getAllAnnotations()
                    .none { it.name == PREVIEW_PARAMETER_SIMPLE_NAME }
            }
            else -> true
        }
    }

    internal fun validateColorElement(
        element: XElement,
        annotationName: String
    ) {
        contract {
            returns() implies (element is XFieldElement)
        }
        when {
            !element.isField() -> {
                throw ShowkaseProcessorException(
                    "Only \"Color\" fields can be annotated with $annotationName",
                    element
                )
            }
            !element.type.isLong() -> {
                throw ShowkaseProcessorException(
                    "Only \"Color\" fields can be annotated with $annotationName",
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
    ) {
        if (elements.isEmpty()) return

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
                requireInterface(
                    element = element,
                    interfaceType = showkaseScreenshotTestTypeMirror,
                    annotationName = showkaseScreenshotAnnotationName,
                )

                // TODO(vinaygaba): Validate that the passed root class is annotated with @ShowkaseRoot
                // and implements [ShowkaseRootModule]
            }
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
        componentsMetadata: Set<ShowkaseMetadata.Component>
    ) {
        val groupedComponents = componentsMetadata.groupBy { it.showkaseGroup }
        groupedComponents.forEach { groupEntry ->
            val groupedByNameComponents = groupEntry.value.groupBy { it.showkaseName }
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
    }
}
