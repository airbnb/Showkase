package com.vinaygaba.showcase.processor.logging

import com.vinaygaba.showcase.annotation.models.Showcase
import com.vinaygaba.showcase.annotation.models.ShowcaseComponents
import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
import com.vinaygaba.showcase.annotation.models.ShowcaseRootModule
import com.vinaygaba.showcase.processor.ShowcaseProcessor
import com.vinaygaba.showcase.processor.exceptions.ShowcaseProcessorException
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class ShowcaseValidator {
    @Suppress("ThrowsCount")
    internal fun validateElement(
        element: Element,
        composableTypeMirror: TypeMirror?,
        typeUtils: Types?
    ) {
        val errorPrefix = "Error in ${element.simpleName}:"
        val showcaseAnnotationName = Showcase::class.java.simpleName
        when {
            element.kind != ElementKind.METHOD -> {
                throw ShowcaseProcessorException(
                    "$errorPrefix Only composable methods can be " +
                            "annotated with $showcaseAnnotationName"
                )
            }
            element.annotationMirrors.find {
                typeUtils?.isSameType(it.annotationType, composableTypeMirror!!) ?: false
            } == null -> {
                throw ShowcaseProcessorException(
                    "$errorPrefix Only composable methods can be " +
                            "annotated with $showcaseAnnotationName"
                )
            }
            element.modifiers.contains(Modifier.PRIVATE) -> {
                throw ShowcaseProcessorException(
                    "$errorPrefix The methods annotated with " +
                            "$showcaseAnnotationName can't be private " +
                            "as the library won't be able to access them otherwise."
                )
            }
            else -> {
            }
        }
    }

    internal fun validateShowcaseRootElement(
        elementSet: Set<Element>,
        elementUtils: Elements,
        typeUtils: Types
    ) {
        val showcaseRootAnnotationName = ShowcaseRoot::class.java.simpleName
        val showcaseRootModuleName = ShowcaseRootModule::class.java.simpleName

        when {
            elementSet.size != 1 -> {
                throw ShowcaseProcessorException(
                    "Only one class in the root module can be annotated with $showcaseRootAnnotationName"
                )
            }
            else -> {
                // Safe to do this as we've ensured that there's only one element in this set
                val element = elementSet.first()
                val errorPrefix = "Error in ${element.simpleName}:"

                requireClass(element, showcaseRootAnnotationName, errorPrefix)
                requireInterface(element, elementUtils, typeUtils, showcaseRootAnnotationName,
                    errorPrefix, showcaseRootModuleName)
                requireNoShowcaseRootOnClassPath(elementUtils, errorPrefix, 
                    showcaseRootAnnotationName, showcaseRootModuleName)
            }
        }
    }

    private fun requireClass(
        element: Element,
        showcaseRootAnnotationName: String,
        errorPrefix: String
    ) {
        if (element.kind != ElementKind.CLASS) {
            throw ShowcaseProcessorException(
                "$errorPrefix Only classes can be annotated with $showcaseRootAnnotationName"
            )
        }
    }

    @Suppress("LongParameterList")
    private fun requireInterface(
        element: Element,
        elementUtils: Elements,
        typeUtils: Types,
        showcaseRootAnnotationName: String,
        errorPrefix: String,
        showcaseRootModuleName: String
    ) {
        val showcaseRootInterfaceElement =
            elementUtils.getTypeElement(ShowcaseRootModule::class.java.name)
        if (!typeUtils.isAssignable(element.asType(), showcaseRootInterfaceElement.asType())) {
            throw ShowcaseProcessorException(
                "$errorPrefix Only an implementation of $showcaseRootModuleName can be annotated " +
                        "with $showcaseRootAnnotationName"
            )
        }
    }

    private fun requireNoShowcaseRootOnClassPath(
        elementUtils: Elements,
        errorPrefix: String,
        showcaseRootAnnotationName: String,
        showcaseRootModuleName: String
    ) {
        val showcaseGeneratedPackageElement =
            elementUtils.getPackageElement(ShowcaseProcessor.CODEGEN_PACKAGE_NAME)
        val showcaseComponentsOnClassPath = showcaseGeneratedPackageElement.enclosedElements
            .mapNotNull { element -> element.getAnnotation(ShowcaseComponents::class.java) }
        if (showcaseComponentsOnClassPath.isNotEmpty()) {
            throw ShowcaseProcessorException(
                "$errorPrefix Only your root module must have a single implementation of " +
                        "$showcaseRootModuleName and be annotated by $showcaseRootAnnotationName"
            )
        }
    }
}
