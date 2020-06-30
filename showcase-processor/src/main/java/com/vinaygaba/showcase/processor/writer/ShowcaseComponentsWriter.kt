package com.vinaygaba.showcase.processor.writer

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
import com.vinaygaba.showcase.processor.models.ShowcaseMetadata
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowcaseComponentsWriter(private val processingEnv: ProcessingEnvironment) {
    internal fun generateShowcaseBrowserComponents(
        showcaseMetadataList: List<ShowcaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        if (showcaseMetadataList.isEmpty()) return
        val fileBuilder = FileSpec.builder(
            rootModulePackageName,
            "$rootModuleClassName$AUTOGEN_CLASS_NAME"
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        // List<ShowcaseCodegenMetadata>
        val showcaseCodegenMetadataParameterizedList =
            List::class.asClassName().parameterizedBy(SHOWCASE_BROWSER_COMPONENT_CLASS_NAME)

        // val componentList: List<ShowcaseCodegenMetadata>
        val componentListProperty = PropertySpec.builder(
            "componentList",
            showcaseCodegenMetadataParameterizedList
        )

        val componentListInitializerCodeBlock = CodeBlock.Builder()
            .add(
                "listOf<%T>(\n",
                SHOWCASE_BROWSER_COMPONENT_CLASS_NAME
            )
            .indent()

        showcaseMetadataList.forEachIndexed { index, showcaseMetadata ->
            componentListInitializerCodeBlock.add("\n")
            componentListInitializerCodeBlock.add(
                "%T(%S, %S, %L, %L,",
                SHOWCASE_BROWSER_COMPONENT_CLASS_NAME,
                showcaseMetadata.showcaseComponentGroup,
                showcaseMetadata.showcaseComponentName,
                showcaseMetadata.showcaseComponentWidthDp,
                showcaseMetadata.showcaseComponentHeightDp
            )
            val composableLambdaCodeBlock = composePreviewFunctionLambda(
                showcaseMetadata.packageName,
                showcaseMetadata.enclosingClass,
                showcaseMetadata.methodName
            )
            componentListInitializerCodeBlock.add("\n")
            componentListInitializerCodeBlock.indent().indent()
            componentListInitializerCodeBlock.add(composableLambdaCodeBlock)
            componentListInitializerCodeBlock.unindent().unindent()

            if (index == showcaseMetadataList.lastIndex) {
                componentListInitializerCodeBlock.add(")")
            } else {
                componentListInitializerCodeBlock.add("),")
            }
        }
        componentListInitializerCodeBlock.add("\n)")

        componentListProperty.initializer(componentListInitializerCodeBlock.build())


        fileBuilder
            .addType(
                TypeSpec.classBuilder("$rootModuleClassName$AUTOGEN_CLASS_NAME")
                    .addSuperinterface(SHOWCASE_COMPONENTS_PROVIDER_CLASS_NAME)
                    .addFunction(
                        getShowcaseComponentsProviderInterfaceFunction()
                    )
                    .addProperty(componentListProperty.build())
                    .build()
            )

        fileBuilder.build().writeTo(processingEnv.filer)
    }

    private fun getShowcaseComponentsProviderInterfaceFunction() =
        FunSpec.builder("getShowcaseComponents")
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
        private const val SHOWCASE_MODELS_PACKAGE_NAME = "com.vinaygaba.showcase.models"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose", "Composable")
        val SHOWCASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWCASE_MODELS_PACKAGE_NAME, "ShowcaseBrowserComponent")
        val SHOWCASE_COMPONENTS_PROVIDER_CLASS_NAME =
            ClassName(SHOWCASE_MODELS_PACKAGE_NAME, "ShowcaseComponentsProvider")
    }
}
