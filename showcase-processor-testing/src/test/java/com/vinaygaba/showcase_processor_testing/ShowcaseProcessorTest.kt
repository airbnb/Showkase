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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Make sure that the @Composable functions that you annotate with the " +
                "@Showcase annotation do not take in any parameters"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `multiple classes with showcaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
        import com.vinaygaba.showcase.annotation.models.ShowcaseRootModule
        
        @Showcase("group", "name")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowcaseRoot
        class RootModule1: ShowcaseRootModule
        
        @ShowcaseRoot
        class RootModule2: ShowcaseRootModule
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only one class in the root module can be annotated with @ShowcaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `method with showcaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
        import com.vinaygaba.showcase.annotation.models.ShowcaseRootModule
        
        @Showcase("group", "name")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowcaseRoot
        fun testMethod() {
        
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only classes can be annotated with @ShowcaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `class with no interface but showcaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
        import com.vinaygaba.showcase.annotation.models.ShowcaseRootModule
        
        @Showcase("group", "name")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowcaseRoot
        class TestClass() {
        
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only an implementation of ShowcaseRootModule can be annotated @ShowcaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `top level composable function with showcase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        @Composable
        fun TestComposable() {
            
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach { 
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
                
                class ShowcaseMetadataShowcase_processor_testing {
                  @ShowcaseCodegenMetadata(
                    showcaseComposableName = "group",
                    showcaseComposableGroup = "name",
                    showcaseComposableWidthDp = -1,
                    showcaseComposableHeightDp = -1,
                    packageName = "com.vinaygaba.showcase_processor_testing",
                    moduleName = "showcase_processor_testing",
                    composableMethodName = "TestComposable"
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with showcase and showcaseroot generates 2 files`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        @Showcase("group", "name")
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowcaseRootSource = SourceFile.kotlin("TestShowcaseRoot.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        import com.vinaygaba.showcase.annotation.models.ShowcaseRoot
        import com.vinaygaba.showcase.annotation.models.ShowcaseRootModule
        
        @ShowcaseRoot
        class TestShowcaseRoot: ShowcaseRootModule {
        
        }
    """
        )
        
        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowcaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find { 
            it.name ==  "ShowcaseMetadataShowcase_processor_testing.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowcaseCodegenComponents.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find { 
            it.name ==  "ShowcaseMetadataShowcase_processor_testing.kt" 
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
                
                class ShowcaseMetadataShowcase_processor_testing {
                  @ShowcaseCodegenMetadata(
                    showcaseComposableName = "group",
                    showcaseComposableGroup = "name",
                    showcaseComposableWidthDp = -1,
                    showcaseComposableHeightDp = -1,
                    packageName = "com.vinaygaba.showcase_processor_testing",
                    moduleName = "showcase_processor_testing",
                    composableMethodName = "TestComposable"
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowcaseCodegenComponents.kt"
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import androidx.compose.Composable
                import com.vinaygaba.showcase.annotation.models.ShowcaseComponents
                import com.vinaygaba.showcase.models.ShowcaseBrowserComponent
                import com.vinaygaba.showcase.models.ShowcaseComponentsProvider
                import com.vinaygaba.showcase_processor_testing.TestComposable
                import kotlin.collections.List
                
                @ShowcaseComponents
                class ShowcaseCodegenComponents : ShowcaseComponentsProvider {
                  val componentList: List<ShowcaseBrowserComponent> = listOf<ShowcaseBrowserComponent>(
                
                        ShowcaseBrowserComponent("name", "group", -1, -1,
                            @Composable { TestComposable() })
                        )
                
                    override fun getShowcaseComponents() = componentList
                  }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `wrapped composable function with showcase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.vinaygaba.showcase_processor_testing
        
        import com.vinaygaba.showcase.annotation.models.Showcase
        import androidx.compose.Composable
        
        class WrapperClass {
            @Showcase("group", "name")
            @Composable
            fun TestComposable() {
            
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
                import com.vinaygaba.showcase_processor_testing.WrapperClass
                
                class ShowcaseMetadataShowcase_processor_testing {
                  @ShowcaseCodegenMetadata(
                    showcaseComposableName = "group",
                    showcaseComposableGroup = "name",
                    showcaseComposableWidthDp = -1,
                    showcaseComposableHeightDp = -1,
                    packageName = "com.vinaygaba.showcase_processor_testing",
                    moduleName = "showcase_processor_testing",
                    composableMethodName = "TestComposable",
                    enclosingClass = [WrapperClass::class]
                  )
                  fun WrapperClass_TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `multiple composable functions with showcase annotations generates only metadata file`() {
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
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.vinaygaba.showcase
                
                import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata
                
                class ShowcaseMetadataShowcase_processor_testing {
                  @ShowcaseCodegenMetadata(
                    showcaseComposableName = "group1",
                    showcaseComposableGroup = "name1",
                    showcaseComposableWidthDp = -1,
                    showcaseComposableHeightDp = -1,
                    packageName = "com.vinaygaba.showcase_processor_testing",
                    moduleName = "showcase_processor_testing",
                    composableMethodName = "TestComposable1"
                  )
                  fun TestComposable1() {
                  }
                
                  @ShowcaseCodegenMetadata(
                    showcaseComposableName = "group1",
                    showcaseComposableGroup = "name2",
                    showcaseComposableWidthDp = -1,
                    showcaseComposableHeightDp = -1,
                    packageName = "com.vinaygaba.showcase_processor_testing",
                    moduleName = "showcase_processor_testing",
                    composableMethodName = "TestComposable2"
                  )
                  fun TestComposable2() {
                  }
                }
            """.trimIndent()
            )
        }
    }
    
    

    private fun compileKotlinSource(kotlinSourceFiles: List<SourceFile>): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            sources = kotlinSourceFiles
            annotationProcessors = listOf(ShowcaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    }
}
