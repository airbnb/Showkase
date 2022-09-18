package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.*
import com.airbnb.android.showkase.annotation.*
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.models.*
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.CODEGEN_AUTOGEN_CLASS_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseExtensionFunctionsWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseScreenshotTestWriter
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.*
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

class ShowkaseProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ShowkaseProcessor(environment)
    }
}

@SupportedSourceVersion(SourceVersion.RELEASE_11) // to support Java 8
class ShowkaseProcessor @JvmOverloads constructor(
    kspEnvironment: SymbolProcessorEnvironment? = null
) : BaseProcessor(kspEnvironment) {

    private val logger = ShowkaseExceptionLogger()
    private val showkaseValidator = ShowkaseValidator()

    override fun getSupportedAnnotationTypes(): MutableSet<String> = supportedAnnotationTypes()

    private fun supportedAnnotationTypes(): MutableSet<String> {
        val set = mutableSetOf(
            ShowkaseComposable::class.java.name,
            PREVIEW_CLASS_NAME,
            ShowkaseColor::class.java.name,
            ShowkaseTypography::class.java.name,
            ShowkaseRoot::class.java.name,
            ShowkaseScreenshot::class.java.name,
        )
        //getSupportedMultipreviewTypes().let {
        //    set.addAll(it)
        //}

        environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .flatMap { it.getEnclosedElements() }
            .map {
                it.getAnnotations(ShowkaseMultiPreviewCodegenMetadata::class)
                    .forEach { annotation ->
                        set.add(annotation.value.supportTypeQualifiedName)
                    }
            }

        return set
    }

    private fun getSupportedMultipreviewTypes(): Set<String> {
        val set = mutableSetOf<String>()
        // This is to check if we have generated any types that we want to support.
        set.addAll(
            environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
                .flatMap { it.getEnclosedElements() }.mapNotNull {
                when (val annotation =
                    it.getAnnotation(ShowkaseMultiPreviewCodegenMetadata::class)) {
                    null -> null
                    else -> annotation.value.supportTypeQualifiedName
                }
            }.toSet()
        )
        environment
            .options["MultipreviewTypes"]
            ?.split(",")?.map { it.replace(" ", "") }
            ?.toSet()?.let { set.addAll(it) }
        return set
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf("skipPrivatePreviews")
    }

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

    private fun processComponentAnnotation(
        roundEnvironment: XRoundEnv,
    ): Set<ShowkaseMetadata.Component> {
        val showkaseComposablesMetadata = processShowkaseAnnotation(roundEnvironment)
        val previewComposablesMetadata = processPreviewAnnotation(roundEnvironment)
        // This is for getting custom annotations from the supported types.
        val customAnnotationMetadata = processCustomAnnotation(roundEnvironment)

        val customAnnotationMetadataFromClassPath = processCustomAnnotationFromClasspath(roundEnvironment)
        return (showkaseComposablesMetadata + previewComposablesMetadata + customAnnotationMetadata + customAnnotationMetadataFromClassPath)
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
        return roundEnvironment.getElementsAnnotatedWith(PREVIEW_CLASS_NAME)
            .mapNotNull { element ->
                if (showkaseValidator.checkElementIsAnnotationClass(element)) {
                    // Here we write to metadata to aggregate custom annotation data
                    writeCustomAnnotationElementToMetadata(element)
                    return@mapNotNull null
                }
                val skipElement = showkaseValidator.validateComponentElementOrSkip(
                    element,
                    PREVIEW_SIMPLE_NAME,
                    skipPrivatePreviews
                )
                if (skipElement) return@mapNotNull null
                showkaseValidator.validateComponentElement(
                    element,
                    PREVIEW_SIMPLE_NAME
                )

                getShowkaseMetadataFromPreview(
                    element = element,
                    showkaseValidator = showkaseValidator
                )

            }.flatten().mapNotNull { it }.toSet()
    }

    // TODO: Move to writer
    // This is to aggregate metadata for the custom annotation annotated with Preview
    private fun writeCustomAnnotationElementToMetadata(element: XElement) {
        val moduleName = "Showkase_test_testy_$element"
        val generatedClassName = "ShowkaseMetadata_${moduleName.lowercase(Locale.getDefault())}"
        val previewAnnotations =
            element.getAllAnnotations().filter { it.name == PREVIEW_SIMPLE_NAME }
        if (!element.isTypeElement()) return
        ShowkaseBrowserWriter(environment).apply {

            val functions = previewAnnotations.mapIndexed { index, xAnnotation ->
                FunSpec.builder("${xAnnotation.name}_$index")
                    .addAnnotation(
                        AnnotationSpec.builder(
                            ShowkaseMultiPreviewCodegenMetadata::class
                        ).addMember("previewName = %S", xAnnotation.get("name"))
                            .addMember("previewGroup = %S", xAnnotation.get("group"))
                            .addMember("supportTypeQualifiedName = %S", element.qualifiedName)
                            .addMember("showkaseWidth = %L", xAnnotation.getAsInt("widthDp"))
                            .addMember("showkaseHeight = %L", xAnnotation.getAsInt("heightDp"))
                            .addMember("packageName = %S", element.packageName)
                            .build()
                    ).build()
            }

            val fileBuilder = FileSpec.builder(
                CODEGEN_PACKAGE_NAME,
                generatedClassName
            )
            fileBuilder.addType(
                TypeSpec.classBuilder(generatedClassName).addFunctions(functions).build()
            )

            fileBuilder.build().writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
        }
    }

    private fun processCustomAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        val supportedCustomAnnotationTypes =
            getSupportedMultipreviewTypes().toList()
        val elementsAnnotatedWithCustomAnnotation =
            supportedCustomAnnotationTypes.map { roundEnvironment.getElementsAnnotatedWith(it) }
                .flatten()
        val elementAnnotationMap =
            supportedCustomAnnotationTypes.zip(elementsAnnotatedWithCustomAnnotation).toMap()
        val metadataSet = elementAnnotationMap.map { (annotation, element) ->
            writeCustomAnnotationElementToMetadata(element)
            showkaseValidator.validateComponentElement(
                element,
                annotation,
            )

            getShowkaseMetadataFromCustomAnnotation(
                element = element,
                showkaseValidator = showkaseValidator,
                annotation.getCustomAnnotationSimpleName(),
                roundEnvironment,
            )

        }.flatten().mapNotNull { it }.toSet()

        return metadataSet
    }

    private fun processCustomAnnotationFromClasspath(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata.Component> {
        // In this function we are checking generated classpath for MultiPreview codegen annotations.
        // We also check the current module if there is any composables that are annotated with the qualified name
        // from the annotation from classpath. We also use the fields from the classpath annotation to build
        // common data for the ShowkasMetadata.

        // Supported annotations from classpath
        val supportedCustomPreview = environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .flatMap { it.getEnclosedElements() }.mapNotNull {
                when (val annotation =
                    it.getAnnotation(ShowkaseMultiPreviewCodegenMetadata::class)) {
                    null -> null
                    else -> annotation.value
                }
            }
        return supportedCustomPreview.flatMap { customPreviewMetadata ->
            roundEnvironment.getElementsAnnotatedWith(customPreviewMetadata.supportTypeQualifiedName).mapIndexed { index, xElement ->
                showkaseValidator.validateComponentElement(
                    xElement,
                    customPreviewMetadata.supportTypeQualifiedName,
                )
                val commonMetadata = xElement.extractCommonMetadata(showkaseValidator)
                //val previewParamMetadata = xElement
                ShowkaseMetadata.Component(
                    element = xElement,
                    elementName = xElement.name,
                    packageName = commonMetadata.packageName,
                    packageSimpleName = commonMetadata.moduleName,
                    showkaseName = customPreviewMetadata.previewName,
                    insideObject = commonMetadata.showkaseFunctionType.insideObject(),
                    previewParameterName = customPreviewMetadata.previewName,
                    previewParameterProviderType = null,
                    showkaseGroup = customPreviewMetadata.previewGroup,
                    showkaseKDoc = commonMetadata.kDoc,
                    enclosingClassName = commonMetadata.enclosingClassName,
                    componentIndex = index,
                    insideWrapperClass = commonMetadata.showkaseFunctionType == ShowkaseFunctionType.INSIDE_OBJECT,
                    showkaseHeightDp = if (customPreviewMetadata.showkaseHeight == -1) null else customPreviewMetadata.showkaseHeight,
                    showkaseWidthDp = if (customPreviewMetadata.showkaseWidth == -1) null else customPreviewMetadata.showkaseWidth,
                    showkaseStyleName = "",
                )

            }
        }.toSet()

    }

    private fun String.getCustomAnnotationSimpleName(): String {
        return this.split(".").last()
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
        if (it.componentIndex != null) {
            "${it.packageName}_${it.enclosingClassName}_${it.elementName}_${it.componentIndex}"
        } else {

            "${it.packageName}_${it.enclosingClassName}_${it.elementName}"
        }
    }
        .distinctBy {
            // We also ensure that the component groupName and the component name are unique so 
            // that they don't show up twice in the browser app.
            if (it.componentIndex != null) {
                "${it.showkaseName}_${it.showkaseGroup}_${it.showkaseStyleName}_${it.componentIndex}"
            } else {
                "${it.showkaseName}_${it.showkaseGroup}_${it.showkaseStyleName}"
            }
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

        // Showkase custom annotations
        val customPreviewAnnotations = getCustomPreviewAnnotation(roundEnvironment, environment)

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
            writeSupportedAnnotationTypesToFile(roundEnvironment)

        }

        if (customPreviewAnnotations != null) {
            writeCustomAnnotationElementToMetadata(customPreviewAnnotations)
        }

        if (screenshotTestElement != null) {
            // Generate screenshot test file if ShowkaseScreenshotTest is present in the root module
            writeScreenshotTestFiles(screenshotTestElement, rootElement, showkaseProcessorMetadata)
        }
    }

    // This is just to test what kinda types are supported at this point
    private fun writeSupportedAnnotationTypesToFile(roundEnvironment: XRoundEnv) {
        val moduleName = "Showkase_test_testy_"
        val generatedClassName = "ShowkaseMetadatas_${moduleName.lowercase(Locale.getDefault())}"

        val fileBuilder = FileSpec.builder(
            CODEGEN_PACKAGE_NAME,
            generatedClassName
        )
        val supportedCustomPreview = environment.getTypeElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .flatMap { it.getEnclosedElements() }.mapNotNull {
                when (val annotation =
                    it.getAnnotation(ShowkaseMultiPreviewCodegenMetadata::class)) {
                    null -> null
                    else -> annotation.value
                }
            }

        val function = supportedCustomPreview.flatMapIndexed {index, s ->
            roundEnvironment.getElementsAnnotatedWith(s.supportTypeQualifiedName).mapIndexed { index, xElement ->

                FunSpec.builder("${xElement.toString().replace(".", "_")}_${s.previewName}_${s.previewGroup}_$index").build()
            }
        }
        fileBuilder.addType(
            TypeSpec.classBuilder(generatedClassName).addFunctions(function).build()
        )

        fileBuilder.build().writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
    }

    private fun getShowkaseRootElement(
        roundEnvironment: XRoundEnv,
        environment: XProcessingEnv
    ): XTypeElement? {
        val showkaseRootElements = roundEnvironment.getElementsAnnotatedWith(ShowkaseRoot::class)
        showkaseValidator.validateShowkaseRootElement(showkaseRootElements, environment)
        return showkaseRootElements.singleOrNull() as XTypeElement?
    }

    private fun getCustomPreviewAnnotation(
        roundEnvironment: XRoundEnv,
        environment: XProcessingEnv
    ): XTypeElement? {
        val customAnnotationElements =
            roundEnvironment.getElementsAnnotatedWith(ShowkaseMultiPreviewCodegenMetadata::class)
        showkaseValidator.validateCustomAnnotation(customAnnotationElements, environment)
        return customAnnotationElements.singleOrNull() as XTypeElement?
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

