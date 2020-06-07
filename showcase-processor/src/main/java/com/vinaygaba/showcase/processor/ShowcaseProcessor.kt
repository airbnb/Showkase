package com.vinaygaba.showcase.processor

import com.google.auto.service.AutoService
import com.vinaygaba.showcase.annotation.models.Showcase
import com.vinaygaba.showcase.processor.logging.ShowcaseExceptionLogger
import com.vinaygaba.showcase.processor.models.ShowcaseMetadata
import com.vinaygaba.showcase.processor.exceptions.ShowcaseProcessorException
import com.vinaygaba.showcase.processor.logging.ShowcaseValidator
import com.vinaygaba.showcase.processor.writer.KotlinComposableWriter
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
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
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
    private val logger = ShowcaseExceptionLogger()
    private val showcaseValidator = ShowcaseValidator()
    private var composableKind: TypeKind? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        typeUtils = processingEnv?.typeUtils
        elementUtils = processingEnv?.elementUtils
        filter = processingEnv?.filer
        messager = processingEnv?.messager
        composableKind = elementUtils
            ?.getTypeElement(Class.forName("androidx.compose.Composable").canonicalName)
            ?.asType()
            ?.kind!!
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
            // Throw error if this annotation is added to something that is not a method or if the 
            // method annotated with the showcase annotation isn't a @Composable function.
            if (!showcaseValidator.validateElement(element, logger, composableKind)) {
                return@forEach
            }

            try {
                val showcaseMetadata =
                    getShowcaseMetadata(
                        element = element, elementUtil = elementUtils!!, typeUtils = typeUtils!!
                    )
                list += showcaseMetadata
            } catch (exception: ShowcaseProcessorException) {
                logger.logMessage("Error encountered: ${exception.message}")
            }
        }

        KotlinComposableWriter(processingEnv).generateShowcaseBrowserComponents(list)

        if (p1?.processingOver() == true) {
            logger.publishMessages(messager)
        }
        return true
    }

    companion object {
        private fun getShowcaseMetadata(
            element: Element,
            elementUtil: Elements,
            typeUtils: Types
        ): ShowcaseMetadata {
            val executableElement = element as ExecutableElement
            val enclosingElement = element.enclosingElement
            val isStaticMethod = executableElement.modifiers.contains(Modifier.STATIC)
            val showcaseAnnotation = executableElement.getAnnotation(Showcase::class.java)

            val noOfParameters = executableElement.parameters.size
            if (noOfParameters > 0) {
                throw ShowcaseProcessorException(
                    "Make sure that the @Composable functions that you " +
                            "annotate with the @Showcase annotation do not take in any parameters"
                )
            }

            return ShowcaseMetadata(
                executableElement,
                executableElement.simpleName.toString(),
                // If isStaticMethod is true, it means the method was declared at the top level. 
                // If not, it was declared inside a class
                // TODO(vinaygaba): Add support for methods inside companion objects and 
                // objects
                if (isStaticMethod) null else enclosingElement.asType(),
                element.enclosingElement.enclosingElement.asType().toString(),
                showcaseAnnotation.name,
                showcaseAnnotation.group,
                showcaseAnnotation.widthDp,
                showcaseAnnotation.heightDp
            )
        }
    }
}

