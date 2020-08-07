package com.airbnb.showkase.processor

import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.annotation.models.ShowkaseRoot
import com.airbnb.showkase.processor.ShowkaseProcessor.Companion.KAPT_KOTLIN_DIR_PATH
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.showkase.processor.logging.ShowkaseValidator
import com.airbnb.showkase.processor.models.ShowkaseMetadata
import com.airbnb.showkase.processor.models.getShowkaseMetadata
import com.airbnb.showkase.processor.models.getShowkaseMetadataFromPreview
import com.airbnb.showkase.processor.models.toModel
import com.airbnb.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.airbnb.showkase.processor.writer.ShowkaseComponentsWriter
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

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filter = processingEnv.filer
        messager = processingEnv.messager
        composableTypeMirror = elementUtils
            .getTypeElement(Class.forName(COMPOSABLE_CLASS_NAME).canonicalName)
            .asType()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
        Showkase::class.java.name,
        ShowkaseRoot::class.java.name,
        PREVIEW_CLASS_NAME
    )

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        try {
            val showkaseComposablesMetadata = processShowkaseAnnotation(roundEnvironment)
            val previewComposablesMetadata = processPreviewAnnotation(roundEnvironment)
            val uniqueComposablesMetadata = dedupePreviewAndShowkaseAnnotationComponents(
                showkaseComposablesMetadata,
                previewComposablesMetadata
            )
            processShowkaseMetadata(roundEnvironment, uniqueComposablesMetadata)
        } catch (exception: ShowkaseProcessorException) {
            logger.logErrorMessage("${exception.message}")
        }

        if (roundEnvironment.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return false
    }

    private fun processShowkaseMetadata(
        roundEnvironment: RoundEnvironment,
        uniqueComposablesMetadata: Set<ShowkaseMetadata>
    ) {
        val showkaseRootElements =
            roundEnvironment.getElementsAnnotatedWith(ShowkaseRoot::class.java)
        val rootElement = showkaseRootElements?.singleOrNull()
        when (rootElement) {
            // If root element is not present in this module, it means that we only need to write
            // the metadata file for this module so that the root module can use this info to 
            // include the composables from this module into the final codegen file. 
            null -> writeMetadataFile(uniqueComposablesMetadata)
            // Else, this is the module that should aggregate all the other metadata files and 
            // also use the showkaseMetadata set from the current round to write the final file.
            else -> {
                showkaseValidator.validateShowkaseRootElement(
                    showkaseRootElements, elementUtils,
                    typeUtils
                )
                val generatedShowkaseMetadataOnClasspath =
                    getShowkaseCodegenMetadataOnClassPath(elementUtils)
                val combinedShowkaseMetadata = uniqueComposablesMetadata +
                        generatedShowkaseMetadataOnClasspath
                processMetadata(rootElement, combinedShowkaseMetadata)
            }
        }
    }

    private fun processShowkaseAnnotation(roundEnvironment: RoundEnvironment) =
        roundEnvironment.getElementsAnnotatedWith(Showkase::class.java).map { element ->
            showkaseValidator.validateElement(
                element, composableTypeMirror, typeUtils,
                Showkase::class.java.simpleName
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
            showkaseValidator.validateElement(element, composableTypeMirror, typeUtils, 
                previewClass.simpleName)
            val showkaseMetadata = getShowkaseMetadataFromPreview(
                element as ExecutableElement, elementUtils, typeUtils, previewTypeMirror, showkaseValidator
            )
            showkaseMetadata
        }.toSet()
    }

    private fun dedupePreviewAndShowkaseAnnotationComponents(
        showcaseMetadataList: Set<ShowkaseMetadata>,
        previewMetadataList: Set<ShowkaseMetadata>
    ) = (showcaseMetadataList + previewMetadataList)
        .dedupeAndSort()
        .toSet()

    private fun writeMetadataFile(uniqueComposablesMetadata: Set<ShowkaseMetadata>) {
        ShowkaseCodegenMetadataWriter(processingEnv).apply {
            generateShowkaseCodegenFunctions(uniqueComposablesMetadata, typeUtils)
        }
    }

    private fun Collection<ShowkaseMetadata>.dedupeAndSort() = this.distinctBy {
        // It's possible that a composable annotation is annotated with both Preview & 
        // Showkase(especially if we add more functionality to Showkase and they diverge in 
        // the customizations that they offer). In that scenario, its important to dedupe the
        // composables as they will be processed across both the rounds. We first ensure that
        // only distict method's are passed onto the next round. We do this by deduping on 
        // the combination of packageName, the wrapper class when available(otherwise it 
        // will be null) & the methodName.
        "${it.packageName}_${it.enclosingClass}_${it.methodName}"
    }
        .distinctBy {
            // We also ensure that the component groupName and the component name are unique so 
            // that they don't show up twice in the browser app. 
            "${it.showkaseComponentName}_${it.showkaseComponentGroup}"
        }
        .sortedBy {
            "${it.packageName}_${it.enclosingClass}_${it.methodName}"
        }

    private fun processMetadata(
        rootElement: Element,
        combinedMetadataList: Set<ShowkaseMetadata>
    ) {
        if (combinedMetadataList.isEmpty()) return
        val rootModuleClassName = rootElement.simpleName.toString()
        val rootModulePackageName = elementUtils.getPackageOf(rootElement).qualifiedName.toString()

        ShowkaseComponentsWriter(processingEnv).apply {
            generateShowkaseBrowserComponents(
                combinedMetadataList.toList(), rootModulePackageName, rootModuleClassName
            )
        }
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

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.Composable"
        const val PREVIEW_CLASS_NAME = "androidx.ui.tooling.preview.Preview"

        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.airbnb.showkase"
    }
}

