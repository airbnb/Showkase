package com.airbnb.android.showkase.processor.models

import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import kotlinx.metadata.Flag
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassHeader.Companion.CLASS_KIND
import kotlinx.metadata.jvm.KotlinClassHeader.Companion.FILE_FACADE_KIND
import kotlinx.metadata.jvm.KotlinClassMetadata
import java.util.Locale
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@Suppress("LongParameterList")
internal sealed class ShowkaseMetadata {
    abstract val element: Element
    abstract val packageName: String
    abstract val packageSimpleName: String
    abstract val elementName: String
    abstract val showkaseName: String
    abstract val showkaseGroup: String
    abstract val showkaseKDoc: String
    abstract val enclosingClass: TypeMirror?
    abstract val insideWrapperClass: Boolean
    abstract val insideObject: Boolean
    
    data class Component(
        override val element: Element,
        override val packageName: String,
        override val packageSimpleName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: TypeMirror? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
        val showkaseWidthDp: Int? = null,
        val showkaseHeightDp: Int? = null,
        val previewParameter: TypeMirror? = null
    ): ShowkaseMetadata()

    data class Color(
        override val element: Element,
        override val packageSimpleName: String,
        override val packageName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: TypeMirror? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
    ): ShowkaseMetadata()

    data class Typography(
        override val element: Element,
        override val packageSimpleName: String,
        override val packageName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: TypeMirror? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
    ): ShowkaseMetadata()
}

private enum class ShowkaseAnnotationProperty {
    NAME,
    GROUP,
    WIDTHDP,
    HEIGHTDP,
}

private enum class PreviewParameterProperty {
    PROVIDER,
    LIMIT,
}

internal enum class ShowkaseFunctionType {
    TOP_LEVEL,
    INSIDE_CLASS,
    INSIDE_OBJECT,
    INSIDE_COMPANION_OBJECT,
}

internal fun ShowkaseFunctionType.insideObject() = this == ShowkaseFunctionType.INSIDE_OBJECT || 
        this == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT

internal enum class ShowkaseMetadataType {
    COMPONENT,
    COLOR,
    TYPOGRAPHY
}

internal fun ShowkaseCodegenMetadata.toModel(element: Element): ShowkaseMetadata {
    val (enclosingClassTypeMirror, previewParameterClassTypeMirror) = 
        getCodegenMetadataTypeMirror()

    return when(ShowkaseMetadataType.valueOf(showkaseMetadataType)) {
        ShowkaseMetadataType.COMPONENT -> {
            ShowkaseMetadata.Component(
                packageSimpleName = packageSimpleName,
                packageName = packageName,
                enclosingClass = enclosingClassTypeMirror,
                elementName = showkaseElementName,
                showkaseName = showkaseName,
                showkaseGroup = showkaseGroup,
                showkaseWidthDp = showkaseWidthDp.parseAnnotationProperty(),
                showkaseHeightDp = showkaseHeightDp.parseAnnotationProperty(),
                insideWrapperClass = insideWrapperClass,
                insideObject = insideObject,
                showkaseKDoc = showkaseKDoc,
                element = element,
                previewParameter = previewParameterClassTypeMirror
            )
        }
        ShowkaseMetadataType.COLOR -> {
            ShowkaseMetadata.Color(
                packageSimpleName = packageSimpleName,
                packageName = packageName,
                enclosingClass = enclosingClassTypeMirror,
                elementName = showkaseElementName,
                showkaseName = showkaseName,
                showkaseGroup = showkaseGroup,
                insideWrapperClass = insideWrapperClass,
                insideObject = insideObject,
                showkaseKDoc = showkaseKDoc,
                element = element
            )
        }
        ShowkaseMetadataType.TYPOGRAPHY -> {
            ShowkaseMetadata.Typography(
                packageSimpleName = packageSimpleName,
                packageName = packageName,
                enclosingClass = enclosingClassTypeMirror,
                elementName = showkaseElementName,
                showkaseName = showkaseName,
                showkaseGroup = showkaseGroup,
                insideWrapperClass = insideWrapperClass,
                insideObject = insideObject,
                showkaseKDoc = showkaseKDoc,
                element = element
            )
        }
    }
}

private fun ShowkaseCodegenMetadata.getCodegenMetadataTypeMirror(): Pair<TypeMirror?, TypeMirror?> {
    val enclosingClassTypeMirror = try {
        enclosingClass
        listOf<TypeMirror>()
    } catch (mte: MirroredTypesException) {
        mte.typeMirrors
    }.firstOrNull()
    val previewParameterClassTypeMirror = try {
        previewParameterClass
        listOf<TypeMirror>()
    } catch (mte: MirroredTypesException) {
        mte.typeMirrors
    }.firstOrNull()
    return enclosingClassTypeMirror to previewParameterClassTypeMirror
}

private fun Int.parseAnnotationProperty() = when(this) {
    -1 -> null
    else -> this
}

internal fun getShowkaseMetadata(
    element: ExecutableElement,
    elementUtil: Elements,
    typeUtils: Types,
    showkaseValidator: ShowkaseValidator,
    previewParameterTypeMirror: TypeMirror
): ShowkaseMetadata {
    val showkaseAnnotation = element.getAnnotation(ShowkaseComposable::class.java)
    val packageElement = elementUtil.getPackageOf(element)
    val moduleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val elementName = element.simpleName.toString()
    val showkaseFunctionType = element.getShowkaseFunctionType()
    val kDoc = elementUtil.getDocComment(element).orEmpty().trim()
    val enclosingClassTypeMirror = element.getEnclosingClassType(showkaseFunctionType)
    val showkaseName = getShowkaseName(showkaseAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(showkaseAnnotation.group, enclosingClassTypeMirror, 
        typeUtils)
    val previewParamTypeMirror = element.getPreviewParameterTypeMirror(
        typeUtils, previewParameterTypeMirror, elementUtil
    )
    
    showkaseValidator.validateEnclosingClass(enclosingClassTypeMirror, typeUtils)
    
    return ShowkaseMetadata.Component(
        packageSimpleName = moduleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeMirror,
        elementName = elementName,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseWidthDp = showkaseAnnotation.widthDp.parseAnnotationProperty(),
        showkaseHeightDp = showkaseAnnotation.heightDp.parseAnnotationProperty(),
        insideObject = showkaseFunctionType.insideObject(),
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        element = element,
        showkaseKDoc = kDoc,
        previewParameter = previewParamTypeMirror
    )
}

@Suppress("LongParameterList")
internal fun getShowkaseMetadataFromPreview(
    element: ExecutableElement,
    elementUtil: Elements,
    typeUtils: Types,
    previewTypeMirror: TypeMirror,
    previewParameterTypeMirror: TypeMirror,
    showkaseValidator: ShowkaseValidator
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
    val elementName = element.simpleName.toString()
    val showkaseFunctionType = element.getShowkaseFunctionType()
    val kDoc = elementUtil.getDocComment(element).orEmpty().trim()
    val enclosingClassTypeMirror = element.getEnclosingClassType(showkaseFunctionType)
    val showkaseName = getShowkaseName(
        map[ShowkaseAnnotationProperty.NAME]?.let { it as String }.orEmpty(),
        elementName
    )
    val showkaseGroup = getShowkaseGroup(
        map[ShowkaseAnnotationProperty.GROUP]?.let { it as String }.orEmpty(),
        enclosingClassTypeMirror,
        typeUtils
    )
    val previewParamTypeMirror = element.getPreviewParameterTypeMirror(
        typeUtils, previewParameterTypeMirror, elementUtil
    )
    
    showkaseValidator.validateEnclosingClass(enclosingClassTypeMirror, typeUtils)
    
    return ShowkaseMetadata.Component(
        packageSimpleName = moduleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeMirror,
        elementName = elementName,
        showkaseKDoc = kDoc,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseWidthDp = map[ShowkaseAnnotationProperty.WIDTHDP]?.let { it as Int },
        showkaseHeightDp = map[ShowkaseAnnotationProperty.HEIGHTDP]?.let { it as Int },
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = showkaseFunctionType.insideObject(),
        element = element,
        previewParameter = previewParamTypeMirror 
    )
}

private fun ExecutableElement.getPreviewParameterTypeMirror(
    typeUtils: Types,
    previewParameterTypeMirror: TypeMirror,
    elementUtil: Elements
): TypeMirror? {
    val previewParametersMap =
        processPreviewParameterAnnotation(typeUtils, previewParameterTypeMirror)
            .firstOrNull()
    return previewParametersMap?.get(PreviewParameterProperty.PROVIDER)?.let {
        elementUtil.getTypeElement(it.toString()).asType()
    }
}

private fun ExecutableElement.processPreviewParameterAnnotation(
    typeUtils: Types,
    previewParameterTypeMirror: TypeMirror
) = parameters
    .map {
        val previewParameterAnnotation = it.annotationMirrors.find {
            typeUtils.isSameType(it.annotationType, previewParameterTypeMirror)
        }
        previewParameterAnnotation?.elementValues?.entries?.map { entry ->
            val previewParameterPropertyName =
                entry.key.simpleName.toString().toUpperCase(Locale.getDefault())
            val previewParameterPropertyValue = entry.value.value.toString()
            previewParameterPropertyName to previewParameterPropertyValue
        }?.filter { pair ->
            // Only store the properties that we currently support in the annotation
            PreviewParameterProperty.values().any { it.name == pair.first }
        }?.associate {
            PreviewParameterProperty.valueOf(it.first) to it.second
        }
    }

internal fun getShowkaseColorMetadata(
    element: Element,
    elementUtils: Elements,
    typeUtils: Types,
    showkaseValidator: ShowkaseValidator
): ShowkaseMetadata {
    val showkaseColorAnnotation = element.getAnnotation(ShowkaseColor::class.java)
    val packageElement = elementUtils.getPackageOf(element)
    val packageSimpleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val elementName = element.simpleName.toString()
    // TODO(vinaygaba): Color propertie's aren't working properly with companion objects. This is
    // because the properties are generated outside the companion object in java land(as opposed to 
    // inside the companion class for functions). Need to investigate more.
    val showkaseFunctionType = element.getShowkaseFunctionType()
    val enclosingClassTypeMirror = element.getEnclosingClassType(showkaseFunctionType)
    val kDoc = elementUtils.getDocComment(element).orEmpty().trim()
    val showkaseName = getShowkaseName(showkaseColorAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(showkaseColorAnnotation.group, enclosingClassTypeMirror,
        typeUtils)

    showkaseValidator.validateEnclosingClass(enclosingClassTypeMirror, typeUtils)

    return ShowkaseMetadata.Color(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = kDoc,
        elementName = elementName,
        packageSimpleName = packageSimpleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeMirror,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT ||
                showkaseFunctionType == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
    )
}

internal fun getShowkaseTypographyMetadata(
    element: Element,
    elementUtils: Elements,
    typeUtils: Types,
    showkaseValidator: ShowkaseValidator
): ShowkaseMetadata {
    val showkaseTypographyAnnotation = 
        element.getAnnotation(ShowkaseTypography::class.java)
    val packageElement = elementUtils.getPackageOf(element)
    val packageSimpleName = packageElement.simpleName.toString()
    val packageName = packageElement.qualifiedName.toString()
    val elementName = element.simpleName.toString()
    // TODO(vinaygaba): Typography properties aren't working properly with companion objects. 
    // This is because the properties are generated outside the companion object in java land(as
    // opposed to inside the companion class for functions). Need to investigate more.
    val showkaseFunctionType = element.getShowkaseFunctionType()
    val enclosingClassTypeMirror = element.getEnclosingClassType(showkaseFunctionType)
    val kDoc = elementUtils.getDocComment(element).orEmpty().trim()
    val showkaseName = getShowkaseName(showkaseTypographyAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(
        showkaseTypographyAnnotation.group,
        enclosingClassTypeMirror,
        typeUtils
    )

    showkaseValidator.validateEnclosingClass(enclosingClassTypeMirror, typeUtils)

    return ShowkaseMetadata.Typography(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = kDoc,
        elementName = elementName,
        packageSimpleName = packageSimpleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeMirror,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = showkaseFunctionType.insideObject()
    )
}

internal fun Element.getShowkaseFunctionType(): ShowkaseFunctionType =
    when (enclosingElement?.kotlinMetadata()?.header?.kind) {
        CLASS_KIND -> {
            val kmClass =
                (enclosingElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
            when {
                Flag.Class.IS_CLASS(kmClass.flags) -> ShowkaseFunctionType.INSIDE_CLASS
                Flag.Class.IS_COMPANION_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
                Flag.Class.IS_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_OBJECT
                else -> throw ShowkaseProcessorException(
                    "Your UI element:${this.simpleName} is declared in a way " + 
                            "that is not supported by Showkase.")
            }
        }
        FILE_FACADE_KIND -> ShowkaseFunctionType.TOP_LEVEL
        else -> throw ShowkaseProcessorException("Your UI element:${this.simpleName} is declared " +
                "in a way that is not supported by Showkase.")
    }

internal fun Element.kotlinMetadata(): KotlinClassMetadata? {
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

internal fun Element.getEnclosingClassType(
    showkaseFunctionType: ShowkaseFunctionType
) = when(showkaseFunctionType) {
    ShowkaseFunctionType.TOP_LEVEL -> null
    ShowkaseFunctionType.INSIDE_CLASS, ShowkaseFunctionType.INSIDE_OBJECT -> enclosingElement.asType()
    // Get the class that holds the companion object instead of using the intermediate element 
    // that's used to represent the companion object.
    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT -> enclosingElement.enclosingElement.asType()
}

internal fun getShowkaseName(
    showkaseNameFromAnnotation: String,
    elementName: String
) = when {
    showkaseNameFromAnnotation.isBlank() -> elementName.capitalize(Locale.getDefault())
    else -> showkaseNameFromAnnotation
}

internal fun getShowkaseGroup(
    showkaseGroupFromAnnotation: String,
    enclosingClassTypeMirror: TypeMirror?,
    typeUtils: Types
) = when {
    !showkaseGroupFromAnnotation.isBlank() -> showkaseGroupFromAnnotation
    showkaseGroupFromAnnotation.isBlank() && enclosingClassTypeMirror != null  ->
        typeUtils.asElement(enclosingClassTypeMirror).simpleName.toString().capitalize(Locale.getDefault())
    else -> "Default Group"
}
