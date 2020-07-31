package com.airbnb.showkase.processor.writer

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.airbnb.showkase.processor.models.ShowkaseMetadata
import javax.annotation.processing.ProcessingEnvironment
import com.squareup.kotlinpoet.TypeSpec
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.processor.ShowkaseProcessor.Companion.CODEGEN_PACKAGE_NAME
import javax.lang.model.util.Types

internal class ShowkaseCodegenMetadataWriter(private val processingEnv: ProcessingEnvironment) {

    internal fun generateShowkaseCodegenFunctions(
        showkaseMetadataList: List<ShowkaseMetadata>,
        typeUtil: Types
    ) {
        if (showkaseMetadataList.isEmpty()) return
        val moduleName = showkaseMetadataList.first().moduleName
        val generatedClassName = "ShowkaseMetadata${moduleName.capitalize()}"
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(generatedClassName)

        showkaseMetadataList.forEachIndexed { index, showkaseMetadata ->
            val methodName = when {
                showkaseMetadata.enclosingClass == null -> showkaseMetadata.methodName
                else -> {
                    val enclosingClassName =
                        typeUtil.asElement(showkaseMetadata.enclosingClass).simpleName
                    "${enclosingClassName}_${showkaseMetadata.methodName}"
                }
            }

            val annotation = AnnotationSpec.builder(ShowkaseCodegenMetadata::class)
                .addMember("showkaseComposableName = %S", showkaseMetadata.showkaseComponentName)
                .addMember("showkaseComposableGroup = %S", showkaseMetadata.showkaseComponentGroup)
                .addMember(
                    "showkaseComposableWidthDp = %L",
                    showkaseMetadata.showkaseComponentWidthDp
                )
                .addMember(
                    "showkaseComposableHeightDp = %L",
                    showkaseMetadata.showkaseComponentHeightDp
                )
                .addMember("packageName = %S", showkaseMetadata.packageName)
                .addMember("moduleName = %S", showkaseMetadata.moduleName)
                .addMember("composableMethodName = %S", showkaseMetadata.methodName)
                .addMember("insideObject = ${showkaseMetadata.insideObject}")
                .addMember("insideWrapperClass = ${showkaseMetadata.insideWrapperClass}")

            showkaseMetadata.enclosingClass?.let {
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
