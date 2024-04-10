package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.processor.ShowkaseGeneratedMetadata
import com.airbnb.android.showkase.processor.ShowkaseGeneratedMetadataType
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName

class ShowkaseBrowserPropertyWriter(private val environment: XProcessingEnv) {
    @Suppress("LongMethod")
    internal fun generateMetadataPropertyFiles(
        allMetadata: Set<ShowkaseMetadata>,
    ): ShowkaseBrowserProperties {

        val withoutParameterPropertyNames = mutableListOf<ShowkaseGeneratedMetadata>()
        val withParameterPropertyNames = mutableListOf<ShowkaseGeneratedMetadata>()
        val colorPropertyNames = mutableListOf<ShowkaseGeneratedMetadata>()
        val typographyPropertyNames = mutableListOf<ShowkaseGeneratedMetadata>()

        for ((file, metadataList) in allMetadata.groupBy { it.element.closestMemberContainer }) {
            val fileBuilder = getFileBuilder(
                file.asClassName().packageName,
                "Showkase_${file.name}"
            )
            for (metadata in metadataList) {
                when (metadata) {
                    is ShowkaseMetadata.Component -> {
                        val propertyName = generatePropertyNameFromMetadata(metadata)
                        if (metadata.previewParameterProviderType != null) {
                            val property =
                                getPropertyForComponentWithParameter(propertyName, metadata)
                            fileBuilder.addProperty(property)
                            withParameterPropertyNames.add(
                                ShowkaseGeneratedMetadata(
                                    element = metadata.element,
                                    propertyName = propertyName,
                                    propertyPackage = metadata.packageName,
                                    type = ShowkaseGeneratedMetadataType.COMPONENTS_WITH_PARAMETER,
                                    group = metadata.showkaseGroup,
                                    name = metadata.showkaseName,
                                    isDefaultStyle = metadata.isDefaultStyle,
                                    tags = metadata.tags,
                                    extraMetadata = metadata.extraMetadata,
                                )
                            )
                        } else {
                            val property =
                                getPropertyForComponentWithoutParameter(
                                    propertyName,
                                    metadata
                                )
                            fileBuilder.addProperty(property)
                            withoutParameterPropertyNames.add(
                                ShowkaseGeneratedMetadata(
                                    element = metadata.element,
                                    propertyName = propertyName,
                                    propertyPackage = metadata.packageName,
                                    type = ShowkaseGeneratedMetadataType.COMPONENTS_WITHOUT_PARAMETER,
                                    group = metadata.showkaseGroup,
                                    name = metadata.showkaseName,
                                    isDefaultStyle = metadata.isDefaultStyle,
                                    tags = metadata.tags,
                                    extraMetadata = metadata.extraMetadata,
                                )
                            )
                        }
                    }

                    is ShowkaseMetadata.Color -> {
                        val propertyName = generatePropertyNameFromMetadata(metadata)
                        val colorProperty = getPropertyForMetadata(
                            propertyName,
                            metadata,
                            ShowkaseBrowserWriter.SHOWKASE_BROWSER_COLOR_CLASS_NAME
                        ) { addShowkaseBrowserColor(metadata) }

                        fileBuilder.addProperty(colorProperty)
                        colorPropertyNames.add(
                            ShowkaseGeneratedMetadata(
                                element = metadata.element,
                                propertyName = propertyName,
                                propertyPackage = metadata.packageName,
                                type = ShowkaseGeneratedMetadataType.COLOR,
                                group = metadata.showkaseGroup,
                                name = metadata.showkaseName,
                            )
                        )
                    }

                    is ShowkaseMetadata.Typography -> {
                        val propertyName = generatePropertyNameFromMetadata(metadata)
                        val typographyProperty = getPropertyForMetadata(
                            propertyName,
                            metadata,
                            ShowkaseBrowserWriter.SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME
                        ) { addShowkaseBrowserTypography(metadata) }
                        fileBuilder.addProperty(typographyProperty)
                        typographyPropertyNames.add(
                            ShowkaseGeneratedMetadata(
                                element = metadata.element,
                                propertyName = propertyName,
                                propertyPackage = metadata.packageName,
                                type = ShowkaseGeneratedMetadataType.COLOR,
                                group = metadata.showkaseGroup,
                                name = metadata.showkaseName,
                            )
                        )
                    }
                }
            }
            fileBuilder.build().writeTo(environment.filer, XFiler.Mode.Isolating)
        }

        return ShowkaseBrowserProperties(
            componentsWithoutPreviewParameters = withoutParameterPropertyNames,
            componentsWithPreviewParameters = withParameterPropertyNames,
            colors = colorPropertyNames,
            typography = typographyPropertyNames
        )
    }

    private fun getPropertyForMetadata(
        propertyName: String,
        showkaseMetadata: ShowkaseMetadata,
        propertyClassName: ClassName,
        showkaseMetadataCodeblock: CodeBlock.Builder.(ShowkaseMetadata) -> Unit
    ) = PropertySpec.builder(
        propertyName,
        propertyClassName
    ).initializer(
        CodeBlock.Builder().apply {
            showkaseMetadataCodeblock(showkaseMetadata)
            addLineBreak()
            add(")")
        }
            .build()
    )
        .addOriginatingElement(showkaseMetadata.element)
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
            .addOriginatingElement(showkaseMetadata.element)
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
            .addOriginatingElement(showkaseMetadata.element)
            .build()
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

internal data class ShowkaseBrowserProperties(
    val componentsWithoutPreviewParameters: List<ShowkaseGeneratedMetadata> = listOf(),
    val componentsWithPreviewParameters: List<ShowkaseGeneratedMetadata> = listOf(),
    val colors: List<ShowkaseGeneratedMetadata> = listOf(),
    val typography: List<ShowkaseGeneratedMetadata> = listOf(),
) {
    fun isEmpty() = componentsWithPreviewParameters.isEmpty() &&
            componentsWithoutPreviewParameters.isEmpty() &&
            colors.isEmpty() &&
            typography.isEmpty()

    fun zip() =
        componentsWithPreviewParameters + componentsWithoutPreviewParameters + colors + typography

    operator fun plus(other: ShowkaseBrowserProperties): ShowkaseBrowserProperties {
        return ShowkaseBrowserProperties(
            componentsWithoutPreviewParameters = componentsWithoutPreviewParameters +
                    other.componentsWithoutPreviewParameters,
            componentsWithPreviewParameters = componentsWithPreviewParameters +
                    other.componentsWithPreviewParameters,
            colors = colors + other.colors,
            typography = typography + other.typography
        )
    }
}
