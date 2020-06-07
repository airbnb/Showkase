package com.vinaygaba.showcase_processor_testing

import com.google.common.truth.Truth.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.vinaygaba.showcase.processor.ShowcaseProcessor
import org.junit.Test

class ShowcaseProcessorTest {
    @Test
    fun `composable with showcase annotation inside class compiles ok`() {
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

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    }

    @Test
    fun `top level composable function with showcase annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        @Composable
        fun TestComposable() {
            
        }
    """)
        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(ShowcaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    }

    @Test
    fun `private composable with showcase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        class GeneratedTestComposables {
            @Showcase("group", "name")
            @Composable
            private fun TestComposable() {
                
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
        val error = "The methods annotated with Showcase can't be private as the library won't be " +
                "able to access them otherwise."
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `basic function annotated with showcase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        
        class GeneratedTestComposables {
            @Showcase("group", "name")
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
        val error = "Only composable methods can be annotated with Showcase"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `class annotated with showcase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        class GeneratedTestComposables {
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
        val error = "Only composable methods can be annotated with Showcase"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `object annotated with showcase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        object GeneratedTestComposables {
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
        val error = "Only composable methods can be annotated with Showcase"
        assertThat(result.messages.contains(error))
    }
    
    @Test
    fun `composable function that has parameters with showcase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
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
        val error = "Make sure that the @Composable functions that you annotate with the " +
                "@Showcase annotation do not take in any parameters"
        assertThat(result.messages.contains(error))
    }
}
