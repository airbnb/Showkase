package com.airbnb.android.showkase.processor.models

import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XAnnotationBox
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XMemberContainer
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.XType
import androidx.room.compiler.processing.XTypeElement
import androidx.room.compiler.processing.compat.XConverters.toJavac
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.PREVIEW_PARAMETER_SIMPLE_NAME
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.PREVIEW_SIMPLE_NAME
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.utils.findAnnotationBySimpleName
import com.airbnb.android.showkase.processor.utils.getFieldWithReflection
import com.airbnb.android.showkase.processor.utils.requireAnnotationBySimpleName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.javapoet.toKClassName
import com.squareup.kotlinpoet.javapoet.toKTypeName
import kotlinx.metadata.jvm.KotlinClassHeader.Companion.FILE_FACADE_KIND
import kotlinx.metadata.jvm.KotlinClassMetadata
import java.util.Locale
import kotlin.reflect.KProperty1

@Suppress("LongParameterList")
internal sealed class ShowkaseMetadata {
    abstract val element: XElement
    abstract val packageName: String
    abstract val packageSimpleName: String
    abstract val elementName: String
    abstract val showkaseName: String
    abstract val showkaseGroup: String
    abstract val showkaseKDoc: String
    abstract val enclosingClassName: ClassName?
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
        override val enclosingClassName: ClassName? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
        val componentIndex: Int? = null,
        val showkaseWidthDp: Int? = null,
        val showkaseHeightDp: Int? = null,
        val previewParameterProviderType: TypeName? = null,
        val previewParameterName: String? = null,
        val showkaseStyleName: String? = null,
        val isDefaultStyle: Boolean = false
    ) : ShowkaseMetadata()

    data class Color(
        override val element: XElement,
        override val packageSimpleName: String,
        override val packageName: String,
        override val elementName: String,
        override val showkaseName: String,
        override val showkaseGroup: String,
        override val showkaseKDoc: String,
        override val enclosingClassName: ClassName? = null,
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
        override val enclosingClassName: ClassName? = null,
        override val insideWrapperClass: Boolean = false,
        override val insideObject: Boolean = false,
    ) : ShowkaseMetadata()
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
    val (enclosingClassType, previewParameterClassType) = getCodegenMetadataTypes()

    // The box is needed to get all Class values, primitives can be accessed dirctly
    val props = value

    return when (ShowkaseMetadataType.valueOf(props.showkaseMetadataType)) {
        ShowkaseMetadataType.COMPONENT -> {
            ShowkaseMetadata.Component(
                packageSimpleName = props.packageSimpleName,
                packageName = props.packageName,
                enclosingClassName = enclosingClassType?.typeElement?.className?.toKClassName(),
                elementName = props.showkaseElementName,
                showkaseName = props.showkaseName,
                showkaseGroup = props.showkaseGroup,
                showkaseStyleName = props.showkaseStyleName,
                showkaseWidthDp = props.showkaseWidthDp.parseAnnotationProperty(),
                showkaseHeightDp = props.showkaseHeightDp.parseAnnotationProperty(),
                insideWrapperClass = props.insideWrapperClass,
                insideObject = props.insideObject,
                showkaseKDoc = props.showkaseKDoc,
                element = element,
                previewParameterProviderType = previewParameterClassType?.typeName?.toKTypeName(),
                previewParameterName = props.previewParameterName,
                isDefaultStyle = props.isDefaultStyle
            )
        }

        ShowkaseMetadataType.COLOR -> {
            ShowkaseMetadata.Color(
                packageSimpleName = props.packageSimpleName,
                packageName = props.packageName,
                enclosingClassName = enclosingClassType?.typeElement?.className?.toKClassName(),
                elementName = props.showkaseElementName,
                showkaseName = props.showkaseName,
                showkaseGroup = props.showkaseGroup,
                insideWrapperClass = props.insideWrapperClass,
                insideObject = props.insideObject,
                showkaseKDoc = props.showkaseKDoc,
                element = element
            )
        }

        ShowkaseMetadataType.TYPOGRAPHY -> {
            ShowkaseMetadata.Typography(
                packageSimpleName = props.packageSimpleName,
                packageName = props.packageName,
                enclosingClassName = enclosingClassType?.typeElement?.className?.toKClassName(),
                elementName = props.showkaseElementName,
                showkaseName = props.showkaseName,
                showkaseGroup = props.showkaseGroup,
                insideWrapperClass = props.insideWrapperClass,
                insideObject = props.insideObject,
                showkaseKDoc = props.showkaseKDoc,
                element = element
            )
        }
    }
}

internal fun XAnnotationBox<ShowkaseCodegenMetadata>.getCodegenMetadataTypes(): Pair<XType?, XType?> {
    return Pair(
        getAsTypeList("enclosingClass").firstOrNull(),
        getAsTypeList("previewParameterClass").firstOrNull()
    )
}

private fun Int.parseAnnotationProperty() = when (this) {
    -1 -> null
    else -> this
}

/**
 * Retrieves the value of a field on an annotation, either by using the [environmentOptions] map to read a custom field on a custom annotation or
 * by falling back to a relevant field defined by [ShowkaseComposable].
 *
 * @param environmentOptions The map of custom processor arguments and values set via kapt/ksp in the Gradle file.
 * @param customizedName The custom field name on the annotation to read.
 * @param fallbackField The field of the [ShowkaseComposable] annotation that should be used if the name has not been customized.
 */
private fun XAnnotation.getValueWithOptions(
    environmentOptions: Map<String, String>,
    customizedName: String,
    fallbackField: KProperty1<ShowkaseComposable, String>
): String = environmentOptions
    .getOrElse(customizedName) {
        // Field name has not been customized, use the fallback field and don't split.
        return getAsString(fallbackField.name)
    }.split("|")
    .firstNotNullOfOrNull { fieldName ->
        // For each delimited value, check to see if a value exists. If so take the first one. If all are blank, return an empty string.
        (getAnnotationValue(fieldName).value as? String?).takeIf { it?.isNotBlank() == true }
    } ?: ""

internal fun getShowkaseMetadata(
    element: XMethodElement,
    annotationName: com.squareup.javapoet.ClassName,
    showkaseValidator: ShowkaseValidator,
    environmentOptions: Map<String, String>
): List<ShowkaseMetadata.Component?> {
    val showkaseAnnotations = element.getAnnotations(annotationName)
    val commonMetadata = element.extractCommonMetadata(showkaseValidator)
    val previewParameterMetadata = element.getPreviewParameterMetadata()

    return showkaseAnnotations.mapNotNull { annotation ->
        // If this component was configured to be skipped, return early
        if (annotation.getAsBoolean(ShowkaseComposable::skip.name)) return@mapNotNull null

        val nameValue = annotation.getValueWithOptions(
            environmentOptions = environmentOptions,
            customizedName = ShowkaseComposable.FieldOverrideName,
            fallbackField = ShowkaseComposable::name
        )
        val groupValue = annotation.getValueWithOptions(
            environmentOptions = environmentOptions,
            customizedName = ShowkaseComposable.FieldOverrideGroup,
            fallbackField = ShowkaseComposable::group
        )
        val styleNameValue = annotation.getValueWithOptions(
            environmentOptions = environmentOptions,
            customizedName = ShowkaseComposable.FieldOverrideStyleName,
            fallbackField = ShowkaseComposable::styleName
        )

        val showkaseName = getShowkaseName(nameValue, element.name)
        val showkaseGroup = getShowkaseGroup(
            groupValue,
            commonMetadata.enclosingClass,
        )
        val isDefaultStyle = annotation.getAsBoolean(ShowkaseComposable::defaultStyle.name)
        val showkaseStyleName = getShowkaseStyleName(styleNameValue, isDefaultStyle)

        ShowkaseMetadata.Component(
            packageSimpleName = commonMetadata.moduleName,
            packageName = commonMetadata.packageName,
            enclosingClassName = commonMetadata.enclosingClassName,
            elementName = element.name,
            showkaseName = showkaseName,
            showkaseGroup = showkaseGroup,
            showkaseStyleName = showkaseStyleName,
            showkaseWidthDp = annotation.getAsInt(ShowkaseComposable::widthDp.name).parseAnnotationProperty(),
            showkaseHeightDp = annotation.getAsInt(ShowkaseComposable::heightDp.name).parseAnnotationProperty(),
            insideObject = commonMetadata.showkaseFunctionType.insideObject(),
            insideWrapperClass = commonMetadata.showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
            element = element,
            showkaseKDoc = commonMetadata.kDoc,
            previewParameterName = previewParameterMetadata?.first,
            previewParameterProviderType = previewParameterMetadata?.second,
            isDefaultStyle = isDefaultStyle,
            componentIndex = showkaseAnnotations.indexOf(annotation),
        )
    }
}

internal fun XMethodElement.extractCommonMetadata(showkaseValidator: ShowkaseValidator): CommonMetadata {
    return extractCommonMetadata(enclosingElement, showkaseValidator)
}

internal fun XFieldElement.extractCommonMetadata(showkaseValidator: ShowkaseValidator): CommonMetadata {
    return extractCommonMetadata(enclosingElement, showkaseValidator)
}

internal fun XElement.extractCommonMetadata(
    enclosingElement: XMemberContainer,
    showkaseValidator: ShowkaseValidator
): CommonMetadata {
    val showkaseFunctionType: ShowkaseFunctionType = getShowkaseFunctionType(enclosingElement)

    return CommonMetadata(
        packageName = enclosingElement.className.packageName(),
        moduleName = enclosingElement.className.packageName().substringAfterLast("."),
        kDoc = docComment.orEmpty().trim(),
        showkaseFunctionType = showkaseFunctionType,
        enclosingClass = getEnclosingClass(showkaseFunctionType, enclosingElement)
    ).also {
        showkaseValidator.validateEnclosingClass(it.enclosingClass)
    }
}

internal data class CommonMetadata(
    val packageName: String,
    val moduleName: String,
    val kDoc: String,
    val showkaseFunctionType: ShowkaseFunctionType,
    val enclosingClass: XTypeElement?,
) {
    val enclosingClassName: ClassName? = enclosingClass?.className?.toKClassName()
}

@Suppress("LongParameterList", "LongMethod")
internal fun getShowkaseMetadataFromPreview(
    element: XMethodElement,
    showkaseValidator: ShowkaseValidator,
): List<ShowkaseMetadata.Component?> {
    val previewAnnotations = element.requireAnnotationBySimpleName(PREVIEW_SIMPLE_NAME)

    val showkaseComosableAnnotation = element.getAnnotation(ShowkaseComposable::class)?.value
    // If this component was configured to be skipped, return early
    if (showkaseComosableAnnotation != null && showkaseComosableAnnotation.skip) return listOf() // Will be mapped out
    return previewAnnotations.mapIndexed { index, annotation ->
        val commonMetadata = element.extractCommonMetadata(showkaseValidator)
        val showkaseName = getShowkaseName(
            annotation.getAsString("name"),
            element.name
        )
        val showkaseGroup = getShowkaseGroup(
            annotation.getAsString("group"),
            commonMetadata.enclosingClass,
        )

        val width = annotation.getAsInt("widthDp")
        val height = annotation.getAsInt("heightDp")

        val previewParameterMetadata = element.getPreviewParameterMetadata()

        ShowkaseMetadata.Component(
            packageSimpleName = commonMetadata.moduleName,
            packageName = commonMetadata.packageName,
            enclosingClassName = commonMetadata.enclosingClassName,
            elementName = element.name,
            showkaseKDoc = commonMetadata.kDoc,
            showkaseName = showkaseName,
            showkaseGroup = showkaseGroup,
            showkaseWidthDp = if (width == -1) null else width,
            showkaseHeightDp = if (height == -1) null else height,
            insideWrapperClass = commonMetadata.showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
            insideObject = commonMetadata.showkaseFunctionType.insideObject(),
            element = element,
            previewParameterName = previewParameterMetadata?.first,
            previewParameterProviderType = previewParameterMetadata?.second,
            componentIndex = index,
        )
    }
}

private fun XMethodElement.getPreviewParameterMetadata(): Pair<String, TypeName>? {
    val previewParameterPair = getPreviewParameterAnnotation()
    return previewParameterPair?.let {
        it.first to it.second.getAsType("provider")
            .typeName
            .toKTypeName()
    }
}

private fun XMethodElement.getPreviewParameterAnnotation(): Pair<String, XAnnotation>? {
    return parameters.mapNotNull { parameter ->
        val previewParamAnnotation = parameter.findAnnotationBySimpleName(PREVIEW_PARAMETER_SIMPLE_NAME)
        previewParamAnnotation?.let {
            parameter.name to previewParamAnnotation
        }
    }.firstOrNull()
}

internal fun getShowkaseColorMetadata(
    element: XFieldElement,
    showkaseValidator: ShowkaseValidator
): ShowkaseMetadata {
    val showkaseColorAnnotation = element.requireAnnotation(ShowkaseColor::class).value
    // TODO(vinaygaba): Color properties aren't working properly with companion objects. This is
    // because the properties are generated outside the companion object in java land(as opposed to
    // inside the companion class for functions). Need to investigate more.
    val commonMetadata = element.extractCommonMetadata(showkaseValidator)
    val showkaseName = getShowkaseName(showkaseColorAnnotation.name, element.name)
    val showkaseGroup = getShowkaseGroup(
        showkaseColorAnnotation.group, commonMetadata.enclosingClass,
    )

    return ShowkaseMetadata.Color(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = commonMetadata.kDoc,
        elementName = element.name,
        packageSimpleName = commonMetadata.moduleName,
        packageName = commonMetadata.packageName,
        enclosingClassName = commonMetadata.enclosingClassName,
        insideWrapperClass = commonMetadata.showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = commonMetadata.showkaseFunctionType.insideObject()
    )
}

internal fun getShowkaseTypographyMetadata(
    element: XFieldElement,
    showkaseValidator: ShowkaseValidator
): ShowkaseMetadata {
    val showkaseTypographyAnnotation = element.requireAnnotation(ShowkaseTypography::class).value

    val commonMetadata = element.extractCommonMetadata(showkaseValidator)
    // TODO(vinaygaba): Typography properties aren't working properly with companion objects.
    // This is because the properties are generated outside the companion object in java land(as
    // opposed to inside the companion class for functions). Need to investigate more.
    val showkaseName = getShowkaseName(showkaseTypographyAnnotation.name, element.name)
    val showkaseGroup = getShowkaseGroup(
        showkaseTypographyAnnotation.group,
        commonMetadata.enclosingClass,
    )

    return ShowkaseMetadata.Typography(
        element = element,
        showkaseName = showkaseName,
        showkaseGroup = showkaseGroup,
        showkaseKDoc = commonMetadata.kDoc,
        elementName = element.name,
        packageSimpleName = commonMetadata.moduleName,
        packageName = commonMetadata.packageName,
        enclosingClassName = commonMetadata.enclosingClassName,
        insideWrapperClass = commonMetadata.showkaseFunctionType == ShowkaseFunctionType.INSIDE_CLASS,
        insideObject = commonMetadata.showkaseFunctionType.insideObject()
    )
}

internal fun XElement.getShowkaseFunctionType(enclosingElement: XMemberContainer): ShowkaseFunctionType {
    return when {
        this.isTopLevel(enclosingElement) -> ShowkaseFunctionType.TOP_LEVEL
        (enclosingElement as? XTypeElement)?.isCompanionObject() == true -> ShowkaseFunctionType.INSIDE_COMPANION_OBJECT
        (enclosingElement as? XTypeElement)?.isKotlinObject() == true -> ShowkaseFunctionType.INSIDE_OBJECT
        enclosingElement is XTypeElement -> ShowkaseFunctionType.INSIDE_CLASS
        else -> throw ShowkaseProcessorException(
            "Function is declared in a way that is not supported by Showkase.",
            this
        )
    }
}


fun XElement.isTopLevel(enclosingElement: XMemberContainer): Boolean {
    return if (isJavac()) {
        // Per enclosingElement kdoc:
        // When running with KAPT, the value will be an XTypeElement.
        // Right now xprocessing doesn't expose the top level details, so we have to use
        // reflection to get kotlin metadata
        val xTypeElement = enclosingElement as? XTypeElement
            ?: throw ShowkaseProcessorException(
                "Expected a type element but got $enclosingElement",
                this
            )

        // JavacTypeElement has a kotlinMetadata property with a custom "KotlinMetadataElement"
        // class type. This is null though if the type doesn't have metadata, such as in the case
        // of a top level function.
        val kotlinMetadata = xTypeElement.getFieldWithReflection<Any?>("kotlinMetadata")
            ?: return true

        val enclosingElementKind = kotlinMetadata
            .getFieldWithReflection<KotlinClassMetadata.Class>("classMetadata")
            .header
            .kind

        enclosingElementKind == FILE_FACADE_KIND
    } else {
        // Per enclosingElement kdoc:
        // When running with KSP, if this function is in source, the value will NOT be an XTypeElement.
        // We don't expect to handle functions from classpath because we only process annotations in source
        enclosingElement !is XTypeElement
    }
}

fun XElement.isJavac(): Boolean {
    @Suppress("TooGenericExceptionCaught")
    return try {
        toJavac()
        true
    } catch (e: Throwable) {
        false
    }
}

internal fun getEnclosingClass(
    showkaseFunctionType: ShowkaseFunctionType,
    enclosingElement: XMemberContainer
): XTypeElement? = when (showkaseFunctionType) {
    ShowkaseFunctionType.TOP_LEVEL -> null
    ShowkaseFunctionType.INSIDE_CLASS, ShowkaseFunctionType.INSIDE_OBJECT -> enclosingElement as XTypeElement
    // Get the class that holds the companion object instead of using the intermediate element
    // that's used to represent the companion object.
    ShowkaseFunctionType.INSIDE_COMPANION_OBJECT -> (enclosingElement as XTypeElement).enclosingTypeElement
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
    enclosingClass: XTypeElement?,
) = when {
    showkaseGroupFromAnnotation.isNotBlank() -> showkaseGroupFromAnnotation
    showkaseGroupFromAnnotation.isBlank() && enclosingClass != null -> enclosingClass.name.capitalize(
        Locale.getDefault()
    )
    else -> "Default Group"
}

internal fun getShowkaseStyleName(
    showkaseStyleFromAnnotation: String,
    isDefaultStyle: Boolean,
) = when {
    showkaseStyleFromAnnotation.isNotBlank() -> showkaseStyleFromAnnotation.replaceFirstChar { it.uppercase() }
    // If style name is not specified but its the default style, just provide the default style name
    isDefaultStyle -> "Default Style"
    else -> null
}
