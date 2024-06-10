package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.SHOWKASE_PROVIDER_CLASS_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.initializeColorCodeBlock
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.initializeComponentCodeBlock
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.initializeTypographyCodeBlock
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

object ShowkaseModuleApiWriter {
    @Suppress("LongMethod", "LongParameterList")
    internal fun generateModuleLevelShowkaseProvider(
        environment: XProcessingEnv,
        moduleShowkaseBrowserProperties: ShowkaseBrowserProperties,
    ) {
        if (moduleShowkaseBrowserProperties.isEmpty()) return
        val packageName = moduleShowkaseBrowserProperties.getPackageName()
        val showkaseComponentsListClassName =
            "${MODULE_LEVEL_SHOWKASE_PROVIDER_CLASS_PREFIX}_${packageName.normalizePackageName()}"
        val fileBuilder = getFileBuilder(packageName, showkaseComponentsListClassName)
        val componentCodeBlock = initializeComponentCodeBlock(
            moduleShowkaseBrowserProperties.componentsWithoutPreviewParameters,
            moduleShowkaseBrowserProperties.componentsWithPreviewParameters
        )
        val colorCodeBlock = initializeColorCodeBlock(moduleShowkaseBrowserProperties.colors)
        val typographyCodeBlock =
            initializeTypographyCodeBlock(moduleShowkaseBrowserProperties.typography)
        writeFile(
            environment,
            fileBuilder,
            ShowkaseBrowserWriter.SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            moduleShowkaseBrowserProperties,
            getShowkaseProviderInterfaceFunction(
                methodName = ShowkaseBrowserWriter.COMPONENT_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(ShowkaseBrowserWriter.SHOWKASE_BROWSER_COMPONENT_CLASS_NAME),
                codeBlock = componentCodeBlock
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = ShowkaseBrowserWriter.COLOR_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(ShowkaseBrowserWriter.SHOWKASE_BROWSER_COLOR_CLASS_NAME),
                codeBlock = colorCodeBlock
            ),
            getShowkaseProviderInterfaceFunction(
                methodName = ShowkaseBrowserWriter.TYPOGRAPHY_INTERFACE_METHOD_NAME,
                returnType = LIST.parameterizedBy(ShowkaseBrowserWriter.SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME),
                codeBlock = typographyCodeBlock
            ),
            showkaseRootCodegenAnnotation = null
        )
    }

    internal fun generateModuleMetadataPublicApi(
        environment: XProcessingEnv,
        moduleShowkaseBrowserProperties: ShowkaseBrowserProperties,
    ) {
        if (moduleShowkaseBrowserProperties.isEmpty()) return
        val packageName = moduleShowkaseBrowserProperties.getPackageName()
        val normalizedPackageName = packageName.normalizePackageName()
        val showkaseComponentsListClassName =
            "${MODULE_LEVEL_SHOWKASE_PROVIDER_CLASS_PREFIX}ExtensionFunctions_${normalizedPackageName}"
        val fileBuilder = getFileBuilder(packageName, showkaseComponentsListClassName)

        fileBuilder
            .addFileComment("This is an auto-generated file. Please do not edit/modify this file.")
            .addFunction(
                FunSpec.builder(MODULE_METADATA_FUNCTION_NAME).apply {
                    receiver(ShowkaseExtensionFunctionsWriter.SHOWKASE_OBJECT_CLASS_NAME)
                    returns(ShowkaseExtensionFunctionsWriter.SHOWKASE_ELEMENTS_METADATA_CLASS_NAME)
                    addKdoc(
                        "Helper function that gives you access to Showkase elements that are " +
                                "declared in a given module. This contains data about the composables, " +
                                "colors and typography that are meant to be rendered inside the Showkase " +
                                "browser. This is different from the " +
                                "Showkase.${ShowkaseExtensionFunctionsWriter.METADATA_FUNCTION_NAME}() " +
                                "function, which contains all the Showkase elements in a given " +
                                "ShowkaseRoot graph, whereas this function only contains metadata " +
                                "about the module it's generated in. Each module where Showkase is " +
                                "setup will have this function generated in it."
                    )
                    addCode(
                        CodeBlock.Builder()
                            .indent()
                            .addStatement(
                                "return (%T() as %T).metadata()",
                                ClassName(
                                    packageName,
                                    "${MODULE_LEVEL_SHOWKASE_PROVIDER_CLASS_PREFIX}_${normalizedPackageName}"
                                ),
                                SHOWKASE_PROVIDER_CLASS_NAME
                            )
                            .unindent()
                            .build()
                    )
                    moduleShowkaseBrowserProperties.zip()
                        .forEach { addOriginatingElement(it.element) }
                }
                    .build()
            )
            .build()
            .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
    }

    private const val MODULE_METADATA_FUNCTION_NAME = "getModuleMetadata"
    private const val MODULE_LEVEL_SHOWKASE_PROVIDER_CLASS_PREFIX = "ShowkaseModuleMetadata"
}
