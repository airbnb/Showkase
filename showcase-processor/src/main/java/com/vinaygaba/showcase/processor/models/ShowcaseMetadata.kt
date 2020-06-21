package com.vinaygaba.showcase.processor.models

import com.vinaygaba.showcase.annotation.models.Showcase
import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.processor.exceptions.ShowcaseProcessorException
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

internal data class ShowcaseMetadata(
    val moduleName: String,
    val packageName: String,
    val enclosingClass: TypeMirror? = null,
    val methodName: String,
    val showcaseComponentName: String,
    val showcaseComponentGroup: String,
    val showcaseComponentWidthDp: Int,
    val showcaseComponentHeightDp: Int
)

internal fun ShowcaseCodegenMetadata.toModel(): ShowcaseMetadata {
    val enclosingClassArray = try {
        enclosingClass
        listOf<TypeMirror>()
    } catch (mte: MirroredTypesException) {
        mte.typeMirrors
    }

    return ShowcaseMetadata(
        moduleName = moduleName,
        packageName = packageName,
        enclosingClass = if (enclosingClassArray.isEmpty()) null else enclosingClassArray.first(),
        methodName = composableMethodName,
        showcaseComponentName = showcaseComposableName,
        showcaseComponentGroup = showcaseComposableGroup,
        showcaseComponentWidthDp = showcaseComposableWidthDp,
        showcaseComponentHeightDp = showcaseComposableHeightDp
    )
}

internal fun getShowcaseMetadata(
    element: Element,
    elementUtil: Elements
): ShowcaseMetadata {
    val executableElement = element as ExecutableElement
    val enclosingElement = element.enclosingElement
    val isStaticMethod = executableElement.modifiers.contains(Modifier.STATIC)
    val showcaseAnnotation = executableElement.getAnnotation(Showcase::class.java)
    val moduleName = elementUtil.getPackageOf(executableElement).simpleName.toString()
    val packageName = element.enclosingElement.enclosingElement.asType().toString()
    val methodName = executableElement.simpleName.toString()

    val noOfParameters = executableElement.parameters.size
    if (noOfParameters > 0) {
        throw ShowcaseProcessorException(
            "Make sure that the @Composable functions that you " +
                    "annotate with the @Showcase annotation do not take in any parameters"
        )
    }

    return ShowcaseMetadata(
        moduleName = moduleName,
        packageName = packageName,
        // If isStaticMethod is true, it means the method was declared at the top level. 
        // If not, it was declared inside a class
        // TODO(vinaygaba): Add support for methods inside companion objects and 
        // objects
        enclosingClass = if (isStaticMethod) null else enclosingElement.asType(),
        methodName = methodName,
        showcaseComponentName = showcaseAnnotation.name,
        showcaseComponentGroup = showcaseAnnotation.group,
        showcaseComponentWidthDp = showcaseAnnotation.widthDp,
        showcaseComponentHeightDp = showcaseAnnotation.heightDp
    )
}
