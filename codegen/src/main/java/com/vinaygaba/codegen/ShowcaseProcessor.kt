package com.vinaygaba.codegen

import com.google.auto.service.AutoService
import com.vinaygaba.annotation.Showcase
import com.vinaygaba.annotation.exceptions.ShowcaseProcessorException
import com.vinaygaba.annotation.logging.Logger
import com.vinaygaba.annotation.models.ShowcaseMetadata
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8DDD
//@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ShowcaseProcessor: AbstractProcessor() {
    private var typeUtils: Types? = null
    private var elementUtils: Elements? = null
    private var filter: Filer? = null
    private var messager: Messager? = null
    private val logger = Logger()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        typeUtils = processingEnv?.typeUtils
        elementUtils = processingEnv?.elementUtils
        filter = processingEnv?.filer
        messager = processingEnv?.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Showcase::class.java.name)
    }
    
    override fun process(p0: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        
        val map = mapOf<TypeMirror, ShowcaseMetadata>()
        roundEnvironment?.getElementsAnnotatedWith(Showcase::class.java)?.forEach { element ->
            // TODO(vinaygaba) Also add check to ensure that it's a @Composable method with no 
            //  parameters passed to it
            // Throw error if this annotation is added to something that is not a method.
            if (element.kind != ElementKind.METHOD) {
                logger.logMessage("Only composable methods can be annotated with ${Showcase::class.java.simpleName}")
            }
            
            try {
                val showcaseMetadata = ShowcaseMetadata.getShowcaseMetadata(element = element)
            } catch (exception: ShowcaseProcessorException) {
                logger.logMessage("Only composable methods can be annotated with ${Showcase::class.java.simpleName}")
            }
            
            
        }
        return true
    }
}

