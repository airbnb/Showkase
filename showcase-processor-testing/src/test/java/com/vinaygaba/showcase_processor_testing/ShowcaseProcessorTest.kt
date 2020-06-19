package com.vinaygaba.showcase_processor_testing

import org.assertj.core.api.Assertions.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.vinaygaba.showcase.processor.ShowcaseProcessor
import org.junit.Test

class ShowcaseProcessorTest {
    @Test
    fun `composable with showcase annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
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

    @Test
    fun `top level composable function with showcase annotation generates correct file`() {
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
        result.sourcesGeneratedByAnnotationProcessor.forEach { 
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import androidx.compose.Composable
                import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
                import com.vinaygaba.showcase.models.ShowcaseComponentsProvider
                import com.vinaygaba.showcase_processor_testing.TestComposable
                import kotlin.collections.List
                
                class ShowcaseCodegenComponents : ShowcaseComponentsProvider {
                  val componentList: List<ShowcaseCodegenMetadata> = listOf<ShowcaseCodegenMetadata>(
                
                        ShowcaseCodegenMetadata("name", "group", -1, -1,
                            @Composable { TestComposable() })
                        )

                    override fun getShowcaseComponents() = componentList
                  }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `multiple composable functions with showcase annotations generates correct file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group1", "name1")
        @Composable
        fun TestComposable1() {
            
        }
        
        @Showcase("group1", "name2")
        @Composable
        fun TestComposable2() {
            
        }
    """)
        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(ShowcaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import androidx.compose.Composable
                import com.vinaygaba.showcase.models.ShowcaseCodegenMetadata
                import com.vinaygaba.showcase.models.ShowcaseComponentsProvider
                import com.vinaygaba.showcase_processor_testing.TestComposable1
                import com.vinaygaba.showcase_processor_testing.TestComposable2
                import kotlin.collections.List
                
                class ShowcaseCodegenComponents : ShowcaseComponentsProvider {
                  val componentList: List<ShowcaseCodegenMetadata> = listOf<ShowcaseCodegenMetadata>(
                
                        ShowcaseCodegenMetadata("name1", "group1", -1, -1,
                            @Composable { TestComposable1() }),
                        ShowcaseCodegenMetadata("name2", "group1", -1, -1,
                            @Composable { TestComposable2() })
                        )

                    override fun getShowcaseComponents() = componentList
                  }
            """.trimIndent()
            )
        }
    }
}
