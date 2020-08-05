package com.airbnb.showkase.processor.models

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import kotlinx.metadata.Flag
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassHeader.Companion.CLASS_KIND
import kotlinx.metadata.jvm.KotlinClassHeader.Companion.FILE_FACADE_KIND
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal data class ShowkaseMetadata(
    val element: Element? = null,
    val moduleName: String,
    val packageName: String,
    val enclosingClass: TypeMirror? = null,
    val methodName: String,
    val showkaseComponentName: String,
    val showkaseComponentGroup: String,
    val showkaseComponentWidthDp: Int? = null,
    val showkaseComponentHeightDp: Int? = null,
    val insideWrapperClass: Boolean = false,
    val insideObject: Boolean = false
)

private enum class ShowkaseAnnotationProperty {
    NAME,
    GROUP,
    WIDTHDP,
    HEIGHTDP,
}

private enum class ShowkaseFunctionType {
    TOP_LEVEL,
    INSIDE_CLASS,
    INSIDE_OBJECT,
    INSIDE_COMPANION_OBJECT,
}

internal fun ShowkaseCodegenMetadata.toModel(first: Element): ShowkaseMetadata {
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
        showkaseComponentHeightDp = showkaseComposableHeightDp.parseAnnotationProperty(),
        insideWrapperClass = insideWrapperClass,
        insideObject = insideObject,
        element = first as ExecutableElement
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
    val showkaseAnnotation = element.getAnnotation(Showkase::class.java)
    val packageElement = elementUtil.getPackageOf(element)
    val moduleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val methodName = element.simpleName.toString()
    val showkaseFunctionType = element.getShowkaseFunctionType()

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
        enclosingClass = element.getEnclosingClassType(showkaseFunctionType),
        methodName = methodName,
        showkaseComponentName = showkaseAnnotation.name,
        showkaseComponentGroup = showkaseAnnotation.group,
        showkaseComponentWidthDp = showkaseAnnotation.widthDp.parseAnnotationProperty(),
        showkaseComponentHeightDp = showkaseAnnotation.heightDp.parseAnnotationProperty(),
        insideObject = showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT || 
                showkaseFunctionType == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        element = element
    )
}

internal fun getShowkaseMetadataFromPreview(
    element: ExecutableElement,
    elementUtil: Elements,
    typeUtils: Types,
    previewTypeMirror: TypeMirror
): ShowkaseMetadata? {
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
    val showkaseFunctionType = element.getShowkaseFunctionType()

    val numParameters = element.parameters.size
    if (numParameters > 0) {
        // We don't support @Composable preview functions that take in a parameter. So @Showkase 
        // annotation throws an error to notify the user. However, for the @Preview annotation, they 
        // recently added support for functions that acccept a parameter(in their case, its a 
        // data provider for showing some combinations in the preview. Since it's new, I want the
        // API to stabilize a bit before I try to add support for it. Until then, I return early 
        // to skip this composable. 
        // TODO(vinaygaba): Maybe notify the user that we are skipping this Preview.
        return null
    }
    return ShowkaseMetadata(
        moduleName = moduleName,
        packageName = packageName,
        enclosingClass = element.getEnclosingClassType(showkaseFunctionType),
        methodName = methodName,
        showkaseComponentName = map[ShowkaseAnnotationProperty.NAME]?.let { it as String }.orEmpty(),
        showkaseComponentGroup = map[ShowkaseAnnotationProperty.GROUP]?.let { it as String }.orEmpty(),
        showkaseComponentWidthDp = map[ShowkaseAnnotationProperty.WIDTHDP]?.let { it as Int },
        showkaseComponentHeightDp = map[ShowkaseAnnotationProperty.HEIGHTDP]?.let { it as Int },
        insideObject = showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT ||
                showkaseFunctionType == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        element = element
    )
}

private fun ExecutableElement.getShowkaseFunctionType(): ShowkaseFunctionType =
    when (enclosingElement?.kotlinMetadata()?.header?.kind) {
        CLASS_KIND -> {
            val kmClass =
                (enclosingElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
            when {
                Flag.Class.IS_CLASS(kmClass.flags) -> ShowkaseFunctionType.INSIDE_CLASS
                Flag.Class.IS_COMPANION_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
                Flag.Class.IS_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_OBJECT
                else -> throw ShowkaseProcessorException(
                    "Your @Showkase/@Preview function:${this.simpleName} is declared in a way " + 
                            "that is not supported by Showkase.")
            }
        }
        FILE_FACADE_KIND -> ShowkaseFunctionType.TOP_LEVEL
        else -> throw ShowkaseProcessorException("Your @Showkase/@Preview " +
                "function:${this.simpleName} is declared in a way that is not supported by " +
                "Showkase.")
    }

private fun Element.kotlinMetadata(): KotlinClassMetadata? {
    // https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm
    val kotlinMetadataAnnotation = getAnnotation(Metadata::class.java) ?: return null
    val header = KotlinClassHeader(
        kind = kotlinMetadataAnnotation.kind,
        metadataVersion = kotlinMetadataAnnotation.metadataVersion,
        bytecodeVersion = kotlinMetadataAnnotation.bytecodeVersion,
        data1 = kotlinMetadataAnnotation.data1,
        data2 = kotlinMetadataAnnotation.data2,
        extraString = kotlinMetadataAnnotation.extraString,
        packageName = kotlinMetadataAnnotation.packageName,
        extraInt = kotlinMetadataAnnotation.extraInt
    )

    return KotlinClassMetadata.read(header)
}

private fun ExecutableElement.getEnclosingClassType(
    showkaseFunctionType: ShowkaseFunctionType
) = when(showkaseFunctionType) {
    ShowkaseFunctionType.TOP_LEVEL -> null
    ShowkaseFunctionType.INSIDE_CLASS, ShowkaseFunctionType.INSIDE_OBJECT -> enclosingElement.asType()
    // Get the class that holds the companion object instead of using the intermediate element 
    // that's used to represent the companion object.
    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT -> enclosingElement.enclosingElement.asType()
}
