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
    val group: String,
    val apiLevel: Int,
    val theme: String,
    // TODO(mount): Make this Dp when they are inline classes
    val widthDp: Int,
    // TODO(mount): Make this Dp when they are inline classes
    val heightDp: Int,
    val locale: String,
    val fontScale: Float = 1f,
    val showDecoration: Boolean = false,
    val showBackground: Boolean = false
) {
   
    companion object {
        fun getShowcaseMetadata(element: Element, elementUtil: Elements, typeUtils: Types): ShowcaseMetadata {
            val executableElement = element as ExecutableElement
            val showcaseAnnotation = executableElement.getAnnotation(Showcase::class.java)
            // https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
//            var composableFunctionTypeMirror: TypeMirror
            try {
//                composableFunctionTypeMirror = executableElement.enclosingElement.asType()
            } catch (e: MirroredTypeException) {
//                    composableFunctionTypeMirror = e.typeMirror
            }
            
            return ShowcaseMetadata(
                executableElement,
                executableElement.simpleName.toString(),
                element.enclosingElement.enclosingElement.asType().toString(),
                showcaseAnnotation.name, 
                showcaseAnnotation.group,
                showcaseAnnotation.apiLevel, 
                showcaseAnnotation.theme, 
                showcaseAnnotation.widthDp, 
                showcaseAnnotation.heightDp, 
                showcaseAnnotation.locale
            )
        }
    }
}
