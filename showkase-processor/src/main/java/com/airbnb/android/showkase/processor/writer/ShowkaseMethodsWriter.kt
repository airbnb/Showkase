package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.SHOWKASE_MODELS_PACKAGE_NAME
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal class ShowkaseMethodsWriter(
    private val processingEnv: ProcessingEnvironment
) {
    internal fun generateShowkaseMethodsObject(
        rootModulePackageName: String,
        rootModuleClassName: String,
        rootElement: Element
    ) {
        getFileBuilder(
            rootModulePackageName,
            "${rootModuleClassName}$SHOWKASE_BROWSER_INTENT_SUFFIX"
        )
            .addType(
                TypeSpec.objectBuilder(SHOWKASE_METHODS_OBJECT_NAME)
                    .addFunction(
                        generateIntentFunction(
                            rootModulePackageName, 
                            rootModuleClassName, 
                            rootElement
                        )
                    )
                    .addFunction(
                        generateMetadataFunction(
                            rootElement, 
                            "$rootModulePackageName.$rootModuleClassName"
                        )
                    )
                    .build()
            )
            .build()
            .writeTo(processingEnv.filer)
    }

    private fun generateIntentFunction(
        rootModulePackageName: String,
        rootModuleClassName: String,
        rootElement: Element,
    ) = FunSpec.builder(INTENT_FUNCTION_NAME).apply {
        addParameter(
            CONTEXT_PARAMETER_NAME, CONTEXT_CLASS_NAME
        )
        returns(INTENT_CLASS_NAME)
        addCode(
            CodeBlock.Builder()
                .indent()
                .addStatement(
                    "val intent = %T(%L, %T::class.java)",
                    INTENT_CLASS_NAME,
                    CONTEXT_PARAMETER_NAME,
                    SHOWKASE_BROWSER_ACTIVITY_CLASS_NAME
                )
                .addStatement(
                    "intent.putExtra(%S, %S)",
                    SHOWKASE_ROOT_MODULE_KEY,
                    "$rootModulePackageName.$rootModuleClassName"
                )
                .addStatement(
                    "return intent"
                )
                .unindent()
                .build()
        )
        addOriginatingElement(rootElement)
    }
        .build()

    private fun generateMetadataFunction(
        rootElement: Element,
        classKey: String
    ) = FunSpec.builder(METADATA_FUNCTION_NAME).apply {
        returns(SHOWKASE_ELEMENTS_METADATA_CLASS_NAME)
        addCode(
            CodeBlock.Builder()
                .indent()
                .addStatement("try {")
                .indent()
                .addStatement(
                    "val showkaseComponentProvider = Class.forName(\"${classKey}Codegen\").newInstance() as %T",
                    SHOWKASE_PROVIDER_CLASS_NAME
                )
                .generateShowkaseElementProperty(
                    "componentsMap", 
                    "getShowkaseComponents", 
                    "group"
                )
                .generateShowkaseElementProperty(
                    "colorsMap", 
                    "getShowkaseColors", 
                    "colorGroup"
                )
                .generateShowkaseElementProperty(
                    "typographyMap", 
                    "getShowkaseTypography", 
                    "typographyGroup"
                )
                .addStatement(
                    "return %T(%L, %L, %L)",
                    SHOWKASE_ELEMENTS_METADATA_CLASS_NAME,
                    "componentsMap",
                    "colorsMap",
                    "typographyMap"
                )
                .unindent()
                .addStatement("} catch(exception: ClassNotFoundException) {")
                .indent()
                .addStatement("return %T()", SHOWKASE_ELEMENTS_METADATA_CLASS_NAME)
                .unindent()
                .addStatement("}")
                .unindent()
                .build()
        )
        addOriginatingElement(rootElement)
    }
        .build()

    private fun CodeBlock.Builder.generateShowkaseElementProperty(
        variableName: String,
        elementMethodName: String,
        groupPropertyName: String
    ) = addStatement(
        "val $variableName = %L.$elementMethodName().groupBy { it.$groupPropertyName }",
        "showkaseComponentProvider"
    )

    companion object {
        private const val SHOWKASE_BROWSER_INTENT_SUFFIX = "IntentCodegen"
        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val INTENT_FUNCTION_NAME = "createShowkaseBrowserIntent"
        private const val METADATA_FUNCTION_NAME = "getShowkaseElementsMetadata"
        private const val SHOWKASE_METHODS_OBJECT_NAME = "ShowkaseMethods"
        private const val CONTEXT_PARAMETER_NAME = "context"
        private const val CONTEXT_PACKAGE_NAME = "android.content"
        private val CONTEXT_CLASS_NAME =
            ClassName(CONTEXT_PACKAGE_NAME, "Context")
        private val INTENT_CLASS_NAME =
            ClassName(CONTEXT_PACKAGE_NAME, "Intent")
        private val SHOWKASE_BROWSER_ACTIVITY_CLASS_NAME =
            ClassName("com.airbnb.android.showkase.ui", "ShowkaseBrowserActivity")
        private val SHOWKASE_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseProvider")
        private val SHOWKASE_ELEMENTS_METADATA_CLASS_NAME = 
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseElementsMetadata")
    }
}
