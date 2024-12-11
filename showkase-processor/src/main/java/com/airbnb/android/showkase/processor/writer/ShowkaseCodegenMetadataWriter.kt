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
import java.util.Locale

internal class ShowkaseCodegenMetadataWriter(private val environment: XProcessingEnv) {

    internal fun generateShowkaseCodegenFunctions(
        showkaseMetadataSet: Set<ShowkaseMetadata>,
    ) {
        val moduleName = showkaseMetadataSet.first().packageName.replace(".", "_")
        val generatedClassName = "ShowkaseMetadata_${moduleName.lowercase(Locale.getDefault())}"
        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
            .addFileComment("This is an auto-generated file. Please do not edit/modify this file.")

        val autogenClass = TypeSpec.classBuilder(generatedClassName)

        showkaseMetadataSet.forEach { showkaseMetadata ->

            val name = if (
                showkaseMetadata is ShowkaseMetadata.Component &&
                showkaseMetadata.componentIndex != null &&
                showkaseMetadata.componentIndex > 0
            ) {
                "${showkaseMetadata.fqPrefix}_${showkaseMetadata.showkaseGroup}" +
                    "_${showkaseMetadata.showkaseName}_${showkaseMetadata.componentIndex}"
            } else {
                "${showkaseMetadata.fqPrefix}_${showkaseMetadata.showkaseGroup}" +
                    "_${showkaseMetadata.showkaseName}"
            }
            val methodName = if (showkaseMetadata is ShowkaseMetadata.Component &&
                showkaseMetadata.showkaseStyleName != null
            ) {
                "${name}_${showkaseMetadata.showkaseStyleName}"
            } else {
                name
            }.filter { it.isLetterOrDigit() }

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

    private fun createShowkaseCodegenMetadata(showkaseMetadata: ShowkaseMetadata): AnnotationSpec.Builder =
        AnnotationSpec.builder(ShowkaseCodegenMetadata::class)
            .addMember("showkaseName = %S", showkaseMetadata.showkaseName)
            .addMember("showkaseGroup = %S", showkaseMetadata.showkaseGroup)
            .addMember("packageName = %S", showkaseMetadata.packageName)
            .addMember("packageSimpleName = %S", showkaseMetadata.packageSimpleName)
            .addMember("showkaseElementName = %S", showkaseMetadata.elementName)
            .addMember("insideObject = ${showkaseMetadata.insideObject}")
            .addMember("insideWrapperClass = ${showkaseMetadata.insideWrapperClass}")
            .addMember("showkaseKDoc = %S", showkaseMetadata.showkaseKDoc)
            .addMember("generatedPropertyName = %S", generatePropertyNameFromMetadata(showkaseMetadata))

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
                showkaseMetadata.previewParameterName?.let {
                    addMember("previewParameterName = %S", it)
                }
                showkaseMetadata.showkaseStyleName?.let {
                    addMember("showkaseStyleName = %S", showkaseMetadata.showkaseStyleName)
                }
                addStringArrayMember(ShowkaseCodegenMetadata::tags.name, showkaseMetadata.tags)
                addStringArrayMember(ShowkaseCodegenMetadata::extraMetadata.name, showkaseMetadata.extraMetadata)
            }
        }
        is ShowkaseMetadata.Color -> {
            annotation.addMember("showkaseMetadataType = %S", ShowkaseMetadataType.COLOR.name)
        }
        is ShowkaseMetadata.Typography -> {
            annotation.addMember("showkaseMetadataType = %S", ShowkaseMetadataType.TYPOGRAPHY.name)
        }
    }

    private fun AnnotationSpec.Builder.addStringArrayMember(name: String, values: List<String>) {
        val valueAsArray = values.joinToString(", ", prefix = "[", postfix = "]") { value ->
            "\"$value\""
        }
        values.takeIf { it.isNotEmpty() }?.let {
            addMember("%L = %L", name, valueAsArray)
        }
    }
}
