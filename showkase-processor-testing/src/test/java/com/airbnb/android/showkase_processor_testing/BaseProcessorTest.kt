@file:Suppress("PackageName")
package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.processor.ShowkaseProcessor
import com.airbnb.android.showkase.processor.ShowkaseProcessorProvider
import com.google.common.io.Resources
import com.tschuchort.compiletesting.CompilationResult
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Rule
import java.io.File

/**
 * Temporarily set this to true to have the test runner update test resource file expected outputs
 * instead of failing tests on mismatch. Use this to easily update expected outputs.
 */
const val UPDATE_TEST_OUTPUTS = false

abstract class BaseProcessorTest {
    @Rule
    @JvmField
    val testNameRule = TestNameRule()

    enum class Mode {
        KSP, KAPT
    }

    /**
     * Collects the files in the "input" directory of this test's resources directory
     * and compiles them with Kotlin, returning the result.
     */
    @OptIn(ExperimentalCompilerApi::class)
    protected fun compileInputs(
        modes: List<Mode> = listOf(Mode.KSP, Mode.KAPT),
        options: MutableMap<String, String> = mutableMapOf(),
        onCompilation: (mode: Mode, compilation: KotlinCompilation, result: CompilationResult) -> Unit
    ) {
        val testResourcesDir = getTestResourcesDirectory(getRootResourcesDir())

        val inputDir = File(testResourcesDir, "input")
        inputDir.mkdirs()

        modes.forEach { mode ->
            val compilation = KotlinCompilation().apply {
                kotlincArguments = kotlincArguments + "-Xexplicit-api=strict"
                sources = inputDir.listFiles()?.toList().orEmpty().map { SourceFile.fromPath(it) }
                when (mode) {
                    Mode.KSP -> {
                        symbolProcessorProviders = listOf(ShowkaseProcessorProvider())
                        kspArgs = options
                    }
                    Mode.KAPT -> {
                        annotationProcessors = listOf(ShowkaseProcessor())
                        kaptArgs = options
                    }
                }
                inheritClassPath = true
                messageOutputStream = System.out // see diagnostics in real time
            }

            val result = compilation.compile()

            onCompilation(mode, compilation, result)
        }
    }

    @OptIn(ExperimentalCompilerApi::class)
    protected fun assertCompilationFails(errorMessage: String) {
        compileInputs { _, _, result ->
            assertThat(result.exitCode)
                .isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)

            assertThat(result.messages)
                .contains(errorMessage)
        }
    }

    @OptIn(ExperimentalCompilerApi::class)
    protected fun compileInputsAndVerifyOutputs(
        modes: List<Mode> = listOf(Mode.KSP, Mode.KAPT),
        options: MutableMap<String, String> = mutableMapOf(),
    ) {
        compileInputs(modes = modes, options = options) { mode, compilation, result ->
            result.assertGeneratedSources(mode, compilation)
        }
    }

    /**
     * Collects the files in the "output" directory of this test's resources directory
     * and validates that they match the generated sources of this compilation result.
     */
    @OptIn(ExperimentalCompilerApi::class)
    protected fun CompilationResult.assertGeneratedSources(mode: Mode, compilation: KotlinCompilation) {
        assertThat(exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val testResourcesDir = getTestResourcesDirectory(getRootResourcesDir())
        val outputDir = File(testResourcesDir, "output")

        if (UPDATE_TEST_OUTPUTS) {
            outputDir.deleteRecursively()
        }
        outputDir.mkdirs()

        val generatedSources = when (mode) {
            Mode.KSP -> compilation.kspSourcesDir.walk().filter { it.isFile }.toList()
            Mode.KAPT -> (this as JvmCompilationResult).sourcesGeneratedByAnnotationProcessor
        }

        if (UPDATE_TEST_OUTPUTS) {
            generatedSources.forEach {
                it.copyTo(File(outputDir, it.name))
            }
        } else {
            assertThat(generatedSources.size)
                .isEqualTo(outputDir.listFiles()?.size ?: 0)

            generatedSources.forEach { actualFile ->
                val expectedFile = File(outputDir, actualFile.name)
                assertThat(expectedFile).exists()
                assertThat(actualFile).hasSameTextualContentAs(expectedFile)
            }
        }
    }

    private fun getRootResourcesDir(): File {
        val path = Resources.getResource("")
            .path
            .substringBefore("/build/")
            .plus("/src/test/resources")

        return File(path)
    }

    protected open fun getTestResourcesDirectory(rootResourcesDir: File): File {
        val methodName = testNameRule
            .methodName
            .replace(" ", "_")

        val className = testNameRule.className.substringAfterLast(".")
        return File(rootResourcesDir, "$className/$methodName")
    }
}
