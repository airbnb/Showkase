package com.airbnb.showkase.processor.models

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

internal data class ShowkaseMetadata(
    val moduleName: String,
    val packageName: String,
    val enclosingClass: TypeMirror? = null,
    val methodName: String,
    val showkaseComponentName: String,
    val showkaseComponentGroup: String,
    val showkaseComponentWidthDp: Int,
    val showkaseComponentHeightDp: Int
)

internal fun ShowkaseCodegenMetadata.toModel(): ShowkaseMetadata {
    val enclosingClassArray = try {
        enclosingClass
        listOf<TypeMirror>()
    } catch (mte: MirroredTypesException) {
        mte.typeMirrors
    }

    return ShowkaseMetadata(
        moduleName = moduleName,
        packageName = packageName,
        enclosingClass = if (enclosingClassArray.isEmpty()) null else enclosingClassArray.first(),
        methodName = composableMethodName,
        showkaseComponentName = showkaseComposableName,
        showkaseComponentGroup = showkaseComposableGroup,
        showkaseComponentWidthDp = showkaseComposableWidthDp,
        showkaseComponentHeightDp = showkaseComposableHeightDp
    )
}

internal fun getShowkaseMetadata(
    element: Element,
    elementUtil: Elements
): ShowkaseMetadata {
    val executableElement = element as ExecutableElement
    val enclosingElement = element.enclosingElement
    val isStaticMethod = executableElement.modifiers.contains(Modifier.STATIC)
    val showkaseAnnotation = executableElement.getAnnotation(Showkase::class.java)
    val moduleName = elementUtil.getPackageOf(executableElement).simpleName.toString()
    val packageName = element.enclosingElement.enclosingElement.asType().toString()
    val methodName = executableElement.simpleName.toString()

    val noOfParameters = executableElement.parameters.size
    if (noOfParameters > 0) {
        throw ShowkaseProcessorException(
            "Make sure that the @Composable functions that you " +
                    "annotate with the @Showkase annotation do not take in any parameters"
        )
    }

    return ShowkaseMetadata(
        moduleName = moduleName,
        packageName = packageName,
        // If isStaticMethod is true, it means the method was declared at the top level. 
        // If not, it was declared inside a class
        // TODO(vinaygaba): Add support for methods inside companion objects and 
        // objects
        enclosingClass = if (isStaticMethod) null else enclosingElement.asType(),
        methodName = methodName,
        showkaseComponentName = showkaseAnnotation.name,
        showkaseComponentGroup = showkaseAnnotation.group,
        showkaseComponentWidthDp = showkaseAnnotation.widthDp,
        showkaseComponentHeightDp = showkaseAnnotation.heightDp
    )
}
