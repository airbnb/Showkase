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
        componentMetadata: Set<ShowkaseMetadata.Component>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>,
    ): ShowkaseBrowserProperties {
        val (showkaseMetadataWithParameterList, showkaseMetadataWithoutParameterList) =
            componentMetadata
                .partition {
                    it.previewParameterProviderType != null
                }

        // Generate top level property file for components without preview parameter provider
        val withoutParameterPropertyNames =
            showkaseMetadataWithoutParameterList.mapIndexed { index, showkaseMetadata ->
                val propertyName = generatePropertyNameFromMetadata(showkaseMetadata)
                val fileBuilder = getFileBuilder(showkaseMetadata.packageName, propertyName)
                val property =
                    getPropertyForComponentWithoutParameter(propertyName, showkaseMetadata)

                fileBuilder.addPropertyAndGenerateFile(property)
                return@mapIndexed ShowkaseGeneratedMetadata(
                    element = showkaseMetadata.element,
                    propertyName = propertyName,
                    propertyPackage = showkaseMetadata.packageName,
                    type = ShowkaseGeneratedMetadataType.COMPONENTS_WITHOUT_PARAMETER,
                    group = showkaseMetadata.showkaseGroup,
                    name = showkaseMetadata.showkaseName,
                    isDefaultStyle = showkaseMetadata.isDefaultStyle,
                    tags = showkaseMetadata.tags,
                    extraMetadata = showkaseMetadata.extraMetadata,
                )
            }

        // Generate top level property file for components with preview parameter provider
        val withParameterPropertyNames =
            showkaseMetadataWithParameterList.mapIndexed { index, showkaseMetadata ->
                val propertyName = generatePropertyNameFromMetadata(showkaseMetadata)
                val fileBuilder = getFileBuilder(showkaseMetadata.packageName, propertyName)
                val property = getPropertyForComponentWithParameter(propertyName, showkaseMetadata)

                fileBuilder.addPropertyAndGenerateFile(property)

                return@mapIndexed ShowkaseGeneratedMetadata(
                    element = showkaseMetadata.element,
                    propertyName = propertyName,
                    propertyPackage = showkaseMetadata.packageName,
                    type = ShowkaseGeneratedMetadataType.COMPONENTS_WITH_PARAMETER,
                    group = showkaseMetadata.showkaseGroup,
                    name = showkaseMetadata.showkaseName,
                    isDefaultStyle = showkaseMetadata.isDefaultStyle,
                    tags = showkaseMetadata.tags,
                    extraMetadata = showkaseMetadata.extraMetadata,
                )
            }

        // Generate top level property file for colors
        val colorPropertyNames = colorMetadata.mapIndexed { index, color ->
            val propertyName = generatePropertyNameFromMetadata(color)
            val fileBuilder = getFileBuilder(color.packageName, propertyName)
            val colorProperty = getPropertyForMetadata(
                propertyName,
                color,
                ShowkaseBrowserWriter.SHOWKASE_BROWSER_COLOR_CLASS_NAME
            ) { addShowkaseBrowserColor(color) }

            fileBuilder.addPropertyAndGenerateFile(colorProperty)
            return@mapIndexed ShowkaseGeneratedMetadata(
                element = color.element,
                propertyName = propertyName,
                propertyPackage = color.packageName,
                type = ShowkaseGeneratedMetadataType.COLOR,
                group = color.showkaseGroup,
                name = color.showkaseName,
            )
        }

        // Generate top level property file for typography
        val typographyPropertyNames =
            typographyMetadata.mapIndexed { index, typography ->
                val propertyName = generatePropertyNameFromMetadata(typography)
                val fileBuilder = getFileBuilder(typography.packageName, propertyName)
                val typographyProperty = getPropertyForMetadata(
                    propertyName,
                    typography,
                    ShowkaseBrowserWriter.SHOWKASE_BROWSER_TYPOGRAPHY_CLASS_NAME
                ) { addShowkaseBrowserTypography(typography) }
                fileBuilder.addPropertyAndGenerateFile(typographyProperty)
                return@mapIndexed ShowkaseGeneratedMetadata(
                    element = typography.element,
                    propertyName = propertyName,
                    propertyPackage = typography.packageName,
                    type = ShowkaseGeneratedMetadataType.COLOR,
                    group = typography.showkaseGroup,
                    name = typography.showkaseName,
                )
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

    private fun FileSpec.Builder.addPropertyAndGenerateFile(
        propertySpec: PropertySpec,
    ) {
        addProperty(propertySpec)
        build()
            .writeTo(environment.filer, mode = XFiler.Mode.Isolating)
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

    fun zip() = componentsWithPreviewParameters + componentsWithoutPreviewParameters + colors + typography

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
