package com.airbnb.showkase.processor.models

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal data class ShowkaseMetadata(
    val moduleName: String,
    val packageName: String,
    val enclosingClass: TypeMirror? = null,
    val methodName: String,
    val showkaseComponentName: String,
    val showkaseComponentGroup: String,
    val showkaseComponentWidthDp: Int? = null,
    val showkaseComponentHeightDp: Int? = null
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
        showkaseComponentWidthDp = showkaseComposableWidthDp.parseAnnotationProperty(),
        showkaseComponentHeightDp = showkaseComposableHeightDp.parseAnnotationProperty()
    )
}

private fun Int.parseAnnotationProperty() = when(this) {
    -1 -> null
    else -> this
}

internal fun getShowkaseMetadata(
    element: ExecutableElement,
    elementUtil: Elements
): ShowkaseMetadata {
    val enclosingElement = element.enclosingElement
    val isStaticMethod = element.modifiers.contains(Modifier.STATIC)
    val showkaseAnnotation = element.getAnnotation(Showkase::class.java)
    val packageElement = elementUtil.getPackageOf(element)
    val moduleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val methodName = element.simpleName.toString()

    val numParameters = element.parameters.size
    if (numParameters > 0) {
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
        showkaseComponentWidthDp = showkaseAnnotation.widthDp.parseAnnotationProperty(),
        showkaseComponentHeightDp = showkaseAnnotation.heightDp.parseAnnotationProperty()
    )
}

internal fun getShowkaseMetadataFromPreview(
    element: ExecutableElement,
    elementUtil: Elements,
    typeUtils: Types,
    previewTypeMirror: TypeMirror
): ShowkaseMetadata {
    val enclosingElement = element.enclosingElement
    val isStaticMethod = element.modifiers.contains(Modifier.STATIC)
    val previewAnnotationMirror = element.annotationMirrors.find {
        typeUtils.isSameType(it.annotationType, previewTypeMirror)
    }

    val map = mutableMapOf<ShowkaseAnnotationProperty, Any>()
    previewAnnotationMirror?.elementValues?.map { entry ->
        val key = entry.key.simpleName.toString().toUpperCase()
        val value = entry.value.value
        // Only store the properties that we currently support in the annotation
        if (ShowkaseAnnotationProperty.values().any { it.name == key }) {
            // All the supported types are safe to serialize as a String in order to store in the
            // map.
            val annotationProperty = 
                ShowkaseAnnotationProperty.valueOf(key)
            map[annotationProperty] =  value
        }
    }
    val packageElement = elementUtil.getPackageOf(element)
    val moduleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val methodName = element.simpleName.toString()

    val numParameters = element.parameters.size
    if (numParameters > 0) {
        throw ShowkaseProcessorException(
            "Showkase currently only supports functions that do not take in any parameters."
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
        showkaseComponentName = map[ShowkaseAnnotationProperty.NAME]?.let { it as String }.orEmpty(),
        showkaseComponentGroup = map[ShowkaseAnnotationProperty.GROUP]?.let { it as String }.orEmpty(),
        showkaseComponentWidthDp = map[ShowkaseAnnotationProperty.WIDTHDP]?.let { it as Int },
        showkaseComponentHeightDp = map[ShowkaseAnnotationProperty.HEIGHTDP]?.let { it as Int }
    )
}

internal enum class ShowkaseAnnotationProperty {
    NAME,
    GROUP,
    WIDTHDP,
    HEIGHTDP,
}
