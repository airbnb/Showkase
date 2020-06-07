package com.vinaygaba.showcase_processor_testing

import com.google.common.truth.Truth.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.vinaygaba.showcase.processor.ShowcaseProcessor
import org.junit.Test

class ShowcaseProcessorTest {
    @Test
    fun `basic processor test`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        class Composables {
            @Showcase("group", "name")
            @Composable
            fun TestComposable() {
                
            }
        }
    """)
        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(ShowcaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
    }
}
