package com.airbnb.android.showkase.processor.writer

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.ProcessingEnvironment

internal class ShowkaseBrowserIntentWriter(
    private val processingEnv: ProcessingEnvironment
) {
    internal fun generateIntentFile(
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        val intentFile = 
            getFileBuilder(
                rootModulePackageName, 
                "${rootModuleClassName}$SHOWKASE_BROWSER_INTENT_SUFFIX"
            )
        intentFile.addFunction(generateIntentFunction(rootModulePackageName, rootModuleClassName))
            .build()
            .writeTo(processingEnv.filer)
    }

    private fun generateIntentFunction(
        rootModulePackageName: String,
        rootModuleClassName: String
    ): FunSpec {
        return FunSpec.builder(INTENT_FUNCTION_NAME)
            .addParameter(
                CONTEXT_PARAMETER_NAME, ShowkaseBrowserWriter.CONTEXT_CLASS_NAME
            )
            .returns(
                ShowkaseBrowserWriter.INTENT_CLASS_NAME
            )
            .addCode(
                CodeBlock.Builder()
                    .addStatement(
                        "val intent = %T(%L, %T::class.java)",
                        ShowkaseBrowserWriter.INTENT_CLASS_NAME,
                        CONTEXT_PARAMETER_NAME,
                        ShowkaseBrowserWriter.SHOWKASE_BROWSER_ACTIVITY_CLASS_NAME
                    )
                    .addStatement(
                        "intent.putExtra(%S, %S)",
                        SHOWKASE_ROOT_MODULE_KEY,
                        "$rootModulePackageName.$rootModuleClassName"
                    )
                    .addStatement(
                        "return intent"
                    )
                    .build()
            )
            .build()
    }

    companion object {
        private const val SHOWKASE_BROWSER_INTENT_SUFFIX = "IntentCodegen"
        private const val SHOWKASE_ROOT_MODULE_KEY = "SHOWKASE_ROOT_MODULE"
        private const val INTENT_FUNCTION_NAME = "createShowkaseBrowserIntent"
        private const val CONTEXT_PARAMETER_NAME = "context"
    }
}
