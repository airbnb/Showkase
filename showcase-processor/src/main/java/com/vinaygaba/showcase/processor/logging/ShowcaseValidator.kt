package com.vinaygaba.showcase.processor.logging

import com.vinaygaba.showcase.annotation.models.Showcase
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Types

class ShowcaseValidator {
    internal fun validateElement(
        element: Element,
        logger: ShowcaseExceptionLogger,
        composableTypeMirror: TypeMirror?,
        typeUtils: Types?
    ): Boolean {
        
        return when {
            element.kind != ElementKind.METHOD -> {
                logger.logMessage(
                    "Only composable methods can be annotated " +
                            "with ${Showcase::class.java.simpleName}"
                )
                false
            }
            element.annotationMirrors.find {
                typeUtils?.isSameType(it.annotationType, composableTypeMirror!!) ?: false
            } == null -> {
                logger.logMessage(
                    "Only composable methods can be annotated " +
                            "with ${Showcase::class.java.simpleName}"
                )
                false
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                logger.logMessage(
                    "The methods annotated with ${Showcase::class.java.simpleName} can't be private " +
                            "as the library won't be able to access them otherwise."
                )
                false
            }
            else -> true
        }
    }
}
