package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import androidx.room.compiler.processing.XTypeElement
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseColorMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadataFromPreview
import com.airbnb.android.showkase.processor.models.getShowkaseTypographyMetadata
import com.airbnb.android.showkase.processor.models.toModel
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

@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
class ShowkaseProcessor @JvmOverloads constructor(
    kspEnvironment: SymbolProcessorEnvironment? = null
) : BaseProcessor(kspEnvironment) {

    private val logger = ShowkaseExceptionLogger()
    private val showkaseValidator = ShowkaseValidator()

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
        ShowkaseComposable::class.java.name,
        PREVIEW_CLASS_NAME,
        ShowkaseColor::class.java.name,
        ShowkaseTypography::class.java.name,
        ShowkaseRoot::class.java.name,
        ShowkaseScreenshot::class.java.name,
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
        return (showkaseComposablesMetadata + previewComposablesMetadata)
            .dedupeAndSort()
            .toSet()
    }

    private fun processShowkaseAnnotation(
        roundEnvironment: XRoundEnv
    ): Set<ShowkaseMetadata.Component> {
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseComposable::class)
            .mapNotNull { element ->
                showkaseValidator.validateComponentElement(
                    element,
                    ShowkaseComposable::class.java.simpleName
                )
                getShowkaseMetadata(
                    element = element,
                    showkaseValidator = showkaseValidator,
                )
            }.toSet()
    }


    private fun processPreviewAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        return roundEnvironment.getElementsAnnotatedWith(PREVIEW_CLASS_NAME)
            .mapNotNull { element ->
                showkaseValidator.validateComponentElement(
                    element,
                    PREVIEW_SIMPLE_NAME
                )
                getShowkaseMetadataFromPreview(
                    element = element,
                    showkaseValidator = showkaseValidator
                )
            }.toSet()
    }

    private fun writeMetadataFile(uniqueComposablesMetadata: Set<ShowkaseMetadata>) {
        ShowkaseCodegenMetadataWriter(environment).apply {
            generateShowkaseCodegenFunctions(uniqueComposablesMetadata)
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
        "${it.packageName}_${it.enclosingClassName}_${it.elementName}"
    }
        .distinctBy {
            // We also ensure that the component groupName and the component name are unique so 
            // that they don't show up twice in the browser app. 
            "${it.showkaseName}_${it.showkaseGroup}_${it.showkaseStyleName}"
        }
        .sortedBy {
            "${it.packageName}_${it.enclosingClassName}_${it.elementName}"
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
        val screenshotTestElement = getShowkaseScreenshotTestElement(roundEnvironment)

        var showkaseProcessorMetadata = ShowkaseProcessorMetadata()

        // If root element is not present in this module, it means that we only need to write
        // the metadata file for this module so that the root module can use this info to
        // include the composables from this module into the final codegen file.
        writeMetadataFile(componentMetadata + colorMetadata + typographyMetadata)

        if (rootElement != null) {
            // This is the module that should aggregate all the other metadata files and
            // also use the showkaseMetadata set from the current round to write the final file.
            showkaseProcessorMetadata =
                writeShowkaseFiles(
                    rootElement,
                    componentMetadata,
                    colorMetadata,
                    typographyMetadata
                )
        }

        if (screenshotTestElement != null) {
            // Generate screenshot test file if ShowkaseScreenshotTest is present in the root module
            writeScreenshotTestFiles(screenshotTestElement, rootElement, showkaseProcessorMetadata)
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

    private fun getShowkaseScreenshotTestElement(roundEnvironment: XRoundEnv): XTypeElement? {
        val testElements = roundEnvironment.getElementsAnnotatedWith(ShowkaseScreenshot::class)
            .filterIsInstance<XTypeElement>()
        showkaseValidator.validateShowkaseTestElement(testElements, environment)
        return testElements.singleOrNull()
    }

    private fun writeShowkaseFiles(
        rootElement: XTypeElement,
        componentMetadata: Set<ShowkaseMetadata.Component>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>,
    ): ShowkaseProcessorMetadata {
        val generatedShowkaseMetadataOnClasspath =
            getShowkaseCodegenMetadataOnClassPath(environment)
        val classpathComponentMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Component>()
        val classpathColorMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Color>()
        val classpathTypographyMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Typography>()

        val allComponents = componentMetadata + classpathComponentMetadata
        val allColors = colorMetadata + classpathColorMetadata
        val allTypography = typographyMetadata + classpathTypographyMetadata

        writeShowkaseBrowserFiles(
            rootElement,
            allComponents,
            allColors,
            allTypography,
        )

        return ShowkaseProcessorMetadata(
            components = allComponents,
            colors = allColors,
            typography = allTypography
        )
    }

    private fun writeScreenshotTestFiles(
        screenshotTestElement: XTypeElement,
        rootElement: XTypeElement?,
        showkaseProcessorMetadata: ShowkaseProcessorMetadata,
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
            val (_, showkaseMetadataWithoutParameterList) =
                showkaseProcessorMetadata.components.filterIsInstance<ShowkaseMetadata.Component>()
                    .partition {
                        it.previewParameterProviderType != null
                    }
            ShowkaseTestMetadata(
                componentsSize = showkaseMetadataWithoutParameterList.size,
                showkaseProcessorMetadata.colors.size,
                showkaseProcessorMetadata.typography.size,
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
            // We only handle composables without preview parameter for screenshots. This is because
            // there's no way to get information about how many previews are dynamically generated using
            // preview parameter as it happens on run time and our codegen doesn't get enough information
            // to be able to predict how many extra composables the preview parameters extrapolate to.
            // TODO(vinaygaba): Add screenshot testing support for composabable with preview parameters as well
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

    private fun getShowkaseCodegenMetadataOnClassPath(environment: XProcessingEnv): Set<ShowkaseMetadata> {
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
            .map { it.second.toModel(it.first) }
            .toSet()
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
        componentsMetadata: Set<ShowkaseMetadata.Component>,
        colorsMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>,
    ) {
        if (componentsMetadata.isEmpty() && colorsMetadata.isEmpty() && typographyMetadata.isEmpty()) return
        val rootModuleClassName = rootElement.name
        val rootModulePackageName = rootElement.packageName
        showkaseValidator.validateShowkaseComponents(componentsMetadata)

        ShowkaseBrowserWriter(environment).apply {
            generateShowkaseBrowserFile(
                componentsMetadata,
                colorsMetadata,
                typographyMetadata,
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
        componentsSize: Int,
        colorsSize: Int,
        typographySize: Int,
        screenshotTestPackageName: String,
        rootModulePackageName: String,
        testClassName: String,
    ) {
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

    private data class ShowkaseProcessorMetadata(
        val components: Set<ShowkaseMetadata> = setOf(),
        val colors: Set<ShowkaseMetadata> = setOf(),
        val typography: Set<ShowkaseMetadata> = setOf(),
    )

    private data class ShowkaseTestMetadata(
        val componentsSize: Int,
        val colorsSize: Int,
        val typographySize: Int,
    )

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.runtime.Composable"
        const val COMPOSABLE_SIMPLE_NAME = "Composable"
        const val PREVIEW_CLASS_NAME = "androidx.compose.ui.tooling.preview.Preview"
        const val PREVIEW_SIMPLE_NAME = "Preview"
        const val PREVIEW_PARAMETER_CLASS_NAME =
            "androidx.compose.ui.tooling.preview.PreviewParameter"
        const val PREVIEW_PARAMETER_SIMPLE_NAME = "PreviewParameter"
        const val TYPE_STYLE_CLASS_NAME = "androidx.compose.ui.text.TextStyle"
        const val CODEGEN_PACKAGE_NAME = "com.airbnb.android.showkase"
    }
}

