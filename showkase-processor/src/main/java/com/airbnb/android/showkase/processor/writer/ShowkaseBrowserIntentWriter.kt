package com.airbnb.android.showkase.processor.writer

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal class ShowkaseBrowserIntentWriter(
    private val processingEnv: ProcessingEnvironment
) {
    internal fun generateIntentFile(
        rootModulePackageName: String,
        rootModuleClassName: String,
        rootElement: Element
    ) {
        getFileBuilder(
            rootModulePackageName,
            "${rootModuleClassName}$SHOWKASE_BROWSER_INTENT_SUFFIX"
        )
            .addFunction(
                generateIntentFunction(rootModulePackageName, rootModuleClassName, rootElement)
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

    companion object {
        private const val SHOWKASE_BROWSER_INTENT_SUFFIX = "IntentCodegen"
        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val INTENT_FUNCTION_NAME = "createShowkaseBrowserIntent"
        private const val CONTEXT_PARAMETER_NAME = "context"
        private const val CONTEXT_PACKAGE_NAME = "android.content"
        private val CONTEXT_CLASS_NAME =
            ClassName(CONTEXT_PACKAGE_NAME, "Context")
        private val INTENT_CLASS_NAME =
            ClassName(CONTEXT_PACKAGE_NAME, "Intent")
        private val SHOWKASE_BROWSER_ACTIVITY_CLASS_NAME =
            ClassName("com.airbnb.android.showkase.ui", "ShowkaseBrowserActivity")
    }
}
