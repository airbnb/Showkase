package com.airbnb.showkase.processor

import com.google.auto.service.AutoService
import com.airbnb.showkase.annotation.models.Showkase
import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
import com.airbnb.showkase.annotation.models.ShowkaseRoot
import com.airbnb.showkase.processor.ShowkaseProcessor.Companion.KAPT_KOTLIN_DIR_PATH
import com.airbnb.showkase.processor.logging.ShowkaseExceptionLogger
import com.airbnb.showkase.processor.models.ShowkaseMetadata
import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.showkase.processor.logging.ShowkaseValidator
import com.airbnb.showkase.processor.models.getShowkaseMetadata
import com.airbnb.showkase.processor.models.toModel
import com.airbnb.showkase.processor.writer.ShowkaseCodegenMetadataWriter
import com.airbnb.showkase.processor.writer.ShowkaseComponentsWriter
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
class ShowkaseProcessor: AbstractProcessor() {
    private lateinit var  typeUtils: Types
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

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Showkase::class.java.name, ShowkaseRoot::class.java.name)
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(KAPT_KOTLIN_DIR_PATH)
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        try {
            val showkaseMetadataList = processShowkaseAnnotation(p1)
            
            processShowkaseMetadata(showkaseMetadataList, p1)
        } catch (exception: ShowkaseProcessorException) {
            logger.logErrorMessage("${exception.message}")
        }
        

        if (p1?.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return true
    }
    
    private fun processShowkaseAnnotation(p1: RoundEnvironment?): List<ShowkaseMetadata> {
        val showkaseMetadataList = mutableListOf<ShowkaseMetadata>()
        p1?.getElementsAnnotatedWith(Showkase::class.java)?.forEach { element ->
            showkaseValidator.validateElement(element, composableTypeMirror, typeUtils)
            val showkaseMetadata = getShowkaseMetadata(
                element = element, elementUtil = elementUtils)
            showkaseMetadataList += showkaseMetadata
        }

        ShowkaseCodegenMetadataWriter(processingEnv).apply {
            generateShowkaseCodegenFunctions(showkaseMetadataList, typeUtils)
        }
        return showkaseMetadataList
    }
    
    private fun processShowkaseMetadata(
        currentShowkaseMetadataList: List<ShowkaseMetadata>,
        p1: RoundEnvironment?
    ) {
        if (currentShowkaseMetadataList.isEmpty()) return
        val showkaseRootElements = p1?.getElementsAnnotatedWith(ShowkaseRoot::class.java) ?: setOf()
        
        showkaseRootElements.forEach {
            showkaseValidator.validateShowkaseRootElement(showkaseRootElements, elementUtils, typeUtils)
            val rootModuleClassName = it.simpleName.toString()
            val rootModulePackageName = elementUtils.getPackageOf(it).qualifiedName.toString()
            val generatedShowkaseMetadataOnClasspath = getShowkaseCodegenMetadataOnClassPath(elementUtils)
            val allShowkaseMetadataList = generatedShowkaseMetadataOnClasspath
                .plus(currentShowkaseMetadataList)

            ShowkaseComponentsWriter(processingEnv).apply {
                generateShowkaseBrowserComponents(allShowkaseMetadataList, rootModulePackageName, 
                    rootModuleClassName)
            }
        }
    }
    
    private fun getShowkaseCodegenMetadataOnClassPath(elementUtils: Elements): List<ShowkaseMetadata> {
        val showkaseGeneratedPackageElement = elementUtils.getPackageElement(CODEGEN_PACKAGE_NAME)
        return showkaseGeneratedPackageElement.enclosedElements
            .flatMap { it.enclosedElements }
            .mapNotNull { element -> element.getAnnotation(ShowkaseCodegenMetadata::class.java) }
            .map {
                it.toModel()
            }
    }

    companion object {
        const val COMPOSABLE_CLASS_NAME = "androidx.compose.Composable"
        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        const val CODEGEN_PACKAGE_NAME = "com.airbnb.showkase"
    }
}

