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

internal data class ShowkaseMetadata(
    val moduleName: String,
    val packageName: String,
    val enclosingClass: TypeMirror? = null,
    val methodName: String,
    val showkaseComponentName: String,
    val showkaseComponentGroup: String,
    val showkaseComponentWidthDp: Int,
    val showkaseComponentHeightDp: Int,
    val insideWrapperClass: Boolean = false,
    val insideObject: Boolean = false
)

private enum class ShowkaseFunctionType {
    TOP_LEVEL,
    INSIDE_CLASS,
    INSIDE_OBJECT,
    INSIDE_COMPANION_OBJECT,
    UNKNOWN
}

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
        showkaseComponentHeightDp = showkaseComposableHeightDp,
        insideWrapperClass = insideWrapperClass,
        insideObject = insideObject
    )
}

internal fun getShowkaseMetadata(
    element: Element,
    elementUtil: Elements
): ShowkaseMetadata {
    val executableElement = element as ExecutableElement
    val showkaseAnnotation = executableElement.getAnnotation(Showkase::class.java)
    val moduleName = elementUtil.getPackageOf(executableElement).simpleName.toString()
    val packageName = element.enclosingElement.enclosingElement.asType().toString()
    val methodName = executableElement.simpleName.toString()
    val showkaseFunctionType = executableElement.getShowkaseFunctionType()

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
        enclosingClass = executableElement.getEnclosingClassType(showkaseFunctionType),
        methodName = methodName,
        showkaseComponentName = showkaseAnnotation.name,
        showkaseComponentGroup = showkaseAnnotation.group,
        showkaseComponentWidthDp = showkaseAnnotation.widthDp,
        showkaseComponentHeightDp = showkaseAnnotation.heightDp,
        insideObject = showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT || 
                showkaseFunctionType == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS
    )
}

private fun ExecutableElement.getShowkaseFunctionType() =
    when (enclosingElement.kotlinMetadata()?.header?.kind) {
        CLASS_KIND -> {
            val kmClass =
                (enclosingElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
            when {
                Flag.Class.IS_CLASS(kmClass.flags) -> {
                    ShowkaseFunctionType.INSIDE_CLASS
                }
                Flag.Class.IS_COMPANION_OBJECT(kmClass.flags) -> {
                    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
                }
                Flag.Class.IS_OBJECT(kmClass.flags) -> {
                    ShowkaseFunctionType.INSIDE_OBJECT
                }
                else -> ShowkaseFunctionType.UNKNOWN
            }
        }
        FILE_FACADE_KIND -> {
            ShowkaseFunctionType.TOP_LEVEL
        }
        else -> ShowkaseFunctionType.UNKNOWN
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
    ShowkaseFunctionType.INSIDE_CLASS -> enclosingElement.asType()
    ShowkaseFunctionType.INSIDE_OBJECT -> enclosingElement.asType()
    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT -> enclosingElement.enclosingElement.asType()
    else -> null
}
