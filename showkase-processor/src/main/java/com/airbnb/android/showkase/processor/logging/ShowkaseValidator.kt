package com.airbnb.android.showkase.processor.logging

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.kotlinMetadata
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class ShowkaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateComponentElement(
        element: Element,
        composableTypeMirror: TypeMirror,
        typeUtils: Types,
        annotationName: String 
    ) {
        val errorPrefix = "Error in ${element.simpleName}:"
        when {
            element.kind != ElementKind.METHOD -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only composable methods can be annotated with $annotationName"
                )
            }
            element.annotationMirrors.find {
                typeUtils.isSameType(it.annotationType, composableTypeMirror)
            } == null -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only composable methods can be annotated with $annotationName"
                )
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix The methods annotated with " +
                            "$annotationName can't be private as Showkase won't be able to access " +
                            "them otherwise."
                )
            }
            // We only want to throw an error if the user used the Showkase annotation. For 
            // @Preview annotations with parameter, we simply want to skip those. 
            annotationName == ShowkaseComposable::class.java.simpleName && 
                    (element as ExecutableElement).parameters.size > 0 -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Make sure that the @Composable functions that you annotate with" +
                            " the $annotationName annotation do not take in any parameters"
                )
            }
            else -> { }
        }
    }

    internal fun validateColorElement(
        element: Element,
        annotationName: String
    ) {
        val errorPrefix = "Error in ${element.simpleName}:"
        when {
            element.kind != ElementKind.FIELD -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only \"Color\" fields can be annotated with $annotationName"
                )
            }
            element.asType().kind != TypeKind.LONG -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only \"Color\" fields can be annotated with $annotationName"
                )
            }
            // TODO(vinay.gaba) Also add the private modifier check. Unfortunately, the java code
            //  for this element adds a private modifier since it's a field. Potentially use 
            //  kotlinMetadata to enforce this check. 
            else -> { }
        }
    }

    internal fun validateTypographyElement(
        element: Element,
        annotationName: String,
        textStyleTypeMirror: TypeMirror,
        typeUtils: Types
    ) {
        val errorPrefix = "Error in ${element.simpleName}:"
        when {
            element.kind != ElementKind.FIELD -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only \"TextStyle\" fields can be annotated with $annotationName"
                )
            }
            !typeUtils.isSameType(element.asType(), textStyleTypeMirror) -> {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only \"TextStyle\" fields can be annotated with $annotationName"
                )
            }
            // TODO(vinay.gaba) Also add the private modifier check. Unfortunately, the java code
            //  for this element adds a private modifier since it's a field. Potentially use 
            //  kotlinMetadata to enforce this check. 
            else -> { }
        }
    }

    internal fun validateShowkaseRootElement(
        elementSet: Set<Element>,
        elementUtils: Elements,
        typeUtils: Types
    ) {
        if (elementSet.isEmpty()) return
        
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
                "$errorPrefix Only classes can be annotated with @$showkaseRootAnnotationName"
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
                        "with @$showkaseRootAnnotationName"
            )
        }
    }

    fun validateEnclosingClass(
        enclosingClassTypeMirror: TypeMirror?,
        typeUtils: Types
    ) {
        val enclosingClassElement = enclosingClassTypeMirror?.let { typeUtils.asElement(it) } ?: return
        val kmClass =
            (enclosingClassElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
        val errorPrefix = "Error in ${enclosingClassElement.simpleName}:"
        kmClass.constructors.forEach { constructor ->
            if (constructor.valueParameters.isNotEmpty()) {
                throw ShowkaseProcessorException(
                    "$errorPrefix Only classes that don't accept any constructor parameters can " +
                            "hold a @Composable function that's annotated with the " +
                            "@${ShowkaseComposable::class.java.simpleName}/@Preview annotation"
                )
            }
        }
    }
}
