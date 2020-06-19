package com.vinaygaba.showcase.processor.writer

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.vinaygaba.showcase.processor.models.ShowcaseMetadata
import javax.annotation.processing.ProcessingEnvironment
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.vinaygaba.showcase.annotation.models.ShowcaseCodegen
import com.vinaygaba.showcase.processor.logging.ShowcaseExceptionLogger
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class KotlinComposableWriter(private val processingEnv: ProcessingEnvironment) {

    internal fun generateShowcaseCodegenFunctions(
        showcaseMetadataList: List<ShowcaseMetadata>,
        typeUtil: Types?,
        elementUtil: Elements?,
        logger: ShowcaseExceptionLogger
    ) {
        if (showcaseMetadataList.isEmpty()) return
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            FILE_NAME
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(AUTOGEN_CLASS_NAME)

        showcaseMetadataList.forEachIndexed { index, showcaseMetadata ->
            val methodName = when {
                showcaseMetadata.enclosingClass == null -> showcaseMetadata.methodName
                // TODO(vinaygaba): Fix wrapper class name below
                else -> "${showcaseMetadata.enclosingClass.kind}_${showcaseMetadata.methodName}"
            }

            val annotation = AnnotationSpec.builder(ShowcaseCodegen::class)
                .addMember("name = %S", showcaseMetadata.name)
                .addMember("group = %S", showcaseMetadata.group)
                .addMember("widthDp = %L", showcaseMetadata.widthDp)
                .addMember("heightDp = %L", showcaseMetadata.heightDp)
                .addMember("packageName = %S", showcaseMetadata.packageName)
                .addMember("composableMethodName = %S", showcaseMetadata.methodName)

            showcaseMetadata.enclosingClass?.let {
                annotation.addMember("enclosingClass = [%T::class]", it)
            }

            val composableFunction = FunSpec.builder(methodName)
                .addAnnotation(annotation.build())
                .build()

            autogenClass.addFunction(composableFunction)
        }

        fileBuilder.addType(autogenClass.build())

        fileBuilder.build().writeTo(processingEnv.filer)
    }

    internal fun generateShowcaseBrowserComponents2(showcaseMetadataList: List<ShowcaseMetadata>) {
        if (showcaseMetadataList.isEmpty()) return
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            FILE_NAME
        )
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
                "listOf<%T>(\n",
                SHOWCASE_CODEGEN_METADATA_CLASS_NAME
            )
            .indent()

        showcaseMetadataList.forEachIndexed { index, showcaseMetadata ->
            componentListInitializerCodeBlock.add("\n")
            componentListInitializerCodeBlock.add(
                "%T(%S, %S, %L, %L,",
                SHOWCASE_CODEGEN_METADATA_CLASS_NAME,
                showcaseMetadata.group,
                showcaseMetadata.name,
                showcaseMetadata.widthDp,
                showcaseMetadata.heightDp
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
                TypeSpec.classBuilder(AUTOGEN_CLASS_NAME)
                    .addSuperinterface(SHOWCASE_COMPONENTS_PROVIDER_CLASS_NAME)
                    .addFunction(
                        getShowcaseComponentsProviderInterfaceFunction()
                    )
                    .addProperty(componentListProperty.build())
                    .build()
            )

        fileBuilder.build().writeTo(processingEnv.filer)
    }

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

    private fun getShowcaseComponentsProviderInterfaceFunction() =
        FunSpec.builder("getShowcaseComponents")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return componentList")
            .build()

    companion object {
        const val FILE_NAME = "ShowcaseCodegenComponents"

        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcase"
        const val AUTOGEN_CLASS_NAME = "ShowcaseCodegenComponents"
        private const val SHOWCASE_MODELS_PACKAGE_NAME = "com.vinaygaba.showcase.models"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose", "Composable")
        val SHOWCASE_CODEGEN_METADATA_CLASS_NAME =
            ClassName(SHOWCASE_MODELS_PACKAGE_NAME, "ShowcaseCodegenMetadata")
        val SHOWCASE_COMPONENTS_PROVIDER_CLASS_NAME =
            ClassName(SHOWCASE_MODELS_PACKAGE_NAME, "ShowcaseComponentsProvider")
    }
}
