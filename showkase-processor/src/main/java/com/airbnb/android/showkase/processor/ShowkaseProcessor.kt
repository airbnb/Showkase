package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.XAnnotationBox
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import androidx.room.compiler.processing.XTypeElement
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseMultiPreviewCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.ShowkaseMetadataType
import com.airbnb.android.showkase.processor.models.getCodegenMetadataTypes
import com.airbnb.android.showkase.processor.models.getShowkaseColorMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadataFromCustomAnnotation
import com.airbnb.android.showkase.processor.models.getShowkaseMetadataFromPreview
import com.airbnb.android.showkase.processor.models.getShowkaseTypographyMetadata
import com.airbnb.android.showkase.processor.writer.PaparazziShowkaseScreenshotTestWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserProperties
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserPropertyWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.CODEGEN_AUTOGEN_CLASS_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseExtensionFunctionsWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseScreenshotTestWriter
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

class ShowkaseProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ShowkaseProcessor(environment)
    }
}

@SupportedSourceVersion(SourceVersion.RELEASE_17)
class ShowkaseProcessor @JvmOverloads constructor(
    kspEnvironment: SymbolProcessorEnvironment? = null
) : BaseProcessor(kspEnvironment) {

    private val logger = ShowkaseExceptionLogger()
    private val showkaseValidator by lazy { ShowkaseValidator(environment) }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val supportedAnnotations = mutableSetOf(
            ShowkaseComposable::class.java.name,
            PREVIEW_CLASS_NAME,
            ShowkaseColor::class.java.name,
            ShowkaseTypography::class.java.name,
            ShowkaseRoot::class.java.name,
            ShowkaseScreenshot::class.java.name,
            ShowkaseMultiPreviewCodegenMetadata::class.java.name,
        )
        supportedAnnotations.addAll(supportedCustomAnnotationTypes())
        return supportedAnnotations
    }

    // Getting the custom annotations that are supported as an compiler argument.
    // It is expected to get the compiler argument as follows:
    // arg("multiPreviewTypes", "com.airbnb.android.submodule.showkasesample.FontPreview")
    // This should only be provided by KAPT users
    private fun supportedCustomAnnotationTypes(): MutableSet<String> {
        val set = mutableSetOf<String>()
        environment
            .options["multiPreviewType"]
            ?.split(",")?.map { it.replace(" ", "") }
            ?.toSet()?.let { set.addAll(it) }
        return set
    }
    override fun getSupportedOptions() = mutableSetOf(
        "skipPrivatePreviews",
        "requireShowkaseComposableAnnotation",
        "multiPreviewType"
    )

    override fun process(environment: XProcessingEnv, round: XRoundEnv) {
        val componentMetadata = processComponentAnnotation(round)
        val colorMetadata = processColorAnnotation(round)
        val typographyMetadata = processTypographyAnnotation(round, environment)
        processShowkaseMetadata(
            roundEnvironment = round,
            componentMetadata = componentMetadata,
            colorMetadata = colorMetadata,
            typographyMetadata = typographyMetadata
        )
    }

    override fun finish() {
        logger.publishMessages(messager)
    }

    private fun processComponentAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        val showkaseComposablesMetadata = processShowkaseAnnotation(roundEnvironment)
        val previewComposablesMetadata = processPreviewAnnotation(roundEnvironment)

        val customPreviewFromClassPathMetadata = processCustomAnnotationFromClasspath(roundEnvironment)
        return (showkaseComposablesMetadata + previewComposablesMetadata + customPreviewFromClassPathMetadata)
            .dedupeAndSort()
            .toSet()
    }

    private fun processShowkaseAnnotation(
        roundEnvironment: XRoundEnv
    ): Set<ShowkaseMetadata.Component> {
        val skipPrivatePreviews = environment.options["skipPrivatePreviews"] == "true"
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseComposable::class)
            .mapNotNull { element ->
                if (showkaseValidator.checkElementIsAnnotationClass(element)) return@mapNotNull null
                val skipElement = showkaseValidator.validateComponentElementOrSkip(
                    element,
                    ShowkaseComposable::class.java.simpleName,
                    skipPrivatePreviews
                )
                if (skipElement) return@mapNotNull null
                getShowkaseMetadata(
                    element = element,
                    showkaseValidator = showkaseValidator,
                )
            }.flatten().mapNotNull { it }.toSet()
    }

    private fun processPreviewAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        val skipPrivatePreviews = environment.options["skipPrivatePreviews"] == "true"
        val requireShowkaseComposableAnnotation =
            environment.options["requireShowkaseComposableAnnotation"] == "true"

        if (requireShowkaseComposableAnnotation) return emptySet()

        return roundEnvironment.getElementsAnnotatedWith(PREVIEW_CLASS_NAME)
            .mapNotNull { element ->
                if (showkaseValidator.checkElementIsAnnotationClass(element)) {
                    // Writing preview data to a internal annotation to store values through
                    // processing rounds
                    ShowkaseBrowserWriter(environment).writeCustomAnnotationElementToMetadata(
                        element
                    )
                    return@mapNotNull processCustomAnnotation(
                        skipPrivatePreviews = skipPrivatePreviews,
                        roundEnvironment = roundEnvironment,
                        annotation = element
                    )
                }
                val skipElement = showkaseValidator.validateComponentElementOrSkip(
                    element,
                    PREVIEW_SIMPLE_NAME,
                    skipPrivatePreviews
                )
                if (skipElement) return@mapNotNull null
                getShowkaseMetadataFromPreview(
                    element = element,
                    showkaseValidator = showkaseValidator
                )
            }.flatten().mapNotNull { it }.toSet()
    }

    private fun processCustomAnnotation(
        skipPrivatePreviews: Boolean,
        roundEnvironment: XRoundEnv,
        annotation: XTypeElement? = null
    ): Set<ShowkaseMetadata.Component> {
        val supportedTypes = mutableListOf<String>()
        if (annotation != null) supportedTypes.add(annotation.qualifiedName)
        val components = mutableSetOf<ShowkaseMetadata.Component>()

        supportedTypes.map { supportedType ->
            val annotatedElements = roundEnvironment.getElementsAnnotatedWith(supportedType)
            annotatedElements.map { annotatedElement ->
                if (!showkaseValidator.checkElementIsAnnotationClass(annotatedElement)) {

                    val skipable = showkaseValidator.validateComponentElementOrSkip(
                        element = annotatedElement,
                        annotationName = supportedType,
                        skipPrivatePreviews = skipPrivatePreviews
                    )
                    if (!skipable) {
                        components.addAll(
                            getShowkaseMetadataFromCustomAnnotation(
                                element = annotatedElement,
                                showkaseValidator = showkaseValidator,
                                supportedType.getCustomAnnotationSimpleName(),
                            ).toSet()
                        )
                    }
                }
            }
        }
        return components
    }

    private fun String.getCustomAnnotationSimpleName(): String {
        return this.split(".").last()
    }

    private fun processCustomAnnotationFromClasspath(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        // In this function we are checking generated classpath for MultiPreview codegen annotations.
        // We also check the current module if there is any composables that are annotated with the qualified name
        // from the annotation from classpath. We use the fields from the classpath annotation to build
        // common data for the ShowkaseMetadata.

        val skipPrivatePreviews = environment.options["skipPrivatePreviews"] == "true"
        // Supported annotations from classpath
        val supportedCustomPreview = mutableSetOf<ShowkaseMultiPreviewCodegenMetadata>()
        environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .flatMap { it.getEnclosedElements() }.mapNotNull {
                return@mapNotNull when (
                    val annotation = it.getAnnotation(ShowkaseMultiPreviewCodegenMetadata::class)
                ) {
                    null -> {
                        null
                    }
                    else -> {
                        val codeGenAnnotation = ShowkaseMultiPreviewCodegenMetadata(
                            previewName = annotation.value.previewName,
                            previewGroup = annotation.value.previewGroup,
                            supportTypeQualifiedName = annotation.value.supportTypeQualifiedName,
                            packageName = annotation.value.packageName,
                            showkaseWidth = annotation.value.showkaseWidth,
                            showkaseHeight = annotation.value.showkaseHeight,
                        )
                        supportedAnnotationTypes.add(annotation.value.supportTypeQualifiedName)
                        supportedCustomPreview.add(codeGenAnnotation)
                    }
                }
            }
        val components = mutableSetOf<ShowkaseMetadata.Component>()
        supportedCustomPreview
            .mapIndexed { index: Int, customPreviewMetadata: ShowkaseMultiPreviewCodegenMetadata ->
                roundEnvironment
                    .getElementsAnnotatedWith(customPreviewMetadata.supportTypeQualifiedName)
                    .mapIndexed elementRoot@{ elementIndex, xElement ->
                        val skippable = showkaseValidator.validateComponentElementOrSkip(
                            xElement,
                            customPreviewMetadata.supportTypeQualifiedName,
                            skipPrivatePreviews = skipPrivatePreviews
                        )
                        if (!skippable) {
                            components.add(
                                getShowkaseMetadata(
                                    xElement = xElement,
                                    customPreviewMetadata = customPreviewMetadata,
                                    elementIndex = elementIndex,
                                    index = index,
                                    showkaseValidator = showkaseValidator
                                )
                            )
                        }
                    }
            }
        return components
    }

    private fun writeMetadataFile(
        componentMetadata: Set<ShowkaseMetadata.Component>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>,
    ): ShowkaseBrowserProperties {
        val aggregateMetadataList = componentMetadata + colorMetadata + typographyMetadata
        if (aggregateMetadataList.isEmpty()) return ShowkaseBrowserProperties()

        ShowkaseCodegenMetadataWriter(environment).apply {
            generateShowkaseCodegenFunctions(aggregateMetadataList)
        }
        ShowkaseBrowserPropertyWriter(environment).apply {
            return generateMetadataPropertyFiles(
                componentMetadata = componentMetadata,
                colorMetadata = colorMetadata,
                typographyMetadata = typographyMetadata,
            )
        }
    }

    private fun Collection<ShowkaseMetadata.Component>.dedupeAndSort() = this.distinctBy {
        // It's possible that a composable annotation is annotated with both Preview &
        // ShowkaseComposable(especially if we add more functionality to Showkase and they diverge
        // in the customizations that they offer). In that scenario, its important to dedupe the
        // composables as they will be processed across both the rounds. We first ensure that
        // only distict method's are passed onto the next round. We do this by deduping on
        // the combination of packageName, the wrapper class when available(otherwise it
        // will be null) & the methodName.
        if (it.componentIndex != null) {
            "${it.packageName}_${it.enclosingClassName}_${it.elementName}_${it.componentIndex}"
        } else {

            "${it.packageName}_${it.enclosingClassName}_${it.elementName}"
        }
    }
        .distinctBy {
            // We also ensure that the component groupName and the component name are unique so
            // that they don't show up twice in the browser app. This also de-duplicates based
            // on the fully qualified function name to support categorization with additional
            // fields (e.g. tags, extraMetadata, etc) on custom browsers.
            if (it.componentIndex != null) {
                "${it.fqPrefix}_${it.showkaseName}_${it.showkaseGroup}_${it.showkaseStyleName}_${it.componentIndex}"
            } else {
                "${it.fqPrefix}_${it.showkaseName}_${it.showkaseGroup}_${it.showkaseStyleName}"
            }
        }
        .sortedBy {
            it.fqPrefix
        }

    private fun processColorAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseColor::class).map { element ->
            showkaseValidator.validateColorElement(
                element,
                ShowkaseColor::class.java.simpleName
            )
            getShowkaseColorMetadata(element, showkaseValidator)
        }.toSet()
    }

    private fun processTypographyAnnotation(
        roundEnvironment: XRoundEnv,
        environment: XProcessingEnv
    ): Set<ShowkaseMetadata> {
        val textStyleType by lazy {
            environment.requireType(TYPE_STYLE_CLASS_NAME)
        }

        return roundEnvironment.getElementsAnnotatedWith(ShowkaseTypography::class).map { element ->
            showkaseValidator.validateTypographyElement(
                element,
                ShowkaseTypography::class.java.simpleName,
                textStyleType
            )
            getShowkaseTypographyMetadata(element, showkaseValidator)
        }.toSet()
    }

    private fun processShowkaseMetadata(
        roundEnvironment: XRoundEnv,
        componentMetadata: Set<ShowkaseMetadata.Component>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        // Showkase root annotation
        val rootElement = getShowkaseRootElement(roundEnvironment, environment)

        // Showkase test annotation
        val (screenshotTestElement, screenshotTestType) = getShowkaseScreenshotTestElement(
            roundEnvironment
        )

        var showkaseBrowserProperties = ShowkaseBrowserProperties()

        // If root element is not present in this module, it means that we only need to write
        // the metadata file for this module so that the root module can use this info to
        // include the composables from this module into the final codegen file.
        val currentShowkaseBrowserProperties =
            writeMetadataFile(componentMetadata, colorMetadata, typographyMetadata)

        if (rootElement != null) {
            // This is the module that should aggregate all the other metadata files and
            // also use the showkaseMetadata set from the current round to write the final file.
            showkaseBrowserProperties = writeShowkaseFiles(
                rootElement,
                currentShowkaseBrowserProperties
            )
        }

        if (screenshotTestElement != null && screenshotTestType != null) {
            // Generate screenshot test file if ShowkaseScreenshotTest is present in the root module
            writeScreenshotTestFiles(
                screenshotTestElement, screenshotTestType, rootElement,
                showkaseBrowserProperties
            )
        }
    }

    private fun getShowkaseRootElement(
        roundEnvironment: XRoundEnv,
        environment: XProcessingEnv
    ): XTypeElement? {
        val showkaseRootElements = roundEnvironment.getElementsAnnotatedWith(ShowkaseRoot::class)
        showkaseValidator.validateShowkaseRootElement(showkaseRootElements, environment)
        return showkaseRootElements.singleOrNull() as XTypeElement?
    }

    private fun getShowkaseScreenshotTestElement(
        roundEnvironment: XRoundEnv
    ): Pair<XTypeElement?, ScreenshotTestType?> {
        val testElements = roundEnvironment.getElementsAnnotatedWith(ShowkaseScreenshot::class)
            .filterIsInstance<XTypeElement>()
        val screenshotTestType =
            showkaseValidator.validateShowkaseTestElement(testElements, environment)
        return testElements.singleOrNull() to screenshotTestType
    }

    private fun writeShowkaseFiles(
        rootElement: XTypeElement,
        currentShowkaseBrowserProperties: ShowkaseBrowserProperties,
    ): ShowkaseBrowserProperties {
        val generatedShowkaseMetadataOnClasspath =
            getShowkaseCodegenMetadataOnClassPath(environment)
        val classpathComponentsWithoutParameter = generatedShowkaseMetadataOnClasspath.filter {
            it.type == ShowkaseGeneratedMetadataType.COMPONENTS_WITHOUT_PARAMETER
        }
        val classpathComponentsWithParameter = generatedShowkaseMetadataOnClasspath.filter {
            it.type == ShowkaseGeneratedMetadataType.COMPONENTS_WITH_PARAMETER
        }
        val classpathColors =
            generatedShowkaseMetadataOnClasspath.filter {
                it.type == ShowkaseGeneratedMetadataType.COLOR
            }
        val classpathTypography =
            generatedShowkaseMetadataOnClasspath.filter {
                it.type == ShowkaseGeneratedMetadataType.TYPOGRAPHY
            }

        val classpathShowkaseBrowserProperties = ShowkaseBrowserProperties(
            componentsWithoutPreviewParameters = classpathComponentsWithoutParameter,
            componentsWithPreviewParameters = classpathComponentsWithParameter,
            colors = classpathColors,
            typography = classpathTypography
        )
        val allShowkaseBrowserProperties =
            currentShowkaseBrowserProperties + classpathShowkaseBrowserProperties
        writeShowkaseBrowserFiles(rootElement, allShowkaseBrowserProperties)

        return allShowkaseBrowserProperties
    }

    private fun writeScreenshotTestFiles(
        screenshotTestElement: XTypeElement,
        screenshotTestType: ScreenshotTestType,
        rootElement: XTypeElement?,
        showkaseBrowserProperties: ShowkaseBrowserProperties,
    ) {
        val testClassName = screenshotTestElement.name
        val screenshotTestPackageName = screenshotTestElement.packageName

        // Parse the showkase root class that was specified in @ShowkaseScreenshot
        val specifiedRootClassTypeElement = getSpecifiedRootTypeElement(screenshotTestElement)

        // Get the package of the specified root module. We need this to ensure that we use the
        // Showkase.getMetadata metadata from that package.
        val rootModulePackageName = specifiedRootClassTypeElement.packageName

        val showkaseTestMetadata = if (rootElement != null &&
            specifiedRootClassTypeElement.name == rootElement.name
        ) {
            // If the specified root element is the being processed in the current processing round,
            // use it directly instead of looking for it in the class path. This is because it won't
            // be availabe in the classpath just yet.
            ShowkaseTestMetadata(
                componentsSize = showkaseBrowserProperties.componentsWithoutPreviewParameters.size,
                showkaseBrowserProperties.colors.size,
                showkaseBrowserProperties.typography.size,
            )
        } else {
            getShowkaseRootCodegenOnClassPath(specifiedRootClassTypeElement)?.let { showkaseRootCodegenAnnotation ->
                // Else if we were able to find the specified root element in the classpath, we will use
                // the metadata from there instead.
                ShowkaseTestMetadata(
                    componentsSize = showkaseRootCodegenAnnotation.numComposablesWithoutPreviewParameter,
                    colorsSize = showkaseRootCodegenAnnotation.numColors,
                    typographySize = showkaseRootCodegenAnnotation.numTypography
                )
            } ?: throw ShowkaseProcessorException(
                "Showkase was not able to find the root class that you" +
                        "passed to @ShowkaseScreenshot. Make sure that you have configured Showkase correctly.",
                screenshotTestElement
            )
        }

        writeShowkaseScreenshotTestFile(
            screenshotTestType,
            showkaseTestMetadata.componentsSize,
            showkaseTestMetadata.colorsSize,
            showkaseTestMetadata.typographySize,
            screenshotTestPackageName,
            rootModulePackageName,
            testClassName,
        )
    }

    private fun getSpecifiedRootTypeElement(screenshotTestElement: XTypeElement): XTypeElement {
        return screenshotTestElement.requireAnnotation(ShowkaseScreenshot::class)
            .getAsType("rootShowkaseClass")
            ?.typeElement
            ?: throw ShowkaseProcessorException(
                "Unable to get rootShowkaseClass in ShowkaseScreenshot annotation",
                screenshotTestElement
            )
    }

    private fun getShowkaseCodegenMetadataOnClassPath(environment: XProcessingEnv):
            Set<ShowkaseGeneratedMetadata> {
        return environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .flatMap { it.getEnclosedElements() }
            .mapNotNull { element ->
                val codegenMetadataAnnotation =
                    element.getAnnotation(ShowkaseCodegenMetadata::class)
                when {
                    codegenMetadataAnnotation == null -> null
                    else -> element to codegenMetadataAnnotation
                }
            }
            .map {
                it.second.toShowkaseGeneratedMetadata(it.first)
            }
            .toSet()
    }

    private fun XAnnotationBox<ShowkaseCodegenMetadata>.toShowkaseGeneratedMetadata(element: XElement):
            ShowkaseGeneratedMetadata {
        val (_, previewParameterClassType) = getCodegenMetadataTypes()

        // The box is needed to get all Class values, primitives can be accessed dirctly
        val props = value
        val type = ShowkaseMetadataType.valueOf(props.showkaseMetadataType)

        return ShowkaseGeneratedMetadata(
            element = element,
            propertyName = props.generatedPropertyName,
            propertyPackage = props.packageName,
            type = when (type) {
                ShowkaseMetadataType.COLOR -> ShowkaseGeneratedMetadataType.COLOR
                ShowkaseMetadataType.TYPOGRAPHY -> ShowkaseGeneratedMetadataType.TYPOGRAPHY
                ShowkaseMetadataType.COMPONENT -> if (previewParameterClassType != null) {
                    ShowkaseGeneratedMetadataType.COMPONENTS_WITH_PARAMETER
                } else {
                    ShowkaseGeneratedMetadataType.COMPONENTS_WITHOUT_PARAMETER
                }
            },
            group = props.showkaseGroup,
            name = props.showkaseName,
            isDefaultStyle = props.isDefaultStyle,
            tags = props.tags.toList(),
            extraMetadata = props.extraMetadata.toList()
        )
    }
    private fun getShowkaseRootCodegenOnClassPath(
        specifiedRootClassTypeElement: XTypeElement
    ): ShowkaseRootCodegen? {
        return environment
            .findTypeElement("${specifiedRootClassTypeElement.qualifiedName}$CODEGEN_AUTOGEN_CLASS_NAME")
            ?.getAnnotation(ShowkaseRootCodegen::class)?.value
    }

    private fun writeShowkaseBrowserFiles(
        rootElement: XTypeElement,
        allShowkaseBrowserProperties: ShowkaseBrowserProperties,
    ) {
        if (allShowkaseBrowserProperties.isEmpty()) return
        val rootModuleClassName = rootElement.name
        val rootModulePackageName = rootElement.packageName

        showkaseValidator.validateShowkaseComponents(allShowkaseBrowserProperties)

        ShowkaseBrowserWriter(environment).apply {
            generateShowkaseBrowserFile(
                allShowkaseBrowserProperties,
                rootModulePackageName,
                rootModuleClassName
            )
        }

        ShowkaseExtensionFunctionsWriter(environment).apply {
            generateShowkaseExtensionFunctions(
                rootModulePackageName = rootModulePackageName,
                rootModuleClassName = rootModuleClassName,
                rootElement = rootElement
            )
        }
    }

    @Suppress("LongParameterList")
    private fun writeShowkaseScreenshotTestFile(
        screenshotTestType: ScreenshotTestType,
        componentsSize: Int,
        colorsSize: Int,
        typographySize: Int,
        screenshotTestPackageName: String,
        rootModulePackageName: String,
        testClassName: String,
    ) {
        when (screenshotTestType) {
            // We only handle composables without preview parameter for screenshots. This is because
            // there's no way to get information about how many previews are dynamically generated using
            // preview parameter as it happens on run time and our codegen doesn't get enough information
            // to be able to predict how many extra composables the preview parameters extrapolate to.
            // TODO(vinaygaba): Add screenshot testing support for composabable with preview
            //  parameters as well
            ScreenshotTestType.SHOWKASE -> {
                ShowkaseScreenshotTestWriter(environment).apply {
                    generateScreenshotTests(
                        componentsSize,
                        colorsSize,
                        typographySize,
                        screenshotTestPackageName,
                        rootModulePackageName,
                        testClassName
                    )
                }
            }
            ScreenshotTestType.PAPARAZZI_SHOWKASE -> {
                PaparazziShowkaseScreenshotTestWriter(environment).apply {
                    generateScreenshotTests(
                        screenshotTestPackageName,
                        rootModulePackageName,
                        testClassName
                    )
                }
            }
        }
    }

    private data class ShowkaseTestMetadata(
        val componentsSize: Int,
        val colorsSize: Int,
        val typographySize: Int,
    )
    companion object {
        internal const val COMPOSABLE_SIMPLE_NAME = "Composable"
        internal const val PREVIEW_CLASS_NAME = "androidx.compose.ui.tooling.preview.Preview"
        internal const val PREVIEW_SIMPLE_NAME = "Preview"
        internal const val PREVIEW_PARAMETER_SIMPLE_NAME = "PreviewParameter"
        internal const val TYPE_STYLE_CLASS_NAME = "androidx.compose.ui.text.TextStyle"
        internal const val CODEGEN_PACKAGE_NAME = "com.airbnb.android.showkase"
    }
}

internal data class ShowkaseGeneratedMetadata(
    val propertyName: String,
    val propertyPackage: String,
    val type: ShowkaseGeneratedMetadataType,
    val element: XElement,
    val group: String,
    val name: String,
    // This property is only used for components
    val isDefaultStyle: Boolean = false,
    val tags: List<String> = emptyList(),
    val extraMetadata: List<String> = emptyList()
)

internal enum class ShowkaseGeneratedMetadataType {
    COMPONENTS_WITH_PARAMETER,
    COMPONENTS_WITHOUT_PARAMETER,
    COLOR,
    TYPOGRAPHY
}

internal enum class ScreenshotTestType {
    SHOWKASE,
    PAPARAZZI_SHOWKASE
}
