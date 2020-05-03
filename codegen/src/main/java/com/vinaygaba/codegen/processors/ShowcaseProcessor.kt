package com.vinaygaba.codegen.processors

import com.google.auto.service.AutoService
import com.vinaygaba.annotation.Showcase
import com.vinaygaba.codegen.logging.Logger
import com.vinaygaba.annotation.models.ShowcaseMetadata
import com.vinaygaba.codegen.writer.KotlinComposableWriter
import java.lang.Exception
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(KotlinComposableWriter.KAPT_KOTLIN_DIR_PATH)
class ShowcaseProcessor: AbstractProcessor() {
    private var typeUtils: Types? = null
    private var elementUtils: Elements? = null
    private var filter: Filer? = null
    private var messager: Messager? = null
    private val logger = Logger()
//    private val kotlinComposableWriter = KotlinComposableWriter(processingEnv)

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
    
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        val map = mutableMapOf<ExecutableElement, ShowcaseMetadata>()
        p1?.getElementsAnnotatedWith(Showcase::class.java)?.forEach { element ->
            // TODO(vinaygaba) Also add check to ensure that it's a @Composable method with no 
            //  parameters passed to it
            // Throw error if this annotation is added to something that is not a method.
            if (element.kind != ElementKind.METHOD) {
                logger.logMessage("Only composable methods can be annotated with ${Showcase::class.java.simpleName}")
            }

            try {
                val showcaseMetadata = ShowcaseMetadata.getShowcaseMetadata(element = element, elementUtil = elementUtils!!,
                    typeUtils = typeUtils!!
                )
                map[showcaseMetadata.methodElement] = showcaseMetadata
            } catch (exception: Exception) {
                logger.logMessage("Only composable methods can be annotated with ${Showcase::class.java.simpleName}")
            }
        }
        
        KotlinComposableWriter(processingEnv).generateShowcaseBrowserComponents(map)

        if (p1?.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return true
    }
}

