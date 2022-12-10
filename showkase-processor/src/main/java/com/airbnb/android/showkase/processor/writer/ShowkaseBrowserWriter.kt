package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
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
        withoutParameterPropertyNames: List<String>,
        withParameterPropertyNames: List<String>,
        colorsParameterPropertyNames: List<String>,
        typographyParameterPropertyNames: List<String>,
        showkaseColorMetadata: Set<ShowkaseMetadata>,
        showkaseTypographyMetadata: Set<ShowkaseMetadata>,
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
            withoutParameterPropertyNames,
            withParameterPropertyNames
        )
        val colorListProperty = initializeColorProperty(colorsParameterPropertyNames)
        val typographyProperty = initializeTypographyProperty(typographyParameterPropertyNames)

        val showkaseRootCodegenAnnotation = initializeShowkaseRootCodegenAnnotation(
            showkaseMetadataWithoutParameterList.size,
            showkaseMetadataWithParameterList.size,
            showkaseColorMetadata.size,
            showkaseTypographyMetadata.size
        )

        writeFile(
            environment,
            fileBuilder,
            SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            componentListProperty.build(),
            colorListProperty.build(),
            typographyProperty.build(),
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
            showkaseRootCodegenAnnotation
        )
    }

    private fun initializeComponentProperty(
        withoutParameterPropertyNames: List<String>,
        withParameterPropertyNames: List<String>,
    ): PropertySpec.Builder {
        val componentListProperty =
            getPropertyList(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME, COMPONENT_PROPERTY_NAME)

        val componentListInitializerCodeBlock =
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.mutableListInitializerCodeBlock()

        componentListInitializerCodeBlock.apply {
            addLineBreak()
            withoutParameterPropertyNames.forEachIndexed { index, property ->
                add("%L,", property)
                addLineBreak()
            }
            unindent()
            add(")")

            if (withParameterPropertyNames.isNotEmpty()) {
                add(".apply {")
                addLineBreak()
                withDoubleIndent {
                    withParameterPropertyNames.forEachIndexed { index, property ->
                        add("addAll(%L)", property)
                        if (index != withParameterPropertyNames.lastIndex) {
                            addLineBreak()
                        }
                    }
                }
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
        withDoubleIndent {
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
            withDoubleIndent {
                addLineBreak()
                add("add(")
                addLineBreak()
                withDoubleIndent {
                    addShowkaseBrowserComponent(withParameterMetadata, true)
                    closeRoundBracket()
                }
                closeRoundBracket()
            }
            closeCurlyBraces()
        }
    }

    private fun initializeColorProperty(
        colorsParameterPropertyNames: List<String>,
    ): PropertySpec.Builder {
        val colorListProperty =
            getPropertyList(
                SHOWKASE_BROWSER_COLOR_CLASS_NAME,
                COLOR_PROPERTY_NAME
            )

        val colorListInitializerCodeBlock =
            SHOWKASE_BROWSER_COLOR_CLASS_NAME.listInitializerCodeBlock()

        colorListInitializerCodeBlock.apply {
            addLineBreak()
            colorsParameterPropertyNames.forEachIndexed { index, property ->
                add("%L,", property)
                addLineBreak()
            }
            unindent()
            add(")")
        }
        colorListProperty.initializer(colorListInitializerCodeBlock.build())
        return colorListProperty
    }

    private fun initializeTypographyProperty(
        typographyParameterPropertyNames: List<String>,
    ): PropertySpec.Builder {
        val typographyListProperty = getPropertyList(
            SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME,
            TYPOGRAPHY_PROPERTY_NAME
        )
        val typographyListInitializerCodeBlock =
            SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME.listInitializerCodeBlock()

        typographyListInitializerCodeBlock.apply {
            addLineBreak()
            typographyParameterPropertyNames.forEachIndexed { index, property ->
                add("%L,", property)
                addLineBreak()
            }
            unindent()
            add(")")
        }
        typographyListProperty.initializer(typographyListInitializerCodeBlock.build())
        return typographyListProperty
    }

    private fun initializeShowkaseRootCodegenAnnotation(
        numComponentsWithoutPreviewParameter: Int,
        numComponentsWithPreviewParameter: Int,
        colorsSize: Int,
        typographySize: Int,
    ) = AnnotationSpec.builder(ShowkaseRootCodegen::class)
        .addMember(
            "numComposablesWithoutPreviewParameter = %L",
            numComponentsWithoutPreviewParameter
        )
        .addMember("numComposablesWithPreviewParameter = %L", numComponentsWithPreviewParameter)
        .addMember("numColors = %L", colorsSize)
        .addMember("numTypography = %L", typographySize)
        .build()

    companion object {
        internal const val CODEGEN_AUTOGEN_CLASS_NAME = "Codegen"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"
        private const val COLOR_INTERFACE_METHOD_NAME = "getShowkaseColors"
        private const val TYPOGRAPHY_INTERFACE_METHOD_NAME = "getShowkaseTypography"
        internal const val SHOWKASE_MODELS_PACKAGE_NAME = "com.airbnb.android.showkase.models"
        internal const val COMPONENT_PROPERTY_NAME = "componentList"
        internal const val COLOR_PROPERTY_NAME = "colorList"
        internal const val TYPOGRAPHY_PROPERTY_NAME = "typographyList"

        internal val COMPOSE_CLASS_NAME = ClassName("androidx.compose.runtime", "Composable")
        internal val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        internal val SHOWKASE_BROWSER_COLOR_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserColor")
        internal val SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserTypography")
        internal val SHOWKASE_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseProvider")
    }
}
