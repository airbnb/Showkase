package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.CODEGEN_PACKAGE_NAME
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.ShowkaseMetadataType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.Locale
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.util.Types

internal class ShowkaseCodegenMetadataWriter(private val processingEnv: ProcessingEnvironment) {

    internal fun generateShowkaseCodegenFunctions(
        showkaseMetadataSet: Set<ShowkaseMetadata>,
        typeUtil: Types
    ) {
        if (showkaseMetadataSet.isEmpty()) return
        val moduleName = showkaseMetadataSet.first().packageSimpleName
        val generatedClassName = "ShowkaseMetadata${moduleName.capitalize(Locale.getDefault())}"
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(generatedClassName)

        showkaseMetadataSet.forEach { showkaseMetadata ->
            val methodName = when {
                showkaseMetadata.enclosingClass == null -> showkaseMetadata.elementName
                else -> {
                    val enclosingClassName =
                        typeUtil.asElement(showkaseMetadata.enclosingClass).simpleName
                    "${enclosingClassName}_${showkaseMetadata.elementName}"
                }
            }

            val annotation = createShowkaseCodegenMetadata(showkaseMetadata)
            showkaseMetadata.enclosingClass?.let {
                annotation.addMember("enclosingClass = [%T::class]", it)
            }
            addMetadataTypeSpecificProperties(showkaseMetadata, annotation)
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

    private fun createShowkaseCodegenMetadata(showkaseMetadata: ShowkaseMetadata) =
        AnnotationSpec.builder(ShowkaseCodegenMetadata::class)
            .addMember("showkaseName = %S", showkaseMetadata.showkaseName)
            .addMember("showkaseGroup = %S", showkaseMetadata.showkaseGroup)
            .addMember("packageName = %S", showkaseMetadata.packageName)
            .addMember("packageSimpleName = %S", showkaseMetadata.packageSimpleName)
            .addMember("showkaseElementName = %S", showkaseMetadata.elementName)
            .addMember("insideObject = ${showkaseMetadata.insideObject}")
            .addMember("insideWrapperClass = ${showkaseMetadata.insideWrapperClass}")
            .addMember("showkaseKDoc = %S", showkaseMetadata.showkaseKDoc)

    private fun addMetadataTypeSpecificProperties(
        showkaseMetadata: ShowkaseMetadata,
        annotation: AnnotationSpec.Builder
    ) = when (showkaseMetadata) {
        is ShowkaseMetadata.Component -> {
            annotation.apply {
                addMember("showkaseMetadataType = %S", ShowkaseMetadataType.COMPONENT.name)
                showkaseMetadata.showkaseWidthDp?.let {
                    addMember("showkaseWidthDp = %L", it)
                }
                showkaseMetadata.showkaseHeightDp?.let {
                    addMember("showkaseHeightDp = %L", it)
                }
            }
        }
        is ShowkaseMetadata.Color -> {
            annotation.addMember("showkaseMetadataType = %S", ShowkaseMetadataType.COLOR.name)
        }
        is ShowkaseMetadata.Typography -> {
            annotation.addMember("showkaseMetadataType = %S", ShowkaseMetadataType.TYPOGRAPHY.name)
        }
    }
}
