package com.vinaygaba.codegen.writer

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.vinaygaba.annotation.models.ShowcaseMetadata
import com.vinaygaba.codegen.exceptions.ShowcaseProcessorException
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

class KotlinComposableWriter(private val processingEnv: ProcessingEnvironment) {

    fun generateShowcaseBrowserComponents(showcaseMetadataList: List<ShowcaseMetadata>) {
        if (showcaseMetadataList.isEmpty()) return
        val kaptKotlinDirPath = processingEnv.options[KAPT_KOTLIN_DIR_PATH]
            ?: throw ShowcaseProcessorException("Exception encountered")
        val fileBuilder = FileSpec.builder(CODEGEN_PACKAGE_NAME, FILE_NAME)
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        // List<ShowcaseCodegenMetadata>
        val showcaseCodegenMetadataParameterizedList =
            List::class.asClassName().parameterizedBy(SHOWCASE_CODEGEN_METADATA_CLASS_NAME)

        // val componentList: List<ShowcaseCodegenMetadata>
        val componentListProperty = PropertySpec.builder(
            "componentList",
            showcaseCodegenMetadataParameterizedList
        )

        val componentListInitializerCodeBlock = CodeBlock.Builder()
            .add(
                "listOf<%T>(\n", SHOWCASE_CODEGEN_METADATA_CLASS_NAME
            )
            .indent()

        showcaseMetadataList.forEachIndexed { index, showcaseMetadata ->
            componentListInitializerCodeBlock.addStatement(
                "%T(%S, %S, ",
                SHOWCASE_CODEGEN_METADATA_CLASS_NAME,
                showcaseMetadata.group,
                showcaseMetadata.name
            )

            val composableLambdaCodeBlock = composePreviewFunctionLambda(
                showcaseMetadata.packageName,
                showcaseMetadata.methodName
            )
            componentListInitializerCodeBlock.add(composableLambdaCodeBlock)

            if (index == showcaseMetadataList.lastIndex) {
                componentListInitializerCodeBlock.add(")")
            } else {
                componentListInitializerCodeBlock.add("),")
            }
        }
        componentListInitializerCodeBlock.add(")")

        componentListProperty.initializer(componentListInitializerCodeBlock.build())


        fileBuilder
            .addType(
                TypeSpec.classBuilder(AUTOGEN_CLASS_NAME)
                    .addProperty(componentListProperty.build())
                    .build()
            )

        fileBuilder.build().writeTo(File(kaptKotlinDirPath))
    }

    fun composePreviewFunctionLambda(
        functionPackageName: String,
        composeFunctionName: String
    ): CodeBlock {
        val composeMember = MemberName(functionPackageName, composeFunctionName)
        return CodeBlock.Builder()
            .add("@%T { %M() }", COMPOSE_CLASS_NAME, composeMember)
            .build()
    }

    fun generateShowcaseCodegenMetadataClass(composablePredicate: TypeName) =
        TypeSpec.classBuilder("ShowcaseCodegenMetadata")
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder("group", String::class).build())
                    .addParameter(ParameterSpec.builder("componentName", String::class).build())
                    .addParameter(ParameterSpec.builder("component", composablePredicate).build())
                    .build()
            )
            .addProperty(
                PropertySpec.builder("group", String::class)
                    .initializer("group")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("componentName", String::class)
                    .initializer("componentName")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("component", composablePredicate)
                    .initializer("component")
                    .build()
            )
            .build()

    companion object {
        const val FILE_NAME = "ShowcaseComposables"
        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcasecodegen"
        const val AUTOGEN_CLASS_NAME = "ShowcaseComponents"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose", "Composable")
        val SHOWCASE_CODEGEN_METADATA_CLASS_NAME =
            ClassName("com.vinaygaba.browser.models", "ShowcaseCodegenMetadata")
    }
}
