package com.vinaygaba.showkase.processor.writer

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.vinaygaba.showkase.processor.models.ShowkaseMetadata
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowkaseComponentsWriter(private val processingEnv: ProcessingEnvironment) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserComponents(
        showkaseMetadataList: List<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        if (showkaseMetadataList.isEmpty()) return
        val showkaseComponentsListClassName = "$rootModuleClassName$AUTOGEN_CLASS_NAME"
        val fileBuilder = FileSpec.builder(
            rootModulePackageName,
            showkaseComponentsListClassName
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        // List<ShowkaseCodegenMetadata>
        val showkaseCodegenMetadataParameterizedList =
            List::class.asClassName().parameterizedBy(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME)

        // val componentList: List<ShowkaseCodegenMetadata>
        val componentListProperty = PropertySpec.builder(
            "componentList",
            showkaseCodegenMetadataParameterizedList
        )

        val componentListInitializerCodeBlock = CodeBlock.Builder()
            .add(
                "listOf<%T>(\n",
                SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
            )
            .indent()

        showkaseMetadataList.forEachIndexed { index, showkaseMetadata ->
            componentListInitializerCodeBlock.add("\n")
            componentListInitializerCodeBlock.add(
                "%T(%S, %S, %L, %L,",
                SHOWKASE_BROWSER_COMPONENT_CLASS_NAME,
                showkaseMetadata.showkaseComponentGroup,
                showkaseMetadata.showkaseComponentName,
                showkaseMetadata.showkaseComponentWidthDp,
                showkaseMetadata.showkaseComponentHeightDp
            )
            val composableLambdaCodeBlock = composePreviewFunctionLambda(
                showkaseMetadata.packageName,
                showkaseMetadata.enclosingClass,
                showkaseMetadata.methodName
            )
            componentListInitializerCodeBlock.add("\n")
            componentListInitializerCodeBlock.indent().indent()
            componentListInitializerCodeBlock.add(composableLambdaCodeBlock)
            componentListInitializerCodeBlock.unindent().unindent()

            if (index == showkaseMetadataList.lastIndex) {
                componentListInitializerCodeBlock.add(")")
            } else {
                componentListInitializerCodeBlock.add("),")
            }
        }
        componentListInitializerCodeBlock.add("\n)")

        componentListProperty.initializer(componentListInitializerCodeBlock.build())


        fileBuilder
            .addType(
                TypeSpec.classBuilder(showkaseComponentsListClassName)
                    .addSuperinterface(SHOWKASE_COMPONENTS_PROVIDER_CLASS_NAME)
                    .addFunction(
                        getShowkaseComponentsProviderInterfaceFunction()
                    )
                    .addProperty(componentListProperty.build())
                    .build()
            )

        fileBuilder.build().writeTo(processingEnv.filer)
    }

    private fun getShowkaseComponentsProviderInterfaceFunction() =
        FunSpec.builder("getShowkaseComponents")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return componentList")
            .build()

    private fun composePreviewFunctionLambda(
        functionPackageName: String,
        enclosingClass: TypeMirror? = null,
        composeFunctionName: String
    ): CodeBlock {
        // IF enclosingClass is null, it denotes that the method was a top-level method declaration.
        return if (enclosingClass == null) {
            val composeMember = MemberName(functionPackageName, composeFunctionName)
            CodeBlock.Builder()
                .add(
                    "@%T { %M() }",
                    COMPOSE_CLASS_NAME, composeMember
                )
                .build()
        } else {
            // Otherwise it was declared inside a class.
            CodeBlock.Builder()
                .add(
                    "@%T { %T().${composeFunctionName}() }",
                    COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
    }

    companion object {
        private const val AUTOGEN_CLASS_NAME = "CodegenComponents"
        private const val SHOWKASE_MODELS_PACKAGE_NAME = "com.vinaygaba.showkase.models"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose", "Composable")
        val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        val SHOWKASE_COMPONENTS_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseComponentsProvider")
    }
}
