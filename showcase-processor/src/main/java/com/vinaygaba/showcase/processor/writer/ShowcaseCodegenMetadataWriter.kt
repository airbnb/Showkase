package com.vinaygaba.showcase.processor.writer

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.vinaygaba.showcase.processor.models.ShowcaseMetadata
import javax.annotation.processing.ProcessingEnvironment
import com.squareup.kotlinpoet.TypeSpec
import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.processor.ShowcaseProcessor.Companion.CODEGEN_PACKAGE_NAME
import javax.lang.model.util.Types

internal class ShowcaseCodegenMetadataWriter(private val processingEnv: ProcessingEnvironment) {

    internal fun generateShowcaseCodegenFunctions(
        showcaseMetadataList: List<ShowcaseMetadata>,
        typeUtil: Types
    ) {
        if (showcaseMetadataList.isEmpty()) return
        val moduleName = showcaseMetadataList.first().moduleName
        val generatedClassName = "ShowcaseMetadata${moduleName.capitalize()}"
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(generatedClassName)

        showcaseMetadataList.forEachIndexed { index, showcaseMetadata ->
            val methodName = when {
                showcaseMetadata.enclosingClass == null -> showcaseMetadata.methodName
                else -> {
                    val enclosingClassName = typeUtil.asElement(showcaseMetadata.enclosingClass).simpleName
                    "${enclosingClassName}_${showcaseMetadata.methodName}"
                }
            }

            val annotation = AnnotationSpec.builder(ShowcaseCodegenMetadata::class)
                .addMember("showcaseComposableName = %S", showcaseMetadata.showcaseComponentName)
                .addMember("showcaseComposableGroup = %S", showcaseMetadata.showcaseComponentGroup)
                .addMember("showcaseComposableWidthDp = %L", showcaseMetadata.showcaseComponentWidthDp)
                .addMember("showcaseComposableHeightDp = %L", showcaseMetadata.showcaseComponentHeightDp)
                .addMember("packageName = %S", showcaseMetadata.packageName)
                .addMember("moduleName = %S", showcaseMetadata.moduleName)
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
}
