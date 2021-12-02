package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.processor.ShowkaseProcessor
import com.google.common.io.Resources
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions
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

    /**
     * Collects the files in the "input" directory of this test's resources directory
     * and compiles them with Kotlin, returning the result.
     */
    protected fun compileInputs(): KotlinCompilation.Result {
        val testResourcesDir = getTestResourcesDirectory(getRootResourcesDir())

        val inputDir = File(testResourcesDir, "input")
        inputDir.mkdirs()

        return KotlinCompilation().apply {
            sources = inputDir.listFiles()?.toList().orEmpty().map { SourceFile.fromPath(it) }
            annotationProcessors = listOf(ShowkaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    }

    protected fun assertCompilationFails(errorMessage: String) {
        val result = compileInputs()

        Assertions.assertThat(result.exitCode)
            .isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        Assertions.assertThat(result.messages).contains(errorMessage)
    }

    protected fun compileInputsAndVerifyOutputs() {
        val result = compileInputs()
        result.assertGeneratedSources()
    }

    /**
     * Collects the files in the "output" directory of this test's resources directory
     * and validates that they match the generated sources of this compilation result.
     */
    protected fun KotlinCompilation.Result.assertGeneratedSources() {
        Assertions.assertThat(exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

        val testResourcesDir = getTestResourcesDirectory(getRootResourcesDir())
        val outputDir = File(testResourcesDir, "output")

        if (UPDATE_TEST_OUTPUTS) {
            outputDir.deleteRecursively()
        }
        outputDir.mkdirs()

        if (UPDATE_TEST_OUTPUTS) {
            sourcesGeneratedByAnnotationProcessor.forEach {
                println("Generated: ${it.name}")
                it.copyTo(File(outputDir, it.name))
            }
        } else {
            Assertions.assertThat(sourcesGeneratedByAnnotationProcessor.size)
                .isEqualTo(outputDir.listFiles()?.size ?: 0)

            sourcesGeneratedByAnnotationProcessor.forEach { actualFile ->
                println("Generated: ${actualFile.name}")
                val expectedFile = File(outputDir, actualFile.name)
                Assertions.assertThat(expectedFile).exists()
                Assertions.assertThat(actualFile).hasSameTextualContentAs(expectedFile)
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
        return File(rootResourcesDir, "$className/${methodName}")
    }
}