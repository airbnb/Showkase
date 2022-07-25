package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
import com.airbnb.android.showkase.processor.ShowkaseProcessor
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName

internal class ShowkaseBrowserWriter(private val environment: XProcessingEnv) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserFile(
        showkaseComponentMetadata: Set<ShowkaseMetadata.Component>,
        showkaseColorMetadata: Set<ShowkaseMetadata>,
        showkaseTypographyMetadata: Set<ShowkaseMetadata>,
        showkaseIconMetadata: Set<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        val showkaseComponentsListClassName = "${rootModuleClassName}$CODEGEN_AUTOGEN_CLASS_NAME"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)

        val (showkaseMetadataWithParameterList, showkaseMetadataWithoutParameterList) =
            showkaseComponentMetadata
                .partition {
                    it.previewParameterProviderType != null
                }
        val componentListProperty = initializeComponentProperty(
            showkaseMetadataWithParameterList,
            showkaseMetadataWithoutParameterList
        )
        val colorListProperty = initializeColorProperty(showkaseColorMetadata)
        val typographyProperty = initializeTypographyProperty(showkaseTypographyMetadata)
        val iconProperty =
            initializeIconProperty(showkaseIconMetadata, SHOWKASE_BROWSER_ICONS_CLASS_NAME)

        val showkaseRootCodegenAnnotation = initializeShowkaseRootCodegenAnnotation(
            showkaseMetadataWithoutParameterList.size,
            showkaseMetadataWithParameterList.size,
            showkaseColorMetadata.size,
            showkaseTypographyMetadata.size,
            showkaseIconMetadata.size
        )

        writeFile(
            environment,
            fileBuilder,
            SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            componentListProperty.build(),
            colorListProperty.build(),
            typographyProperty.build(),
            iconProperty.build(),
            showkaseComponentMetadata + showkaseColorMetadata + showkaseTypographyMetadata,
            getShowkaseProviderInterfaceFunction(
                methodName = COMPONENT_INTERFACE_METHOD_NAME,
                returnPropertyName = COMPONENT_PROPERTY_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME)
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = COLOR_INTERFACE_METHOD_NAME,
                returnPropertyName = COLOR_PROPERTY_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_COLOR_CLASS_NAME)
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = TYPOGRAPHY_INTERFACE_METHOD_NAME,
                returnPropertyName = TYPOGRAPHY_PROPERTY_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME)
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = ICON_INTERFACE_METHOD_NAME,
                returnPropertyName = ICON_PROPERTY_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_ICONS_CLASS_NAME)
            ),
            showkaseRootCodegenAnnotation
        )
    }

    private fun initializeComponentProperty(
        showkaseMetadataWithParameterList: List<ShowkaseMetadata.Component>,
        showkaseMetadataWithoutParameterList: List<ShowkaseMetadata.Component>
    ): PropertySpec.Builder {
        val componentListProperty =
            getPropertyList(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME, COMPONENT_PROPERTY_NAME)

        val componentListInitializerCodeBlock =
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.mutableListInitializerCodeBlock()

        componentListInitializerCodeBlock.apply {
            showkaseMetadataWithoutParameterList.forEachIndexed { index, withoutParameterMetadata ->
                addLineBreak()
                addShowkaseBrowserComponent(withoutParameterMetadata)
                closeOrContinueListCodeBlock(index, showkaseMetadataWithoutParameterList.size - 1)
            }
            unindent()
            add(")")

            if (showkaseMetadataWithParameterList.isNotEmpty()) {
                add(".apply {")
                doubleIndent()
                showkaseMetadataWithParameterList.forEachIndexed { _, withParameterMetadata ->
                    addProviderComponent(withParameterMetadata)
                }
                doubleUnindent()
                doubleUnindent()
                closeCurlyBraces()
            }
        }

        componentListProperty.initializer(componentListInitializerCodeBlock.build())
        return componentListProperty
    }

    private fun CodeBlock.Builder.addProviderComponent(withParameterMetadata: ShowkaseMetadata.Component) {
        addLineBreak()
        add(
            "%T()",
            withParameterMetadata.previewParameterProviderType
        )
        doubleIndent()
        addLineBreak()
        add(
            ".values"
        )
        addLineBreak()
        add(
            ".iterator()"
        )
        addLineBreak()
        add(
            ".asSequence()"
        )
        addLineBreak()
        add(
            ".forEachIndexed { index, previewParam ->"
        )
        doubleIndent()
        addLineBreak()
        add("add(")
        addLineBreak()
        doubleIndent()
        addShowkaseBrowserComponent(withParameterMetadata, true)
        closeRoundBracket()
        doubleUnindent()
        closeRoundBracket()
        doubleUnindent()
        closeCurlyBraces()
    }

    private fun initializeColorProperty(
        showkaseMetadataSet: Set<ShowkaseMetadata>
    ): PropertySpec.Builder {
        val colorListProperty =
            getPropertyList(
                SHOWKASE_BROWSER_COLOR_CLASS_NAME,
                COLOR_PROPERTY_NAME
            )

        val colorListInitializerCodeBlock =
            SHOWKASE_BROWSER_COLOR_CLASS_NAME.listInitializerCodeBlock()

        showkaseMetadataSet.forEachIndexed { index, showkaseMetadata ->
            colorListInitializerCodeBlock.apply {
                addLineBreak()
                add(
                    "%T(\n",
                    SHOWKASE_BROWSER_COLOR_CLASS_NAME
                )
                doubleIndent()
                add(
                    "colorGroup = %S,\ncolorName = %S,\ncolorKDoc = %S,",
                    showkaseMetadata.showkaseGroup,
                    showkaseMetadata.showkaseName,
                    showkaseMetadata.showkaseKDoc
                )
                add(
                    showkaseBrowserPropertyValue(
                        showkaseMetadata.packageName,
                        showkaseMetadata.enclosingClassName,
                        "color",
                        showkaseMetadata.elementName,
                        showkaseMetadata.insideWrapperClass,
                        showkaseMetadata.insideObject
                    )
                )
                doubleUnindent()
                closeOrContinueListCodeBlock(index, showkaseMetadataSet.size - 1)
            }
        }
        colorListInitializerCodeBlock.apply {
            unindent()
            add(")")
        }
        colorListProperty.initializer(colorListInitializerCodeBlock.build())
        return colorListProperty
    }

    private fun initializeTypographyProperty(
        showkaseMetadataSet: Set<ShowkaseMetadata>
    ): PropertySpec.Builder {
        val typographyListProperty =
            getPropertyList(
                SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME,
                TYPOGRAPHY_PROPERTY_NAME
            )

        val typographyListInitializerCodeBlock =
            SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME.listInitializerCodeBlock()

        showkaseMetadataSet.forEachIndexed { index, showkaseMetadata ->
            typographyListInitializerCodeBlock.apply {
                addLineBreak()
                add(
                    "%T(\n",
                    SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME
                )
                doubleIndent()
                add(
                    "typographyGroup = %S,\ntypographyName = %S,\ntypographyKDoc = %S,",
                    showkaseMetadata.showkaseGroup,
                    showkaseMetadata.showkaseName,
                    showkaseMetadata.showkaseKDoc
                )
                add(
                    showkaseBrowserPropertyValue(
                        showkaseMetadata.packageName,
                        showkaseMetadata.enclosingClassName,
                        "textStyle",
                        showkaseMetadata.elementName,
                        showkaseMetadata.insideWrapperClass,
                        showkaseMetadata.insideObject
                    )
                )
                doubleUnindent()
                closeOrContinueListCodeBlock(index, showkaseMetadataSet.size - 1)
            }
        }
        typographyListInitializerCodeBlock.apply {
            unindent()
            add(")")
        }
        return typographyListProperty.initializer(typographyListInitializerCodeBlock.build())
    }

    private fun initializeIconProperty(
        showkaseIconMetadata: Set<ShowkaseMetadata>,
        returnType: TypeName,
    ): PropertySpec.Builder {
        val iconsListProperty = getPropertyList(
            SHOWKASE_BROWSER_ICONS_CLASS_NAME,
            ICON_PROPERTY_NAME
        )
        val imageVectorType by lazy {
            environment.requireType(ShowkaseProcessor.TYPE_IMAGE_VECTOR_NAME)
        }

        val drawableResourceType by lazy {
            environment.requireType(Int::class)
        }

        val iconListInitializerCodeBlock =
            SHOWKASE_BROWSER_ICONS_CLASS_NAME.listInitializerCodeBlock()
        showkaseIconMetadata.forEachIndexed { index, showkaseMetadata ->
            val element = showkaseMetadata.element
            iconListInitializerCodeBlock.apply {
                addLineBreak()
                add(
                    "%T(\n",
                    SHOWKASE_BROWSER_ICONS_CLASS_NAME
                )
                doubleIndent()
                add(
                    "group = %S,\nname = %S,\nkDoc = %S,",
                    showkaseMetadata.showkaseGroup,
                    showkaseMetadata.showkaseName,
                    showkaseMetadata.showkaseKDoc
                )

                if (element is XFieldElement && element.type.isSameType(imageVectorType)) {
                    add(
                        showkaseBrowserPropertyValue(
                            functionPackageName = showkaseMetadata.packageName,
                            enclosingClass = showkaseMetadata.enclosingClassName,
                            fieldPropertyName = "imageVector",
                            fieldName = showkaseMetadata.elementName,
                            insideWrapperClass = showkaseMetadata.insideWrapperClass,
                            insideObject = showkaseMetadata.insideObject
                        )
                    )
                }

                if (element is XFieldElement && element.type.isSameType(drawableResourceType)) {
                    add(
                        showkaseBrowserPropertyValue(
                            functionPackageName = showkaseMetadata.packageName,
                            enclosingClass = showkaseMetadata.enclosingClassName,
                            fieldPropertyName = "drawableRes",
                            fieldName = showkaseMetadata.elementName,
                            insideWrapperClass = showkaseMetadata.insideWrapperClass,
                            insideObject = showkaseMetadata.insideObject
                        )
                    )
                }
                doubleUnindent()
                closeOrContinueListCodeBlock(index, showkaseIconMetadata.size - 1)
            }
        }
        iconListInitializerCodeBlock.apply {
            unindent()
            add(")")
        }
        return iconsListProperty.initializer(iconListInitializerCodeBlock.build())
    }

    private fun initializeShowkaseRootCodegenAnnotation(
        numComponentsWithoutPreviewParameter: Int,
        numComponentsWithPreviewParameter: Int,
        colorsSize: Int,
        typographySize: Int,
        iconSize: Int,
    ) = AnnotationSpec.builder(ShowkaseRootCodegen::class)
        .addMember(
            "numComposablesWithoutPreviewParameter = %L",
            numComponentsWithoutPreviewParameter
        )
        .addMember("numComposablesWithPreviewParameter = %L", numComponentsWithPreviewParameter)
        .addMember("numColors = %L", colorsSize)
        .addMember("numTypography = %L", typographySize)
        .addMember("numIcons = %L", iconSize)
        .build()

    @Suppress("LongParameterList")
    private fun showkaseBrowserPropertyValue(
        functionPackageName: String,
        enclosingClass: TypeName? = null,
        fieldPropertyName: String,
        fieldName: String,
        insideWrapperClass: Boolean,
        insideObject: Boolean
    ) = when {
        // When enclosingClass is null, it denotes that the method was a top-level method 
        // declaration.
        enclosingClass == null -> {
            val composeMember = MemberName(functionPackageName, fieldName)
            CodeBlock.Builder()
                .add("\n$fieldPropertyName = %M,", composeMember)
                .build()
        }
        // It was declared inside a class.
        insideWrapperClass -> {
            CodeBlock.Builder()
                .add("\n$fieldPropertyName = %T().${fieldName},", enclosingClass)
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            CodeBlock.Builder()
                .add("\n$fieldPropertyName = %T.${fieldName},", enclosingClass)
                .build()
        }
        else -> throw ShowkaseProcessorException(
            "Your field:${fieldName} is declared in a way that " +
                    "is not supported by Showkase"
        )
    }

    companion object {
        internal const val CODEGEN_AUTOGEN_CLASS_NAME = "Codegen"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"
        private const val COLOR_INTERFACE_METHOD_NAME = "getShowkaseColors"
        private const val TYPOGRAPHY_INTERFACE_METHOD_NAME = "getShowkaseTypography"
        private const val ICON_INTERFACE_METHOD_NAME = "getShowkaseIcons"
        internal const val SHOWKASE_MODELS_PACKAGE_NAME = "com.airbnb.android.showkase.models"
        internal const val COMPONENT_PROPERTY_NAME = "componentList"
        internal const val COLOR_PROPERTY_NAME = "colorList"
        internal const val TYPOGRAPHY_PROPERTY_NAME = "typographyList"
        internal const val ICON_PROPERTY_NAME = "iconList"

        internal val COMPOSE_CLASS_NAME = ClassName("androidx.compose.runtime", "Composable")
        internal val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        internal val SHOWKASE_BROWSER_COLOR_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserColor")
        internal val SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserTypography")
        internal val SHOWKASE_BROWSER_ICONS_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserIcon")
        internal val SHOWKASE_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseProvider")
    }
}
