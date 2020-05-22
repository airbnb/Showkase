package com.vinaygaba.annotation.models

import com.vinaygaba.annotation.Showcase
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

data class ShowcaseMetadata(
    val methodElement: ExecutableElement,
    val methodName: String,
    val packageName: String,
    val name: String,
    val group: String
) {
    companion object {
        fun getShowcaseMetadata(element: Element, elementUtil: Elements, typeUtils: Types): ShowcaseMetadata {
            val executableElement = element as ExecutableElement
            val showcaseAnnotation = executableElement.getAnnotation(Showcase::class.java)
            
            return ShowcaseMetadata(
                executableElement,
                executableElement.simpleName.toString(),
                element.enclosingElement.enclosingElement.asType().toString(),
                showcaseAnnotation.name, 
                showcaseAnnotation.group
            )
        }
    }
}
