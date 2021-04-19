package com.airbnb.android.showkase.processor.models

import androidx.room.compiler.processing.*
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import java.util.Locale
import kotlin.reflect.KClass

@Suppress("LongParameterList")
internal sealed class ShowkaseMetadata {
    abstract val element: XElement
    abstract val packageName: String
    abstract val packageSimpleName: String
    abstract val elementName: String
    abstract val showkaseName: String
    abstract val showkaseGroup: String
    abstract val showkaseKDoc: String
    abstract val enclosingClass: XType?
    abstract val insideWrapperClass: Boolean
    abstract val insideObject: Boolean

    data class Component(
        override val element: XElement,
        override val packageName: String,
        override val packageSimpleName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: XType? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
        val showkaseWidthDp: Int? = null,
        val showkaseHeightDp: Int? = null,
        val previewParameter: XType? = null
    ) : ShowkaseMetadata()

    data class Color(
        override val element: XElement,
        override val packageSimpleName: String,
        override val packageName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: XType? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
    ) : ShowkaseMetadata()

    data class Typography(
        override val element: XElement,
        override val packageSimpleName: String,
        override val packageName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClass: XType? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
    ) : ShowkaseMetadata()
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

internal fun XAnnotationBox<ShowkaseCodegenMetadata>.toModel(element: XElement): ShowkaseMetadata {
    val enclosingClassType = getAsTypeList("enclosingClass").firstOrNull()
    val previewParameterClassType = getAsTypeList("previewParameterClass").firstOrNull()

    return when (ShowkaseMetadataType.valueOf(value.showkaseMetadataType)) {
        ShowkaseMetadataType.COMPONENT -> {
            ShowkaseMetadata.Component(
                packageSimpleName = value.packageSimpleName,
                packageName = value.packageName,
                enclosingClass = enclosingClassType,
                elementName = value.showkaseElementName,
                showkaseName = value.showkaseName,
                showkaseGroup = value.showkaseGroup,
                showkaseWidthDp = value.showkaseWidthDp.parseAnnotationProperty(),
                showkaseHeightDp = value.showkaseHeightDp.parseAnnotationProperty(),
                insideWrapperClass = value.insideWrapperClass,
                insideObject = value.insideObject,
                showkaseKDoc = value.showkaseKDoc,
                element = element,
                previewParameter = previewParameterClassType
            )
        }
        ShowkaseMetadataType.COLOR -> {
            ShowkaseMetadata.Color(
                packageSimpleName = value.packageSimpleName,
                packageName = value.packageName,
                enclosingClass = enclosingClassType,
                elementName = value.showkaseElementName,
                showkaseName = value.showkaseName,
                showkaseGroup = value.showkaseGroup,
                insideWrapperClass = value.insideWrapperClass,
                insideObject = value.insideObject,
                showkaseKDoc = value.showkaseKDoc,
                element = element
            )
        }
        ShowkaseMetadataType.TYPOGRAPHY -> {
            ShowkaseMetadata.Typography(
                packageSimpleName = value.packageSimpleName,
                packageName = value.packageName,
                enclosingClass = enclosingClassType,
                elementName = value.showkaseElementName,
                showkaseName = value.showkaseName,
                showkaseGroup = value.showkaseGroup,
                insideWrapperClass = value.insideWrapperClass,
                insideObject = value.insideObject,
                showkaseKDoc = value.showkaseKDoc,
                element = element
            )
        }
    }
}

private fun Int.parseAnnotationProperty() = when (this) {
    -1 -> null
    else -> this
}

internal fun getShowkaseMetadata(
    element: XMethodElement,
    showkaseValidator: ShowkaseValidator,
    environment: XProcessingEnv,
    previewParameterType: KClass<out Annotation>
): ShowkaseMetadata {
    val showkaseAnnotation = element.getAnnotation(ShowkaseComposable::class)?.value!!
    // TODO: 4/17/21 top level functions won't have an enclosing type in KSP, need to fix in Room?
    val packageName = element.enclosingTypeElement.packageName
    val moduleName = packageName.substringAfterLast(".")
    val elementName = element.name
    val showkaseFunctionType = element.getShowkaseFunctionType(element.enclosingTypeElement)
    // TODO: 4/17/21 kdoc support to Room
    val kDoc = "" //elementUtil.getDocComment(element).orEmpty().trim()
    val enclosingClassTypeElement =
        getEnclosingClassTypeElement(showkaseFunctionType) { element.enclosingTypeElement }
    val showkaseName = getShowkaseName(showkaseAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(
        showkaseAnnotation.group,
        enclosingClassTypeElement,
    )
    val previewParamTypeMirror = element.getPreviewParameterType(environment, previewParameterType)

    showkaseValidator.validateEnclosingClass(enclosingClassTypeElement)

    return ShowkaseMetadata.Component(
        packageSimpleName = moduleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeElement?.type,
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
    element: XMethodElement,
    environment: XProcessingEnv,
    previewTypeClass: KClass<out Annotation>,
    previewParameterTypeClass: KClass<out Annotation>,
    showkaseValidator: ShowkaseValidator
): ShowkaseMetadata? {
    val previewAnnotationBox = element.getAnnotation(previewTypeClass)
    val map = mutableMapOf<ShowkaseAnnotationProperty, Any>()
    // TODO: 4/18/21 reflective names of fields in annotation, or add preview annotation to dependencies?
//    previewAnnotationBox?.elementValues?.map { entry ->
//        val key = entry.key.simpleName.toString().toUpperCase()
//        val value = entry.value.value
//        // Only store the properties that we currently support in the annotation
//        if (ShowkaseAnnotationProperty.values().any { it.name == key }) {
//            // All the supported types are safe to serialize as a String in order to store in the
//            // map.
//            val annotationProperty =
//                ShowkaseAnnotationProperty.valueOf(key)
//            map[annotationProperty] = value
//        }
//    }
    val packageName = element.enclosingTypeElement.packageName
    val moduleName = packageName.substringAfterLast(".")
    val elementName = element.name
    val showkaseFunctionType = element.getShowkaseFunctionType(element.enclosingTypeElement)
    val enclosingClassTypeElement =
        getEnclosingClassTypeElement(showkaseFunctionType) { element.enclosingTypeElement }
    // TODO: 4/18/21 support kdoc
    val kDoc = "" // elementUtil.getDocComment(element).orEmpty().trim()
    val showkaseName = getShowkaseName(
        map[ShowkaseAnnotationProperty.NAME]?.let { it as String }.orEmpty(),
        elementName
    )
    val showkaseGroup = getShowkaseGroup(
        map[ShowkaseAnnotationProperty.GROUP]?.let { it as String }.orEmpty(),
        enclosingClassTypeElement,
    )
    val previewParamTypeMirror = element.getPreviewParameterType(
        environment,
        previewParameterTypeClass
    )

    showkaseValidator.validateEnclosingClass(enclosingClassTypeElement)

    return ShowkaseMetadata.Component(
        packageSimpleName = moduleName,
        packageName = packageName,
        enclosingClass = enclosingClassTypeElement?.type,
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

private fun XMethodElement.getPreviewParameterType(
    environment: XProcessingEnv,
    previewParameterClass: KClass<out Annotation>,
): XType? {
    val previewParametersMap =
        processPreviewParameterAnnotation(environment, previewParameterClass)
            .firstOrNull()
    return previewParametersMap?.get(PreviewParameterProperty.PROVIDER)?.let {
        environment.requireType(it)
    }
}

private fun XMethodElement.processPreviewParameterAnnotation(
    environment: XProcessingEnv,
    previewParameterClass: KClass<out Annotation>,
): List<Map<PreviewParameterProperty, String>?> {
    return parameters
        .map { param ->
            val previewParameterAnnotation = param.getAnnotation(previewParameterClass)?.value
            // TODO: 4/17/21 Have room expose element values on XAnnotationBox
            // or use java reflection?
//            previewParameterAnnotation?.elementValues?.entries?.map { entry ->
//                val previewParameterPropertyName =
//                    entry.key.simpleName.toString().toUpperCase(Locale.getDefault())
//                val previewParameterPropertyValue = entry.value.value.toString()
//                previewParameterPropertyName to previewParameterPropertyValue
//            }?.filter { pair ->
//                // Only store the properties that we currently support in the annotation
//                PreviewParameterProperty.values().any { it.name == pair.first }
//            }?.associate {
//                PreviewParameterProperty.valueOf(it.first) to it.second
//            }
            emptyMap()
        }
}

internal fun getShowkaseColorMetadata(
    element: XFieldElement,
    showkaseValidator: ShowkaseValidator,
    environment: XProcessingEnv,
): ShowkaseMetadata {
    val showkaseColorAnnotation = element.getAnnotation(ShowkaseColor::class)?.value!!
    val packageName = element.enclosingTypeElement.packageName
    val packageSimpleName = packageName.substringAfterLast(".")
    val elementName = element.name
    // TODO(vinaygaba): Color properties aren't working properly with companion objects. This is
    // because the properties are generated outside the companion object in java land(as opposed to 
    // inside the companion class for functions). Need to investigate more.
    val showkaseFunctionType = element.getShowkaseFunctionType(element.enclosingTypeElement)
    // TODO: 4/18/21 kdoc
    val kDoc = "" // elementUtils.getDocComment(element).orEmpty().trim()
    val showkaseName = getShowkaseName(showkaseColorAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(
        showkaseColorAnnotation.group,
        element.enclosingTypeElement,
    )

    showkaseValidator.validateEnclosingClass(element.enclosingTypeElement)

    return ShowkaseMetadata.Color(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = kDoc,
        elementName = elementName,
        packageSimpleName = packageSimpleName,
        packageName = packageName,
        enclosingClass = element.enclosingTypeElement.type,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT ||
                showkaseFunctionType == ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
    )
}

internal fun getShowkaseTypographyMetadata(
    element: XFieldElement,
    showkaseValidator: ShowkaseValidator,
    environment: XProcessingEnv,
): ShowkaseMetadata {
    val showkaseTypographyAnnotation = element.getAnnotation(ShowkaseTypography::class)!!.value
    val packageName = element.enclosingTypeElement.packageName
    val packageSimpleName = packageName.substringAfterLast(".")
    val elementName = element.name
    // TODO(vinaygaba): Typography properties aren't working properly with companion objects. 
    // This is because the properties are generated outside the companion object in java land(as
    // opposed to inside the companion class for functions). Need to investigate more.
    val showkaseFunctionType = element.getShowkaseFunctionType(element.enclosingTypeElement)
    val enclosingClass =
        getEnclosingClassTypeElement(showkaseFunctionType) { element.enclosingTypeElement }
    // TODO: 4/18/21 kdoc
    val kDoc = "" // elementUtils.getDocComment(element).orEmpty().trim()
    val showkaseName = getShowkaseName(showkaseTypographyAnnotation.name, elementName)
    val showkaseGroup = getShowkaseGroup(
        showkaseTypographyAnnotation.group,
        enclosingClass,
    )

    showkaseValidator.validateEnclosingClass(enclosingClass)

    return ShowkaseMetadata.Typography(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = kDoc,
        elementName = elementName,
        packageSimpleName = packageSimpleName,
        packageName = packageName,
        enclosingClass = enclosingClass?.type,
        insideWrapperClass = showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = showkaseFunctionType.insideObject()
    )
}

internal fun XElement.getShowkaseFunctionType(enclosingClassTypeElement: XTypeElement?): ShowkaseFunctionType {
    // TODO: 4/17/21 Suppport these checks in Room
    return when {
//        enclosingTypeElement == null || this.isTopLevel() -> ShowkaseFunctionType.TOP_LEVEL
//        enclosingClassTypeElement?.isKotlinObject() -> ShowkaseFunctionType.INSIDE_OBJECT
//    enclosingTypeElement.isCompanionObject() -> ShowkaseFunctionType.INSIDE_OBJECT
//        enclosingTypeElement.isNormalClasss() -> ShowkaseFunctionType.INSIDE_CLASS
        else -> throw ShowkaseProcessorException(
            "Your UI element:$this is declared in a way " +
                    "that is not supported by Showkase.", this
        )
    }
//    return when (enclosingElement?.kotlinMetadata()?.header?.kind) {
//        CLASS_KIND -> {
//            val kmClass =
//                (enclosingElement.kotlinMetadata() as KotlinClassMetadata.Class).toKmClass()
//            when {
//                Flag.Class.IS_CLASS(kmClass.flags) -> ShowkaseFunctionType.INSIDE_CLASS
//                Flag.Class.IS_COMPANION_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
//                Flag.Class.IS_OBJECT(kmClass.flags) -> ShowkaseFunctionType.INSIDE_OBJECT
//                else ->
//            }
//        }
//        FILE_FACADE_KIND -> ShowkaseFunctionType.TOP_LEVEL
//        else -> throw ShowkaseProcessorException(
//            "Your UI element:${this.name} is declared " +
//                    "in a way that is not supported by Showkase.", this
//        )
//    }
}

internal fun getEnclosingClassTypeElement(
    showkaseFunctionType: ShowkaseFunctionType,
    enclosingTypeProvider: () -> XTypeElement
): XTypeElement? = when (showkaseFunctionType) {
    ShowkaseFunctionType.TOP_LEVEL -> null
    ShowkaseFunctionType.INSIDE_CLASS, ShowkaseFunctionType.INSIDE_OBJECT -> enclosingTypeProvider()
    // Get the class that holds the companion object instead of using the intermediate element 
    // that's used to represent the companion object.
    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT -> enclosingTypeProvider().enclosingTypeElement
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
    enclosingClassTypeElement: XTypeElement?,
) = when {
    showkaseGroupFromAnnotation.isNotBlank() -> showkaseGroupFromAnnotation
    showkaseGroupFromAnnotation.isBlank() && enclosingClassTypeElement != null ->
        enclosingClassTypeElement.name.capitalize(Locale.getDefault())
    else -> "Default Group"
}
