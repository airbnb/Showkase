package com.airbnb.android.showkase.processor

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.airbnb.android.showkase.processor.ShowkaseProcessor.Companion.KAPT_KOTLIN_DIR_PATH
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.android.showkase.processor.logging.ShowkaseValidator
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseColorMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadata
import com.airbnb.android.showkase.processor.models.getShowkaseMetadataFromPreview
import com.airbnb.android.showkase.processor.models.getShowkaseTypographyMetadata
import com.airbnb.android.showkase.processor.models.toModel
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserIntentWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter
import com.airbnb.android.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(KAPT_KOTLIN_DIR_PATH)
class ShowkaseProcessor: AbstractProcessor() {
    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filter: Filer
    private lateinit var messager: Messager
    private val logger = ShowkaseExceptionLogger()
    private val showkaseValidator = ShowkaseValidator()
    private lateinit var composableTypeMirror: TypeMirror
    private lateinit var textStyleTypeMirror: TypeMirror

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filter = processingEnv.filer
        messager = processingEnv.messager
        composableTypeMirror = elementUtils
            .getTypeElement(Class.forName(COMPOSABLE_CLASS_NAME).canonicalName)
            .asType()
        textStyleTypeMirror = elementUtils
            .getTypeElement(Class.forName(TYPE_STYLE_CLASS_NAME).canonicalName)
            .asType()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
        ShowkaseComposable::class.java.name,
        PREVIEW_CLASS_NAME,
        ShowkaseColor::class.java.name,
        ShowkaseTypography::class.java.name,
        ShowkaseRoot::class.java.name,
    )

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        try {
            val componentMetadata = processComponentAnnotation(roundEnvironment)
            val colorMetadata = processColorAnnotation(roundEnvironment)
            val typographyMetadata = processTypographyAnnotation(roundEnvironment)
            
            processShowkaseMetadata(
                roundEnvironment = roundEnvironment, 
                componentMetadata = componentMetadata, 
                colorMetadata = colorMetadata,
                typographyMetadata = typographyMetadata
            )
        } catch (exception: ShowkaseProcessorException) {
            logger.logErrorMessage("${exception.message}")
        }

        if (roundEnvironment.processingOver()) {
            logger.publishMessages(messager)
        }
        return false
    }

    private fun processComponentAnnotation(roundEnvironment: RoundEnvironment)
            : Set<ShowkaseMetadata> {
        val showkaseComposablesMetadata = processShowkaseAnnotation(roundEnvironment)
        val previewComposablesMetadata = processPreviewAnnotation(roundEnvironment)
        return (showkaseComposablesMetadata + previewComposablesMetadata)
            .dedupeAndSort()
            .toSet()
    }

    private fun processShowkaseAnnotation(roundEnvironment: RoundEnvironment) =
        roundEnvironment.getElementsAnnotatedWith(ShowkaseComposable::class.java).map { element ->
            showkaseValidator.validateComponentElement(
                element, composableTypeMirror, typeUtils,
                ShowkaseComposable::class.java.simpleName
            )
            getShowkaseMetadata(
                element = element as ExecutableElement, elementUtil = elementUtils, typeUtils = typeUtils,
                showkaseValidator = showkaseValidator
            )
        }.toSet()

    private fun processPreviewAnnotation(roundEnvironment: RoundEnvironment): Set<ShowkaseMetadata> {
        val previewClass = Class.forName(PREVIEW_CLASS_NAME)
        val previewClassAnnotation = (previewClass as Class<out Annotation>)
        val previewTypeMirror = elementUtils
            .getTypeElement(previewClass.canonicalName)
            .asType()
        return roundEnvironment.getElementsAnnotatedWith(previewClassAnnotation).mapNotNull { element ->
            showkaseValidator.validateComponentElement(element, composableTypeMirror, typeUtils, 
                previewClass.simpleName)
            val showkaseMetadata = getShowkaseMetadataFromPreview(
                element as ExecutableElement, elementUtils, typeUtils, previewTypeMirror, showkaseValidator
            )
            showkaseMetadata
        }.toSet()
    }

    private fun writeMetadataFile(uniqueComposablesMetadata: Set<ShowkaseMetadata>) {
        ShowkaseCodegenMetadataWriter(processingEnv).apply {
            generateShowkaseCodegenFunctions(uniqueComposablesMetadata, typeUtils)
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

    private fun processColorAnnotation(roundEnvironment: RoundEnvironment) =
        roundEnvironment.getElementsAnnotatedWith(ShowkaseColor::class.java).map { element ->
            showkaseValidator.validateColorElement(element, ShowkaseColor::class.java.simpleName)
            getShowkaseColorMetadata(element, elementUtils, typeUtils, showkaseValidator)
        }.toSet()
    
    private fun processTypographyAnnotation(roundEnvironment: RoundEnvironment) = 
        roundEnvironment.getElementsAnnotatedWith(ShowkaseTypography::class.java).map { element ->
            showkaseValidator.validateTypographyElement(element, 
                ShowkaseTypography::class.java.simpleName, textStyleTypeMirror, typeUtils)
            getShowkaseTypographyMetadata(element, elementUtils, typeUtils, showkaseValidator)
        }.toSet()

    private fun processShowkaseMetadata(
        roundEnvironment: RoundEnvironment,
        componentMetadata: Set<ShowkaseMetadata>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        val showkaseRootElements =
            roundEnvironment.getElementsAnnotatedWith(ShowkaseRoot::class.java)
        showkaseValidator.validateShowkaseRootElement(showkaseRootElements, elementUtils, typeUtils)
        val rootElement = showkaseRootElements.singleOrNull()
        when (rootElement) {
            // If root element is not present in this module, it means that we only need to write
            // the metadata file for this module so that the root module can use this info to 
            // include the composables from this module into the final codegen file. 
            null -> writeMetadataFile(componentMetadata + colorMetadata + typographyMetadata)
            // Else, this is the module that should aggregate all the other metadata files and 
            // also use the showkaseMetadata set from the current round to write the final file.
            else -> {
                writeShowkaseFiles(rootElement, componentMetadata, colorMetadata, typographyMetadata)
            }
        }
    }

    private fun writeShowkaseFiles(
        rootElement: Element,
        componentMetadata: Set<ShowkaseMetadata>,
        colorMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        val generatedShowkaseMetadataOnClasspath =
            getShowkaseCodegenMetadataOnClassPath(elementUtils)
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
        typographyMetadata + classpathTypographyMetadata)
        
    }

    private fun getShowkaseCodegenMetadataOnClassPath(elementUtils: Elements): Set<ShowkaseMetadata> {
        val showkaseGeneratedPackageElement = elementUtils.getPackageElement(CODEGEN_PACKAGE_NAME)
        return showkaseGeneratedPackageElement.enclosedElements
            .flatMap { it.enclosedElements }
            .mapNotNull { element ->
                val codegenMetadataAnnotation =
                    element.getAnnotation(ShowkaseCodegenMetadata::class.java)
                when {
                    codegenMetadataAnnotation == null -> null
                    else -> element to codegenMetadataAnnotation
                }
            }
            .map { it.second.toModel(it.first) }
            .toSet()
    }

    private fun writeShowkaseBrowserFiles(
        rootElement: Element,
        componentsMetadata: Set<ShowkaseMetadata>,
        colorsMetadata: Set<ShowkaseMetadata>,
        typographyMetadata: Set<ShowkaseMetadata>
    ) {
        if (componentsMetadata.isEmpty() && colorsMetadata.isEmpty() && typographyMetadata.isEmpty()) return
        val rootModuleClassName = rootElement.simpleName.toString()
        val rootModulePackageName = elementUtils.getPackageOf(rootElement).qualifiedName.toString()

        ShowkaseBrowserWriter(processingEnv).apply {
            generateShowkaseBrowserFile(
                componentsMetadata, 
                colorsMetadata, 
                typographyMetadata,
                rootModulePackageName, 
                rootModuleClassName
            )
        }

        ShowkaseBrowserIntentWriter(processingEnv).apply {
            generateIntentFile(
                rootModulePackageName = rootModulePackageName,
                rootModuleClassName = rootModuleClassName,
                rootElement = rootElement
            )
        }
    }

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.runtime.Composable"
        const val PREVIEW_CLASS_NAME = "androidx.ui.tooling.preview.Preview"
        const val TYPE_STYLE_CLASS_NAME = "androidx.compose.ui.text.TextStyle"

        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.airbnb.android.showkase"
    }
}

