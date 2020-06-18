package com.vinaygaba.showcase.processor.logging

import com.vinaygaba.showcase.annotation.models.Showcase
import com.vinaygaba.showcase.processor.exceptions.ShowcaseProcessorException
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Types

class ShowcaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateElement(
        element: Element,
        composableTypeMirror: TypeMirror?,
        typeUtils: Types?
    ) {
        
        when {
            element.kind != ElementKind.METHOD -> {
                throw ShowcaseProcessorException("Only composable methods can be annotated " +
                        "with ${Showcase::class.java.simpleName}")
            }
            element.annotationMirrors.find {
                typeUtils?.isSameType(it.annotationType, composableTypeMirror!!) ?: false
            } == null -> {
                throw ShowcaseProcessorException("Only composable methods can be annotated " +
                        "with ${Showcase::class.java.simpleName}")
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                throw ShowcaseProcessorException("The methods annotated with " +
                        "${Showcase::class.java.simpleName} can't be private " +
                        "as the library won't be able to access them otherwise.")
            }
            else -> { }
        }
    }
}
