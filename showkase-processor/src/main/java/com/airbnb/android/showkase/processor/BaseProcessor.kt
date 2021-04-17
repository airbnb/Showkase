package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

/**
 * Creates a unified abstraction for processors of both KSP and java annotation processing.
 */
abstract class BaseProcessor(vararg val supportedAnnotations: KClass<out Annotation>) :
    AbstractProcessor(),
    SymbolProcessor {

    lateinit var environment: XProcessingEnv
        private set

    lateinit var options: Map<String, String>
        private set

    private lateinit var codeGenerator: CodeGenerator
    private lateinit var logger: KSPLogger

    override fun getSupportedAnnotationTypes(): Set<String> {
        return supportedAnnotations.map { it.qualifiedName!! }.toSet()
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_8

    final override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.options = options
        this.codeGenerator = codeGenerator
        this.logger = logger
    }

    final override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        environment = XProcessingEnv.create(processingEnv)
    }

    final override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.errorRaised()) {
            onError()
        }

        process(environment, XRoundEnv.create(environment, roundEnv))

        if (roundEnv.processingOver()) {
            finish()
        }

        return false
    }

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        environment = XProcessingEnv.create(options, resolver, codeGenerator, logger)
        process(environment, XRoundEnv.create(environment))
        return emptyList()
    }

    abstract fun process(
        environment: XProcessingEnv,
        round: XRoundEnv
    )
}