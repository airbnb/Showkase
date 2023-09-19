package com.airbnb.android.showkase.processor

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XMessager
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.XRoundEnv
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * Creates a unified abstraction for processors of both KSP and java annotation processing.
 */
abstract class BaseProcessor(
    val kspEnvironment: SymbolProcessorEnvironment? = null
) : AbstractProcessor(), SymbolProcessor {

    lateinit var environment: XProcessingEnv
        private set

    val messager: XMessager
        get() = environment.messager

    val filer: XFiler
        get() = environment.filer

    private var roundNumber = 1

    init {
        if (kspEnvironment != null) {
            initOptions(kspEnvironment.options)
        }
    }

    fun isKsp(): Boolean = kspEnvironment != null

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_17

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        environment = XProcessingEnv.create(processingEnv)
        initOptions(processingEnv.options)
    }

    /**
     * Unified place to handle any compiler processor options that are passed to
     * either javac processor or KSP processor,
     * before any rounds are processed.
     */
    open fun initOptions(options: Map<String, String>) {}

    final override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.errorRaised()) {
            onError()
        }

        internalProcess(environment, XRoundEnv.create(environment, roundEnv))

        if (roundEnv.processingOver()) {
            finish()
        }

        return false
    }

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        val kspEnvironment = requireNotNull(kspEnvironment)
        environment = XProcessingEnv.create(
            kspEnvironment,
            resolver,
        )
        internalProcess(environment, XRoundEnv.create(environment))
        return emptyList()
    }

    private fun internalProcess(
        environment: XProcessingEnv,
        round: XRoundEnv
    ) {
        val timer =
            Timer(
                this.javaClass.simpleName + " [Round $roundNumber][${if (isKsp()) "ksp" else "javac"}]"
            )
        timer.start()

        tryOrPrintError {
            process(environment, round)
        }

        timer.finishAndPrint(messager)
        roundNumber++
    }

    private inline fun tryOrPrintError(block: () -> Unit) {
        @Suppress("Detekt.TooGenericExceptionCaught")
        try {
            block()
        } catch (e: Throwable) {
            // Errors thrown from within KSP can get lost, making the root cause of an issue hidden.
            // This helps to surface all thrown errors.
            if (e is ShowkaseProcessorException && e.element != null) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.stackTraceToString(), e.element)
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, e.stackTraceToString())
            }
        }
    }

    abstract fun process(
        environment: XProcessingEnv,
        round: XRoundEnv
    )
}
