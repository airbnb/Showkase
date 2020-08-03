package com.airbnb.showkase.processor.logging

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseRoot
import com.airbnb.showkase.annotation.models.ShowkaseRootModule
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.showkase.processor.models.kotlinMetadata
import com.sun.javaws.jnl.XMLUtils
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ElementVisitor
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class ShowkaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateElement(
        element: ExecutableElement,
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
            // We only want to throw an error if the user used the Showkase annotation. For 
            // @Preview annotations with parameter, we simply want to skip those. 
            annotationName == Showkase::class.java.simpleName && element.parameters.size > 0 -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Make sure that the @Composable functions that you " +
                            "annotate with the $annotationName annotation do not take in any parameters"
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

    fun validateEnclosingClass(
        enclosingClassTypeMirror: TypeMirror?,
        typeUtils: Types
    ) {
        enclosingClassTypeMirror?.let { 
            val enclosingClassElement = typeUtils.asElement(it)
            val kmClass =
                (enclosingClassElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
            val errorPrefix = "Error in ${enclosingClassElement.simpleName}:"
            kmClass.constructors.forEach {
                if (it.valueParameters.isNotEmpty()) {
                    throw ShowkaseProcessorException(
                        "$errorPrefix Only classes that don't accept any constructor parameters can " +
                                "hold a @Composable function that's annotated with the " +
                                "@${Showkase::class.java.simpleName}/@Preview annotation"
                    )
                }
            }
        }
    }
}
