package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.isTypeElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.annotation.ShowkaseMultiPreviewCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
import com.airbnb.android.showkase.processor.ShowkaseGeneratedMetadata
import com.airbnb.android.showkase.processor.ShowkaseProcessor
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import java.util.Locale

internal class ShowkaseBrowserWriter(private val environment: XProcessingEnv) {
    @Suppress("LongMethod", "LongParameterList")
    internal fun generateShowkaseBrowserFile(
        allShowkaseBrowserProperties: ShowkaseBrowserProperties,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        val showkaseComponentsListClassName = "${rootModuleClassName}$CODEGEN_AUTOGEN_CLASS_NAME"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)
        val (componentCodeBlock, componentChunkFunctions) = initializeComponentCodeBlock(
            allShowkaseBrowserProperties.componentsWithoutPreviewParameters,
            allShowkaseBrowserProperties.componentsWithPreviewParameters
        )
        val colorCodeBlock = initializeColorCodeBlock(allShowkaseBrowserProperties.colors)
        val typographyCodeBlock =
            initializeTypographyCodeBlock(allShowkaseBrowserProperties.typography)

        val showkaseRootCodegenAnnotation = initializeShowkaseRootCodegenAnnotation(
            allShowkaseBrowserProperties.componentsWithoutPreviewParameters.size,
            allShowkaseBrowserProperties.componentsWithPreviewParameters.size,
            allShowkaseBrowserProperties.colors.size,
            allShowkaseBrowserProperties.typography.size
        )

        writeFile(
            environment,
            fileBuilder,
            SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            allShowkaseBrowserProperties,
            getShowkaseProviderInterfaceFunction(
                methodName = COMPONENT_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME),
                codeBlock = componentCodeBlock
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = COLOR_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_COLOR_CLASS_NAME),
                codeBlock = colorCodeBlock
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = TYPOGRAPHY_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME),
                codeBlock = typographyCodeBlock
            ),
            showkaseRootCodegenAnnotation,
            additionalFunctions = componentChunkFunctions,
        )
    }

    /**
     * Builds the body of `getShowkaseComponents()`.
     *
     * When the component count exceeds [COMPONENT_LIST_CHUNK_SIZE],
     * the list is split across private helper functions of at most
     * [COMPONENT_LIST_CHUNK_SIZE] entries each and `getShowkaseComponents()` concatenates them.
     * Smaller lists keep the original single-call shape.
     *
     * Returns the code block for `getShowkaseComponents()` and the helper functions to add to
     * the generated class.
     */
    private fun initializeComponentCodeBlock(
        withoutParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
        withParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
    ): Pair<CodeBlock, List<FunSpec>> {
        if (withoutParameterPropertyNames.size > COMPONENT_LIST_CHUNK_SIZE) {
            return initializeChunkedComponentCodeBlock(
                withoutParameterPropertyNames,
                withParameterPropertyNames
            )
        }
        return initializeSingleComponentCodeBlock(
            withoutParameterPropertyNames,
            withParameterPropertyNames
        ) to emptyList()
    }

    private fun initializeChunkedComponentCodeBlock(
        withoutParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
        withParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
    ): Pair<CodeBlock, List<FunSpec>> {
        val componentListType = LIST.parameterizedBy(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME)
        val chunkFunctions = withoutParameterPropertyNames
            .chunked(COMPONENT_LIST_CHUNK_SIZE)
            .mapIndexed { chunkIndex, chunk ->
                val chunkCodeBlock = SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
                    .listInitializerCodeBlock()
                    .apply {
                        addLineBreak()
                        chunk.forEach { metadata ->
                            add("%M,", MemberName(metadata.propertyPackage, metadata.propertyName))
                            addLineBreak()
                        }
                        doubleUnindent()
                        add(")")
                    }
                    .build()
                FunSpec.builder("$COMPONENT_INTERFACE_METHOD_NAME$chunkIndex")
                    .addModifiers(KModifier.PRIVATE)
                    .returns(componentListType)
                    .addCode(chunkCodeBlock)
                    .build()
            }

        val codeBlock = SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
            .mutableListInitializerCodeBlock()
            .apply {
                doubleUnindent()
                add(").apply {")
                addLineBreak()
                withDoubleIndent {
                    chunkFunctions.forEach { chunkFunction ->
                        add("addAll(%N())", chunkFunction)
                        addLineBreak()
                    }
                    withParameterPropertyNames.forEachIndexed { index, metadata ->
                        add(
                            "addAll(%M)",
                            MemberName(metadata.propertyPackage, metadata.propertyName)
                        )
                        if (index != withParameterPropertyNames.lastIndex) {
                            addLineBreak()
                        }
                    }
                }
                closeCurlyBraces()
            }
            .build()

        return codeBlock to chunkFunctions
    }

    private fun initializeSingleComponentCodeBlock(
        withoutParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
        withParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
    ): CodeBlock {
        val componentListInitializerCodeBlock = if (withParameterPropertyNames.isNotEmpty()) {
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.mutableListInitializerCodeBlock()
        } else {
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.listInitializerCodeBlock()
        }

        componentListInitializerCodeBlock.apply {
            addLineBreak()
            withoutParameterPropertyNames.forEachIndexed { index, metadata ->
                add("%M,", MemberName(metadata.propertyPackage, metadata.propertyName))
                addLineBreak()
            }
            doubleUnindent()
            add(")")

            if (withParameterPropertyNames.isNotEmpty()) {
                add(".apply {")
                addLineBreak()
                withDoubleIndent {
                    withParameterPropertyNames.forEachIndexed { index, metadata ->
                        add(
                            "addAll(%M)",
                            MemberName(metadata.propertyPackage, metadata.propertyName)
                        )
                        if (index != withParameterPropertyNames.lastIndex) {
                            addLineBreak()
                        }
                    }
                }
                closeCurlyBraces()
            }
        }

        return componentListInitializerCodeBlock.build()
    }

    private fun initializeColorCodeBlock(
        colorsParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
    ): CodeBlock {
        val colorListInitializerCodeBlock =
            SHOWKASE_BROWSER_COLOR_CLASS_NAME.listInitializerCodeBlock()

        return colorListInitializerCodeBlock.apply {
            addLineBreak()
            colorsParameterPropertyNames.forEachIndexed { index, metadata ->
                add("%M,", MemberName(metadata.propertyPackage, metadata.propertyName))
                addLineBreak()
            }
            doubleUnindent()
            add(")")
        }.build()
    }

    private fun initializeTypographyCodeBlock(
        typographyParameterPropertyNames: List<ShowkaseGeneratedMetadata>,
    ): CodeBlock {
        val typographyListInitializerCodeBlock =
            SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME.listInitializerCodeBlock()

        return typographyListInitializerCodeBlock.apply {
            addLineBreak()
            typographyParameterPropertyNames.forEachIndexed { index, metadata ->
                add("%M,", MemberName(metadata.propertyPackage, metadata.propertyName))
                addLineBreak()
            }
            doubleUnindent()
            add(")")
        }.build()
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

    // This is to aggregate metadata for the custom annotation annotated with Preview
    internal fun writeCustomAnnotationElementToMetadata(element: XElement) {
        if (!element.isTypeElement()) return
        if (element.isAnnotationClass() && element.qualifiedName == ShowkaseProcessor.PREVIEW_CLASS_NAME) return

        val moduleName = "Showkase_${element.qualifiedName.replace(".", "_")}"
        val generatedClassName =
            "ShowkaseMetadata_${moduleName.lowercase(Locale.getDefault())}"

        val previewAnnotations =
            element.getAllAnnotations().filter { it.name == ShowkaseProcessor.PREVIEW_SIMPLE_NAME }

        val fileBuilder = FileSpec.builder(
            ShowkaseProcessor.CODEGEN_PACKAGE_NAME,
            generatedClassName
        )

        val functions = previewAnnotations.mapIndexed { index, xAnnotation ->
            FunSpec.builder("${xAnnotation.name}_$index")
                .addAnnotation(
                    AnnotationSpec
                        .builder(ShowkaseMultiPreviewCodegenMetadata::class)
                        .addMember("previewName = %S", xAnnotation.getAsString("name"))
                        .addMember("previewGroup = %S", xAnnotation.getAsString("group"))
                        .addMember("supportTypeQualifiedName = %S", element.qualifiedName)
                        .addMember("showkaseWidth = %L", xAnnotation.getAsInt("widthDp"))
                        .addMember("showkaseHeight = %L", xAnnotation.getAsInt("heightDp"))
                        .addMember("packageName = %S", element.packageName)
                        .build()
                ).build()
        }

        fileBuilder.addType(
            with(TypeSpec.classBuilder(generatedClassName).addFunctions(functions)) {
                addOriginatingElement(element)
                build()
            }
        ).addFileComment("This is an auto-generated file. Please do not edit/modify this file.")
        fileBuilder.build().writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
    }

    companion object {
        internal const val CODEGEN_AUTOGEN_CLASS_NAME = "Codegen"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"

        /**
         * Max number of components listed in a single generated function. Each entry costs
         * roughly 8 bytes of bytecode, so this keeps every function far below the JVM's 64KB
         * method limit.
         */
        internal const val COMPONENT_LIST_CHUNK_SIZE = 500
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
