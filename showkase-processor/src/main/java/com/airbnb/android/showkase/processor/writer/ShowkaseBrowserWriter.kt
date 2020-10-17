package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowkaseBrowserWriter(private val processingEnv: ProcessingEnvironment) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserFile(
        showkaseComponentMetadata: Set<ShowkaseMetadata>,
        showkaseColorMetadata: Set<ShowkaseMetadata>,
        showkaseTypographyMetadata: Set<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        val showkaseComponentsListClassName = "$rootModuleClassName$CODEGEN_AUTOGEN_CLASS_NAME"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)

        val componentListProperty = initializeComponentProperty(showkaseComponentMetadata)
        val colorListProperty = initializeColorProperty(showkaseColorMetadata)
        val typographyProperty = initializeTypographyProperty(showkaseTypographyMetadata)

        writeFile(
            processingEnv,
            fileBuilder,
            SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            componentListProperty.build(),
            colorListProperty.build(),
            typographyProperty.build(),
            showkaseComponentMetadata + showkaseColorMetadata + showkaseTypographyMetadata,
            getShowkaseProviderInterfaceFunction(
                COMPONENT_INTERFACE_METHOD_NAME,
                COMPONENT_PROPERTY_NAME
            ),
            getShowkaseProviderInterfaceFunction(
                COLOR_INTERFACE_METHOD_NAME,
                COLOR_PROPERTY_NAME
            ),
            getShowkaseProviderInterfaceFunction(
                TYPOGRAPHY_INTERFACE_METHOD_NAME,
                TYPOGRAPHY_PROPERTY_NAME
            )
        )
    }

    private fun initializeComponentProperty(
        showkaseMetadataSet: Set<ShowkaseMetadata>
    ): PropertySpec.Builder {
        val componentListProperty =
            getPropertyList(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME, COMPONENT_PROPERTY_NAME)

        val componentListInitializerCodeBlock =
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.mutableListInitializerCodeBlock()
        val (showkaseMetadataWithParameterList, showkaseMetadataWithoutParameterList) =
            showkaseMetadataSet.filterIsInstance<ShowkaseMetadata.Component>().partition {
                it.previewParameter != null
            }

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
                    addLineBreak()
                    add(
                        "%T().values.iterator().forEach { previewParam -> ",
                        withParameterMetadata.previewParameter
                    )
                    doubleIndent()
                    addLineBreak()
                    add("add(")
                    addLineBreak()
                    doubleIndent()
                    addShowkaseBrowserComponent(withParameterMetadata)
                    closeRoundBracket()
                    doubleUnindent()
                    closeRoundBracket()
                    doubleUnindent()
                    closeCurlyBraces()
                }
                doubleUnindent()
                closeCurlyBraces()
            }
        }
        
        componentListProperty.initializer(componentListInitializerCodeBlock.build())
        return componentListProperty
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
                        showkaseMetadata.enclosingClass,
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
                        showkaseMetadata.enclosingClass,
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

    @Suppress("LongParameterList")
    private fun showkaseBrowserPropertyValue(
        functionPackageName: String,
        enclosingClass: TypeMirror? = null,
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
                .add("\n$fieldPropertyName = %M", composeMember)
                .build()
        }
        // It was declared inside a class.
        insideWrapperClass -> {
            CodeBlock.Builder()
                .add("\n$fieldPropertyName = %T().${fieldName}", enclosingClass)
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            CodeBlock.Builder()
                .add("\n$fieldPropertyName = %T.${fieldName}", enclosingClass)
                .build()
        }
        else -> throw ShowkaseProcessorException("Your field:${fieldName} is declared in a way that " +
                "is not supported by Showkase")
    }

    companion object {
        private const val CODEGEN_AUTOGEN_CLASS_NAME = "Codegen"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"
        private const val COLOR_INTERFACE_METHOD_NAME = "getShowkaseColors"
        private const val TYPOGRAPHY_INTERFACE_METHOD_NAME = "getShowkaseTypography"
        internal const val SHOWKASE_MODELS_PACKAGE_NAME = "com.airbnb.android.showkase.models"
        private const val COMPONENT_PROPERTY_NAME = "componentList"
        private const val COLOR_PROPERTY_NAME = "colorList"
        private const val TYPOGRAPHY_PROPERTY_NAME = "typographyList"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose.runtime", "Composable")
        val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        val SHOWKASE_BROWSER_COLOR_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserColor")
        val SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserTypography")
        val SHOWKASE_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseProvider")
    }
}
