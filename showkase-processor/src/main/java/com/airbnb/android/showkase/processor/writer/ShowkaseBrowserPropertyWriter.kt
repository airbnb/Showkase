package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName

class ShowkaseBrowserPropertyWriter(private val environment: XProcessingEnv) {
    internal fun generateMetadataPropertyFile(
        showkaseComponentMetadata: Set<ShowkaseMetadata.Component>,
        showkaseColorMetadata: Set<ShowkaseMetadata>,
        showkaseTypographyMetadata: Set<ShowkaseMetadata>,
        rootModulePackageName: String,
    ): Triple<Pair<List<String>, List<String>>, List<String>, List<String>> {
        val (showkaseMetadataWithParameterList, showkaseMetadataWithoutParameterList) =
            showkaseComponentMetadata
                .partition {
                    it.previewParameterProviderType != null
                }

        // Generate top level property file for components without preview parameter provider
        val withoutParameterPropertyNames =
            showkaseMetadataWithoutParameterList.mapIndexed { index, showkaseMetadata ->
                val propertyName = generatePropertyNameFromComponentMetadata(showkaseMetadata)
                val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)
                val property = getPropertyForComponentWithoutParameter(propertyName, showkaseMetadata)

                fileBuilder.addPropertyAndGenerateFile(property)
                return@mapIndexed propertyName
            }

        // Generate top level property file for components with preview parameter provider
        val withParameterPropertyNames =
            showkaseMetadataWithParameterList.mapIndexed { index, showkaseMetadata ->
                val propertyName = generatePropertyNameFromComponentMetadata(showkaseMetadata)
                val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)
                val property = getPropertyForComponentWithParameter(propertyName, showkaseMetadata)

                fileBuilder.addPropertyAndGenerateFile(property)

                return@mapIndexed propertyName
            }

        // Generate top level property file for colors
        val colorPropertyNames = showkaseColorMetadata.mapIndexed { index, colorMetadata ->
            val propertyName = generatePropertyNameFromMetadata(colorMetadata)
            val fileBuilder = getFileBuilder(rootModulePackageName, propertyName)
            val colorProperty = getPropertyForMetadata(
                propertyName,
                colorMetadata,
                ShowkaseBrowserWriter.SHOWKASE_BROWSER_COLOR_CLASS_NAME
            ) { addShowkaseBrowserColor(colorMetadata) }

            fileBuilder.addPropertyAndGenerateFile(colorProperty)
            return@mapIndexed propertyName
        }

        // Generate top level property file for typography
        val typographyPropertyNames =
            showkaseTypographyMetadata.mapIndexed { index, typographyMetadata ->
                val typographyPropertyName = generatePropertyNameFromMetadata(typographyMetadata)
                val fileBuilder = getFileBuilder(rootModulePackageName, typographyPropertyName)
                val typographyProperty = getPropertyForMetadata(
                    typographyPropertyName,
                    typographyMetadata,
                    ShowkaseBrowserWriter.SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME
                ) { addShowkaseBrowserTypography(typographyMetadata) }
                fileBuilder.addPropertyAndGenerateFile(typographyProperty)
                return@mapIndexed typographyPropertyName
            }

        return Triple(
            (withoutParameterPropertyNames to withParameterPropertyNames),
            colorPropertyNames,
            typographyPropertyNames
        )
    }

    private fun getPropertyForMetadata(
        propertyName: String,
        colorMetadata: ShowkaseMetadata,
        propertyClassName: ClassName,
        showkaseMetadataCodeblock: CodeBlock.Builder.(ShowkaseMetadata) -> Unit
    ) = PropertySpec.builder(
        propertyName,
        propertyClassName
    ).initializer(
        CodeBlock.Builder().apply {
            showkaseMetadataCodeblock(colorMetadata)
            addLineBreak()
            add(")")
        }
            .build()
    )
        .build()

    private fun getPropertyForComponentWithoutParameter(
        propertyName: String,
        showkaseMetadata: ShowkaseMetadata.Component
    ): PropertySpec {
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
        return property
    }

    private fun getPropertyForComponentWithParameter(
        propertyName: String,
        showkaseMetadata: ShowkaseMetadata.Component
    ): PropertySpec {
        return PropertySpec.builder(
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
        }
            .build()
    }

    private fun generatePropertyNameFromComponentMetadata(
        showkaseMetadata: ShowkaseMetadata.Component,
    ): String {
        val name =
            if (showkaseMetadata.componentIndex != null && showkaseMetadata.componentIndex > 0
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
        return propertyName
    }

    private fun generatePropertyNameFromMetadata(colorMetadata: ShowkaseMetadata) =
        "${colorMetadata.packageName}_${colorMetadata.showkaseGroup}_${colorMetadata.showkaseName}"
            .filter { it.isLetterOrDigit() }

    private fun FileSpec.Builder.addPropertyAndGenerateFile(
        propertySpec: PropertySpec
    ) {
        addProperty(propertySpec)
        build()
            .writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
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
}