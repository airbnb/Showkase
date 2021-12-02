package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.CODEGEN_PACKAGE_NAME
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.ShowkaseMetadataType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.*

internal class ShowkaseCodegenMetadataWriter(private val environment: XProcessingEnv) {

    internal fun generateShowkaseCodegenFunctions(
        showkaseMetadataSet: Set<ShowkaseMetadata>,
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

            val methodName = when (val enclosingClass = showkaseMetadata.enclosingClassName) {
                null -> showkaseMetadata.elementName
                else -> {
                    "${enclosingClass.simpleName}_${showkaseMetadata.elementName}"
                }
            }

            val annotation = createShowkaseCodegenMetadata(showkaseMetadata)
            showkaseMetadata.enclosingClassName?.let {
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

        fileBuilder.build().writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
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
                addMember("isDefaultStyle = ${showkaseMetadata.isDefaultStyle}")
                showkaseMetadata.showkaseWidthDp?.let {
                    addMember("showkaseWidthDp = %L", it)
                }
                showkaseMetadata.showkaseHeightDp?.let {
                    addMember("showkaseHeightDp = %L", it)
                }
                showkaseMetadata.previewParameterProviderType?.let {
                    addMember("previewParameterClass = [%T::class]", it)
                }
                showkaseMetadata.showkaseStyleName?.let {
                    addMember("showkaseStyleName = %S", showkaseMetadata.showkaseStyleName)
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
