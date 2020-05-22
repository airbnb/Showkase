package com.vinaygaba.codegen.processors

import com.google.auto.service.AutoService
import com.vinaygaba.annotation.Showcase
import com.vinaygaba.codegen.logging.Logger
import com.vinaygaba.annotation.models.ShowcaseMetadata
import com.vinaygaba.codegen.writer.KotlinComposableWriter
import java.lang.Exception
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
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

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(KotlinComposableWriter.KAPT_KOTLIN_DIR_PATH)
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        val list = mutableListOf<ShowcaseMetadata>()
        p1?.getElementsAnnotatedWith(Showcase::class.java)?.forEach { element ->
            // TODO(vinaygaba) Also add check to ensure that it's a @Composable method with no 
            //  parameters passed to it
            // Throw error if this annotation is added to something that is not a method.
            if (element.kind != ElementKind.METHOD) {
                logger.logMessage("Only composable methods can be annotated " +
                        "with ${Showcase::class.java.simpleName}")
            }

            try {
                val showcaseMetadata = ShowcaseMetadata.getShowcaseMetadata(
                    element = element, elementUtil = elementUtils!!, typeUtils = typeUtils!!
                )
                list += showcaseMetadata
                // TODO(vinaygaba) Remove suppress and replace with appropriate exception type
            } catch (@Suppress("TooGenericExceptionCaught")exception: Exception) {
                logger
                    .logMessage("Error encountered ${exception.message}")
            }
        }

        KotlinComposableWriter(processingEnv).generateShowcaseBrowserComponents(list)

        if (p1?.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return true
    }
}

