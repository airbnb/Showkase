package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName

class ShowkaseBrowserPropertyWriter(private val environment: XProcessingEnv) {
    internal fun generateMetadataPropertyFile(
        showkaseComponentMetadata: Set<ShowkaseMetadata.Component>,
        showkaseColorMetadata: Set<ShowkaseMetadata>,
        showkaseTypographyMetadata: Set<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ): Triple<Pair<List<String>, List<String>>, List<String>, List<String>> {
        val (showkaseMetadataWithParameterList, showkaseMetadataWithoutParameterList) =
            showkaseComponentMetadata
                .partition {
                    it.previewParameterProviderType != null
                }

        val withoutParameterPropertyNames = showkaseMetadataWithoutParameterList.mapIndexed { index, showkaseMetadata ->
            val name = if (showkaseMetadata.componentIndex != null && showkaseMetadata.componentIndex > 0
            ) {
                "${showkaseMetadata.packageName}_${showkaseMetadata.showkaseGroup}_${showkaseMetadata.showkaseName}_${showkaseMetadata.componentIndex}"
            } else {
                "${showkaseMetadata.packageName}_${showkaseMetadata.showkaseGroup}_${showkaseMetadata.showkaseName}"
            }
            val propertyName = if (showkaseMetadata.showkaseStyleName != null) {
                "${name}_${showkaseMetadata.showkaseStyleName}"
            } else {
                name
            }.filter { it.isLetterOrDigit() }

            val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)

            val property = PropertySpec.builder(
                propertyName,
                ShowkaseBrowserWriter.SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
            )
                .initializer(
                    CodeBlock.Builder().apply {
                        addShowkaseBrowserComponent(showkaseMetadata)
                        addLineBreak()
                        add(")")
                    }
                        .build()
                )
                .build()
            fileBuilder
                .addProperty(property)
                .build()
                .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)

            return@mapIndexed propertyName
        }

        val withParameterPropertyNames = showkaseMetadataWithParameterList.mapIndexed { index, showkaseMetadata ->
            val name = if (showkaseMetadata.componentIndex != null &&
                showkaseMetadata.componentIndex > 0) {
                "${showkaseMetadata.showkaseGroup}_${showkaseMetadata.showkaseName}_${showkaseMetadata.componentIndex}"
            } else {
                "${showkaseMetadata.showkaseGroup}_${showkaseMetadata.showkaseName}"
            }
            val propertyName = if (showkaseMetadata.showkaseStyleName != null) {
                "${name}_${showkaseMetadata.showkaseStyleName}"
            } else { name }.filter { it.isLetterOrDigit() }
            val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)
            val property = PropertySpec.builder(
                propertyName,
                List::class
                    .asTypeName()
                    .parameterizedBy(ShowkaseBrowserWriter.SHOWKASE_BROWSER_COMPONENT_CLASS_NAME)
            ).apply {
                initializer(
                    CodeBlock.Builder().apply {
                        addPreviewProviderComponent(showkaseMetadata)
                    }
                        .build()
                )
            }.build()

            fileBuilder
                .addProperty(property)
                .build()
                .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)

            return@mapIndexed propertyName
        }

        val colorPropertyNames = showkaseColorMetadata.mapIndexed { index, showkaseColorMetadata ->
            val propertyName =
                "${showkaseColorMetadata.packageName}_${showkaseColorMetadata.showkaseGroup}_${showkaseColorMetadata.showkaseName}"
                .filter { it.isLetterOrDigit() }
            val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)
            val colorPropertyName = PropertySpec.builder(
                propertyName,
                ShowkaseBrowserWriter.SHOWKASE_BROWSER_COLOR_CLASS_NAME
            ).initializer(
                CodeBlock.Builder().apply {
                    addShowkaseBrowserColor(showkaseColorMetadata)
                    addLineBreak()
                    add(")")
                }
                    .build()
            )
                .build()

            fileBuilder
                .addProperty(colorPropertyName)
                .build()
                .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)

            return@mapIndexed propertyName
        }

        val typographyPropertyNames = showkaseTypographyMetadata.mapIndexed { index, showkaseTypographyMetadata ->
            val typographyPropertyName =
                "${showkaseTypographyMetadata.packageName}_${showkaseTypographyMetadata.showkaseGroup}_${showkaseTypographyMetadata.showkaseName}"
                    .filter { it.isLetterOrDigit() }
            val fileBuilder = getFileBuilder(rootModulePackageName, typographyPropertyName)
            val typographyProperty = PropertySpec.builder(
                typographyPropertyName,
                ShowkaseBrowserWriter.SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME
            ).initializer(
                CodeBlock.Builder().apply {
                    addShowkaseBrowserTypography(showkaseTypographyMetadata)
                    addLineBreak()
                    add(")")
                }
                    .build()
            )
                .build()

            fileBuilder
                .addProperty(typographyProperty)
                .build()
                .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)

            return@mapIndexed typographyPropertyName
        }

        return Triple((withoutParameterPropertyNames to withParameterPropertyNames), colorPropertyNames, typographyPropertyNames)
    }


}

private fun CodeBlock.Builder.addPreviewProviderComponent(withParameterMetadata: ShowkaseMetadata.Component) {
    addLineBreak()
    add(
        "%T()",
        withParameterMetadata.previewParameterProviderType
    )
    withDoubleIndent {
        addLineBreak()
        add(
            ".values"
        )
        addLineBreak()
        add(
            ".iterator()"
        )
        addLineBreak()
        add(
            ".asSequence()"
        )
        addLineBreak()
        add(
            ".mapIndexed { index, previewParam ->"
        )
        withDoubleIndent {
            addLineBreak()
            withDoubleIndent {
                addShowkaseBrowserComponent(withParameterMetadata, true)
                closeRoundBracket()
            }
        }
        closeCurlyBraces()
        addLineBreak()
        add(".toList()")
    }
}