package com.vinaygaba.showcase.processor

import com.google.auto.service.AutoService
import com.vinaygaba.showcase.annotation.models.Showcase
import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
import com.vinaygaba.showcase.processor.ShowcaseProcessor.Companion.KAPT_KOTLIN_DIR_PATH
import com.vinaygaba.showcase.processor.logging.ShowcaseExceptionLogger
import com.vinaygaba.showcase.processor.models.ShowcaseMetadata
import com.vinaygaba.showcase.processor.exceptions.ShowcaseProcessorException
import com.vinaygaba.showcase.processor.logging.ShowcaseValidator
import com.vinaygaba.showcase.processor.models.getShowcaseMetadata
import com.vinaygaba.showcase.processor.models.toModel
import com.vinaygaba.showcase.processor.writer.ShowcaseCodegenMetadataWriter
import com.vinaygaba.showcase.processor.writer.ShowcaseComponentsWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(KAPT_KOTLIN_DIR_PATH)
class ShowcaseProcessor: AbstractProcessor() {
    private lateinit var  typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filter: Filer
    private lateinit var messager: Messager
    private val logger = ShowcaseExceptionLogger()
    private val showcaseValidator = ShowcaseValidator()
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

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Showcase::class.java.name, ShowcaseRoot::class.java.name)
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(KAPT_KOTLIN_DIR_PATH)
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        try {
            val showcaseMetadataList = processShowcaseAnnotation(p1)
            
            processShowcaseMetadata(showcaseMetadataList, p1)
        } catch (exception: ShowcaseProcessorException) {
            logger.logErrorMessage("${exception.message}")
        }
        

        if (p1?.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return true
    }
    
    private fun processShowcaseAnnotation(p1: RoundEnvironment?): List<ShowcaseMetadata> {
        val showcaseMetadataList = mutableListOf<ShowcaseMetadata>()
        p1?.getElementsAnnotatedWith(Showcase::class.java)?.forEach { element ->
            showcaseValidator.validateElement(element, composableTypeMirror, typeUtils)
            val showcaseMetadata = getShowcaseMetadata(
                element = element, elementUtil = elementUtils)
            showcaseMetadataList += showcaseMetadata
        }

        ShowcaseCodegenMetadataWriter(processingEnv).apply {
            generateShowcaseCodegenFunctions(showcaseMetadataList, typeUtils)
        }
        return showcaseMetadataList
    }
    
    private fun processShowcaseMetadata(
        currentShowcaseMetadataList: List<ShowcaseMetadata>,
        p1: RoundEnvironment?
    ) {
        if (currentShowcaseMetadataList.isEmpty()) return
        val showcaseRootElements = p1?.getElementsAnnotatedWith(ShowcaseRoot::class.java) ?: setOf()
        
        showcaseRootElements.forEach {
            showcaseValidator.validateShowcaseRootElement(showcaseRootElements, elementUtils, typeUtils)
            val generatedShowcaseMetadataOnClasspath = getShowcaseCodegenMetadataOnClassPath(elementUtils)
            val allShowcaseMetadataList = generatedShowcaseMetadataOnClasspath
                .plus(currentShowcaseMetadataList)

            ShowcaseComponentsWriter(processingEnv).apply {
                generateShowcaseBrowserComponents(allShowcaseMetadataList)
            }
        }
    }
    
    private fun getShowcaseCodegenMetadataOnClassPath(elementUtils: Elements): List<ShowcaseMetadata> {
        val showcaseGeneratedPackageElement = elementUtils.getPackageElement(CODEGEN_PACKAGE_NAME)
        return showcaseGeneratedPackageElement.enclosedElements
            .flatMap { it.enclosedElements }
            .mapNotNull { element -> element.getAnnotation(ShowcaseCodegenMetadata::class.java) }
            .map {
                it.toModel()
            }
    }

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.Composable"
        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.vinaygaba.showcase"
    }
}

