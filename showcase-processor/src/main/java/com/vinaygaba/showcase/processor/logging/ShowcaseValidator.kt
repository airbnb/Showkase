package com.vinaygaba.showcase.processor.logging

import com.vinaygaba.showcase.annotation.models.Showcase
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind

class ShowcaseValidator {
    internal fun validateElement(
        element: Element,
        logger: ShowcaseExceptionLogger,
        composableKind: TypeKind?
    ): Boolean {
        
        return when {
            element.kind != ElementKind.METHOD -> {
                logger.logMessage(
                    "Only composable methods can be annotated " +
                            "with ${Showcase::class.java.simpleName}"
                )
                false
            }
            element.annotationMirrors.find { it.annotationType.kind == composableKind } == null -> {
                logger.logMessage(
                    "Only composable methods can be annotated " +
                            "with ${Showcase::class.java.simpleName}"
                )
                false
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                logger.logMessage(
                    "The methods annotated with ${Showcase::class.java.simpleName} can't be private " +
                            "as the library won't be able to access them otherwise. "
                )
                false
            }
            else -> true
        }
    }
}
