package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.*
import com.airbnb.android.showkase.annotation.*
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.KAPT_KOTLIN_DIR_PATH
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.models.*
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseExtensionFunctionsWriter
import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.annotation.processing.SupportedOptions
import kotlin.reflect.KClass

@AutoService(Processor::class) // For registering the service
@SupportedOptions(KAPT_KOTLIN_DIR_PATH)
class ShowkaseProcessor : BaseProcessor(
    ShowkaseComposable::class.java.name,
    PREVIEW_CLASS_NAME,
    ShowkaseColor::class.java.name,
    ShowkaseTypography::class.java.name,
    ShowkaseRoot::class.java.name,
) {
    private val logger = ShowkaseExceptionLogger()
    private val showkaseValidator by lazy { ShowkaseValidator(environment) }
    private val composableAnnotationClass: KClass<out Annotation> by lazy {
        @Suppress("UNCHECKED_CAST")
        Class.forName(COMPOSABLE_CLASS_NAME).kotlin as KClass<out Annotation>
    }
    private val previewParameterAnnotationClass: KClass<out Annotation> by lazy {
        @Suppress("UNCHECKED_CAST")
        Class.forName(PREVIEW_PARAMETER_CLASS_NAME).kotlin as KClass<out Annotation>
    }
    private val previewAnnotationClass: KClass<out Annotation> by lazy {
        @Suppress("UNCHECKED_CAST")
        Class.forName(PREVIEW_CLASS_NAME).kotlin as KClass<out Annotation>
    }
    private val textStyleType: XType by lazy {
        environment.requireType(TYPE_STYLE_CLASS_NAME)
    }

    override fun process(environment: XProcessingEnv, round: XRoundEnv) {
        try {
            val componentMetadata = processComponentAnnotation(round)
            val colorMetadata = processColorAnnotation(round)
            val typographyMetadata = processTypographyAnnotation(round)

            processShowkaseMetadata(
                roundEnvironment = round,
                componentMetadata = componentMetadata,
                colorMetadata = colorMetadata,
                typographyMetadata = typographyMetadata
            )
        } catch (exception: ShowkaseProcessorException) {
            logger.logError(exception)
        }
    }

    override fun finish() {
        logger.publishMessages(environment.messager)
    }

    private fun processComponentAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        val showkaseComposablesMetadata = processShowkaseAnnotation(roundEnvironment)
        val previewComposablesMetadata = processPreviewAnnotation(roundEnvironment)
        return (showkaseComposablesMetadata + previewComposablesMetadata)
            .dedupeAndSort()
            .toSet()
    }

    private fun processShowkaseAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseComposable::class)
            .map { element ->
                showkaseValidator.validateComponentElement(
                    element,
                    composableAnnotationClass,
                    ShowkaseComposable::class.java.simpleName,
                    previewParameterAnnotationClass
                )

                getShowkaseMetadata(
                    element = element,
                    showkaseValidator = showkaseValidator,
                    environment = environment,
                    previewParameterType = previewParameterAnnotationClass,
                )
            }.toSet()
    }


    private fun processPreviewAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        return roundEnvironment.getElementsAnnotatedWith(previewAnnotationClass)
            .mapNotNull { element ->
                showkaseValidator.validateComponentElement(
                    element,
                    composableAnnotationClass,
                    previewAnnotationClass.simpleName!!,
                    previewParameterAnnotationClass
                )
                getShowkaseMetadataFromPreview(
                    element = element,
                    showkaseValidator = showkaseValidator,
                    environment = environment,
                    previewTypeClass = previewAnnotationClass,
                    previewParameterTypeClass = previewParameterAnnotationClass
                )
            }.toSet()
    }

    private fun writeMetadataFile(uniqueComposablesMetadata: Set<ShowkaseMetadata>) {
        ShowkaseCodegenMetadataWriter(environment).apply {
            generateShowkaseCodegenFunctions(uniqueComposablesMetadata)
        }
    }

    private fun Collection<ShowkaseMetadata>.dedupeAndSort() = this.distinctBy {
        // It's possible that a composable annotation is annotated with both Preview &
        // ShowkaseComposable(especially if we add more functionality to Showkase and they diverge
        // in the customizations that they offer). In that scenario, its important to dedupe the
        // composables as they will be processed across both the rounds. We first ensure that
        // only distict method's are passed onto the next round. We do this by deduping on
        // the combination of packageName, the wrapper class when available(otherwise it
        // will be null) & the methodName.
        "${it.packageName}_${it.enclosingClass}_${it.elementName}"
    }
        .distinctBy {
            // We also ensure that the component groupName and the component name are unique so
            // that they don't show up twice in the browser app.
            "${it.showkaseName}_${it.showkaseGroup}"
        }
        .sortedBy {
            "${it.packageName}_${it.enclosingClass}_${it.elementName}"
        }

    private fun processColorAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseColor::class).map { element ->
            showkaseValidator.validateColorElement(element, ShowkaseColor::class.java.simpleName)
            getShowkaseColorMetadata(element, showkaseValidator, environment)
        }.toSet()
    }

    private fun processTypographyAnnotation(roundEnvironment: XRoundEnv): Set<ShowkaseMetadata> {
        return roundEnvironment.getElementsAnnotatedWith(ShowkaseTypography::class).map { element ->
            showkaseValidator.validateTypographyElement(
                element,
                ShowkaseTypography::class.java.simpleName,
                textStyleType
            )
            getShowkaseTypographyMetadata(element, showkaseValidator, environment)
        }.toSet()
    }

    private fun processShowkaseMetadata(
        roundEnvironment: XRoundEnv,
        componentMetadata: Set<ShowkaseMetadata>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        val showkaseRootElements = roundEnvironment.getElementsAnnotatedWith(ShowkaseRoot::class)
        val rootElement = showkaseValidator.validateShowkaseRootElement(showkaseRootElements)
        when (rootElement) {
            // If root element is not present in this module, it means that we only need to write
            // the metadata file for this module so that the root module can use this info to
            // include the composables from this module into the final codegen file.
            null -> writeMetadataFile(componentMetadata + colorMetadata + typographyMetadata)
            // Else, this is the module that should aggregate all the other metadata files and
            // also use the showkaseMetadata set from the current round to write the final file.
            else -> {
                writeShowkaseFiles(
                    rootElement,
                    componentMetadata,
                    colorMetadata,
                    typographyMetadata
                )
            }
        }
    }

    private fun writeShowkaseFiles(
        rootElement: XTypeElement,
        componentMetadata: Set<ShowkaseMetadata>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        val generatedShowkaseMetadataOnClasspath = getShowkaseCodegenMetadataOnClassPath()
        val classpathComponentMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Component>()
        val classpathColorMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Color>()
        val classpathTypographyMetadata =
            generatedShowkaseMetadataOnClasspath.filterIsInstance<ShowkaseMetadata.Typography>()

        writeShowkaseBrowserFiles(
            rootElement,
            componentMetadata + classpathComponentMetadata,
            colorMetadata + classpathColorMetadata,
            typographyMetadata + classpathTypographyMetadata
        )

    }

    private fun getShowkaseCodegenMetadataOnClassPath(): Set<ShowkaseMetadata> {
        return environment.getElementsFromPackage(CODEGEN_PACKAGE_NAME)
            .filterIsInstance<XTypeElement>()
            .flatMap { it.getDeclaredMethods() }
            .mapNotNull { element ->
                element.getAnnotation(ShowkaseCodegenMetadata::class)?.toModel(element)
            }
            .toSet()
    }

    private fun writeShowkaseBrowserFiles(
        rootElement: XTypeElement,
        componentsMetadata: Set<ShowkaseMetadata>,
        colorsMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        if (componentsMetadata.isEmpty() && colorsMetadata.isEmpty() && typographyMetadata.isEmpty()) return
        val rootModuleClassName = rootElement.name
        val rootModulePackageName = rootElement.packageName

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

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.runtime.Composable"
        const val PREVIEW_CLASS_NAME = "androidx.compose.ui.tooling.preview.Preview"
        const val PREVIEW_PARAMETER_CLASS_NAME =
            "androidx.compose.ui.tooling.preview.PreviewParameter"
        const val TYPE_STYLE_CLASS_NAME = "androidx.compose.ui.text.TextStyle"

        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.airbnb.android.showkase"
    }
}

// Replace with implementation in https://github.com/androidx/androidx/pull/163
fun XProcessingEnv.getElementsFromPackage(packageName: String): List<XElement> = TODO()

