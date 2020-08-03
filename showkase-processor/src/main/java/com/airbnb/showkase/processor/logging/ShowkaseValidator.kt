package com.airbnb.showkase.processor.logging

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseRoot
import com.airbnb.showkase.annotation.models.ShowkaseRootModule
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class ShowkaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateElement(
        element: Element,
        composableTypeMirror: TypeMirror?,
        typeUtils: Types?,
        annotationName: String 
    ) {
        val errorPrefix = "Error in ${element.simpleName}:"
        when {
            element.kind != ElementKind.METHOD -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only composable methods can be " +
                            "annotated with $annotationName"
                )
            }
            element.annotationMirrors.find {
                typeUtils?.isSameType(it.annotationType, composableTypeMirror!!) ?: false
            } == null -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only composable methods can be " +
                            "annotated with $annotationName"
                )
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix The methods annotated with " +
                            "$annotationName can't be private " +
                            "as the library won't be able to access them otherwise."
                )
            }
            else -> { }
        }
    }

    internal fun validateShowkaseRootElement(
        elementSet: Set<Element>,
        elementUtils: Elements,
        typeUtils: Types
    ) {
        val showkaseRootAnnotationName = ShowkaseRoot::class.java.simpleName
        val showkaseRootModuleName = ShowkaseRootModule::class.java.simpleName

        when {
            elementSet.size != 1 -> {
                throw ShowkaseProcessorException(
                    "Only one class in a module can be annotated with $showkaseRootAnnotationName"
                )
            }
            else -> {
                // Safe to do this as we've ensured that there's only one element in this set
                val element = elementSet.first()
                val errorPrefix = "Error in ${element.simpleName}:"

                requireClass(element, showkaseRootAnnotationName, errorPrefix)
                requireInterface(element, elementUtils, typeUtils, showkaseRootAnnotationName,
                    errorPrefix, showkaseRootModuleName)
            }
        }
    }

    private fun requireClass(
        element: Element,
        showkaseRootAnnotationName: String,
        errorPrefix: String
    ) {
        if (element.kind != ElementKind.CLASS) {
            throw ShowkaseProcessorException(
                "$errorPrefix Only classes can be annotated with $showkaseRootAnnotationName"
            )
        }
    }

    @Suppress("LongParameterList")
    private fun requireInterface(
        element: Element,
        elementUtils: Elements,
        typeUtils: Types,
        showkaseRootAnnotationName: String,
        errorPrefix: String,
        showkaseRootModuleName: String
    ) {
        val showkaseRootInterfaceElement =
            elementUtils.getTypeElement(ShowkaseRootModule::class.java.name)
        if (!typeUtils.isAssignable(element.asType(), showkaseRootInterfaceElement.asType())) {
            throw ShowkaseProcessorException(
                "$errorPrefix Only an implementation of $showkaseRootModuleName can be annotated " +
                        "with $showkaseRootAnnotationName"
            )
        }
    }
}
