package com.airbnb.android.showkase.processor.logging

import androidx.room.compiler.processing.*
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import kotlin.contracts.contract
import kotlin.reflect.KClass

internal class ShowkaseValidator(val environment: XProcessingEnv) {
    @Suppress("ThrowsCount")
    internal fun validateComponentElement(
        element: XElement,
        composableClass: KClass<out Annotation>,
        annotationName: String,
        previewParameterClass: KClass<out Annotation>
    ) {
        contract {
            returns() implies (element is XMethodElement)
        }

        when {
            element !is XMethodElement || !element.hasAnnotation(composableClass) -> {
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
            // Validate that no more than one parameter is passed to these functions. If passed,
            // the parameter must be annotated with @PreviewParameter.
            validateComposableParameter(element, previewParameterClass) -> {
                throw ShowkaseProcessorException(
                    "Make sure that the @Composable functions that you annotate with" +
                            " the $annotationName annotation only have a single parameter that is" +
                            " annotated with @PreviewParameter.",
                    element
                )
            }
        }
    }

    internal fun validateComposableParameter(
        element: XMethodElement,
        previewParameterClass: KClass<out Annotation>,
    ): Boolean {
        // Return true if more than one parameter was passed to the @Composable function or if
        // the parameter that was passed is not annotated with @PreviewParameter.
        if (element.parameters.size > 1) return true

        val param = element.parameters.firstOrNull() ?: return false

        return !param.hasAnnotation(previewParameterClass)
    }

    internal fun validateColorElement(
        element: XElement,
        annotationName: String
    ) {
        contract {
            returns() implies (element is XFieldElement)
        }
        when {
            element !is XFieldElement || !element.type.isLong()-> {
                throw ShowkaseProcessorException(
                    "Only \"Color\" fields can be annotated with $annotationName", element
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
            element !is XFieldElement || !textStyleType.isAssignableFrom(element.type) -> {
                throw ShowkaseProcessorException(
                    "Only \"TextStyle\" fields can be annotated with $annotationName", element
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
    ): XTypeElement? {
        val element = elementSet.firstOrNull() ?: return null

        // TODO: 4/18/21 Better room utils to differentiate between normal class, interface, enum, object, etc
        val showkaseRootAnnotationName = ShowkaseRoot::class.java.simpleName
        if (element !is XTypeElement) {
            throw ShowkaseProcessorException(
                "Only classes can be annotated with @$showkaseRootAnnotationName", element
            )
        }

        when {
            elementSet.size != 1 -> {
                throw ShowkaseProcessorException(
                    "Only one class in a module can be annotated with $showkaseRootAnnotationName",
                    elementSet
                )
            }
            else -> {
                requireShowkaseRootInterface(element, showkaseRootAnnotationName, environment)
            }
        }

        return element
    }

    @Suppress("LongParameterList")
    private fun requireShowkaseRootInterface(
        element: XTypeElement,
        showkaseRootAnnotationName: String,
        environment: XProcessingEnv
    ) {
        val showkaseRootInterfaceType = environment.requireType(ShowkaseRootModule::class)

        if (showkaseRootInterfaceType.isAssignableFrom(element.type)) {
            throw ShowkaseProcessorException(
                "Only an implementation of $showkaseRootInterfaceType can be annotated " +
                        "with @$showkaseRootAnnotationName",
                element
            )
        }
    }

    fun validateEnclosingClass(
        enclosingClassElement: XTypeElement?,
    ) {
        enclosingClassElement?.getConstructors()?.forEach { constructor ->
            if (constructor.parameters.isNotEmpty()) {
                throw ShowkaseProcessorException(
                    "Only classes that don't accept any constructor parameters can " +
                            "hold a @Composable function that's annotated with the " +
                            "@${ShowkaseComposable::class.java.simpleName}/@Preview annotation",
                    enclosingClassElement
                )
            }
        }
    }
}
