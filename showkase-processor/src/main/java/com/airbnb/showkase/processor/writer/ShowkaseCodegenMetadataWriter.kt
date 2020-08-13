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
        showkaseMetadataSet: Set<ShowkaseMetadata>,
        typeUtil: Types
    ) {
        if (showkaseMetadataSet.isEmpty()) return
        val moduleName = showkaseMetadataSet.first().moduleName
        val generatedClassName = "ShowkaseMetadata${moduleName.capitalize()}"
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(generatedClassName)

        showkaseMetadataSet.forEach { showkaseMetadata ->
            val methodName = when {
                showkaseMetadata.enclosingClass == null -> showkaseMetadata.showkaseElementName
                else -> {
                    val enclosingClassName =
                        typeUtil.asElement(showkaseMetadata.enclosingClass).simpleName
                    "${enclosingClassName}_${showkaseMetadata.showkaseElementName}"
                }
            }

            val annotation = AnnotationSpec.builder(ShowkaseCodegenMetadata::class)
                .addMember("showkaseName = %S", showkaseMetadata.showkaseName)
                .addMember("showkaseGroup = %S", showkaseMetadata.showkaseGroup)
                .addMember("packageName = %S", showkaseMetadata.packageName)
                .addMember("moduleName = %S", showkaseMetadata.moduleName)
                .addMember("showkaseElementName = %S", showkaseMetadata.showkaseElementName)
                .addMember("insideObject = ${showkaseMetadata.insideObject}")
                .addMember("insideWrapperClass = ${showkaseMetadata.insideWrapperClass}")
                .addMember("showkaseKDoc = %S", showkaseMetadata.showkaseKDoc)
                .addMember("showkaseMetadataType = %S", showkaseMetadata.showkaseMetadataType.name)
            showkaseMetadata.enclosingClass?.let {
                annotation.addMember("enclosingClass = [%T::class]", it)
            }
            showkaseMetadata.showkaseWidthDp?.let {
                annotation.addMember("showkaseWidthDp = %L", it) 
            }
            showkaseMetadata.showkaseHeightDp?.let {
                annotation.addMember("showkaseHeightDp = %L", it)
            }
            autogenClass.addFunction(
                FunSpec.builder(methodName)
                    .addAnnotation(annotation.build())
                    .build()
            )
        }

        fileBuilder.addType(
            with(autogenClass) {
                showkaseMetadataSet.forEach { addOriginatingElement(it.element) }
                build()
            }
        )

        fileBuilder.build().writeTo(processingEnv.filer)
    }
}
