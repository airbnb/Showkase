package com.airbnb.showkase_processor_testing

import org.assertj.core.api.Assertions.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.airbnb.showkase.processor.ShowkaseProcessor
import org.junit.Test

class ShowkaseProcessorTest {
    @Test
    fun `composable with showkase annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        class Composables {
            @Showkase("name", "group")
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
    fun `composable with preview annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        class Composables {
            @Preview("name", "group")
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
    fun `top level composable function with showkase annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `top level composable function with preview annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `private composable with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        class GeneratedTestComposables {
            @Showkase("name", "group")
            @Composable
            private fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "The methods annotated with Showkase can't be private as the library won't be " +
                "able to access them otherwise."
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `private composable with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.compose.Composable
        
        class GeneratedTestComposables {
            @Preview("name", "group")
            @Composable
            private fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "The methods annotated with Preview can't be private as the library won't be " +
                "able to access them otherwise."
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `basic function annotated with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        
        class GeneratedTestComposables {
            @Showkase("name", "group")
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Showkase"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `basic function annotated with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        
        class GeneratedTestComposables {
            @Preview("name", "group")
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Preview"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `class annotated with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
        class GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Showkase"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `class annotated with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
        class GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Preview"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `object annotated with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
        object GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Showkase"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `object annotated with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
        object GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with Preview"
        assertThat(result.messages.contains(error))
    }
    
    @Test
    fun `composable function that has parameters with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Make sure that the @Composable functions that you annotate with the " +
                "@Showkase annotation do not take in any parameters"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `composable function that has parameters with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Make sure that the @Composable functions that you annotate with the " +
                "@Showkase annotation do not take in any parameters"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `multiple classes with showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.ShowkaseRoot
        import com.airbnb.showkase.annotation.models.ShowkaseRootModule
        
        @Showkase("name", "group")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowkaseRoot
        class RootModule1: ShowkaseRootModule
        
        @ShowkaseRoot
        class RootModule2: ShowkaseRootModule
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only one class in a module can be annotated with @ShowkaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `method with showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.ShowkaseRoot
        import com.airbnb.showkase.annotation.models.ShowkaseRootModule
        
        @Showkase("name", "group")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowkaseRoot
        fun testMethod() {
        
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only classes can be annotated with @ShowkaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `class with no interface but showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.ShowkaseRoot
        import com.airbnb.showkase.annotation.models.ShowkaseRootModule
        
        @Showkase("name", "group")
        class GeneratedTestComposables {
            fun TestComposable(name: String, age: Int) {
                
            }
        }
        
        @ShowkaseRoot
        class TestClass() {
        
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only an implementation of ShowkaseRootModule can be annotated @ShowkaseRoot"
        assertThat(result.messages.contains(error))
    }

    @Test
    fun `top level composable function with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with showkase and showkaseroot generates 2 files`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.ShowkaseRoot
        import com.airbnb.showkase.annotation.models.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )
        
        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find { 
            it.name ==  "ShowkaseMetadataShowkase_processor_testing.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegenComponents.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find { 
            it.name ==  "ShowkaseMetadataShowkase_processor_testing.kt" 
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowkaseCodegenComponents.kt"
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import androidx.compose.Composable
                import com.airbnb.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.showkase.models.ShowkaseComponentsProvider
                import com.airbnb.showkase_processor_testing.TestComposable
                import kotlin.collections.List
                
                class ShowkaseCodegenComponents : ShowkaseComponentsProvider {
                  val componentList: List<ShowkaseBrowserComponent> = listOf<ShowkaseBrowserComponent>(
                
                        ShowkaseBrowserComponent("group", "name", -1, -1,
                            @Composable { TestComposable() })
                        )
                
                    override fun getShowkaseComponents() = componentList
                  }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview and showkaseroot generates 2 files`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.ShowkaseRoot
        import com.airbnb.showkase.annotation.models.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowkaseMetadataShowkase_processor_testing.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegenComponents.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowkaseMetadataShowkase_processor_testing.kt"
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "ShowkaseCodegenComponents.kt"
        }?.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import androidx.compose.Composable
                import com.airbnb.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.showkase.models.ShowkaseComponentsProvider
                import com.airbnb.showkase_processor_testing.TestComposable
                import kotlin.collections.List
                
                class ShowkaseCodegenComponents : ShowkaseComponentsProvider {
                  val componentList: List<ShowkaseBrowserComponent> = listOf<ShowkaseBrowserComponent>(
                
                        ShowkaseBrowserComponent("group", "name", -1, -1,
                            @Composable { TestComposable() })
                        )
                
                    override fun getShowkaseComponents() = componentList
                  }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `wrapped composable function with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        class WrapperClass {
            @Showkase("name", "group")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                import com.airbnb.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = true,
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
    fun `wrapped composable function with preview annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        class WrapperClass {
            @Preview("name", "group")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                import com.airbnb.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name",
                    showkaseComposableGroup = "group",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = true,
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
    fun `multiple composable functions with showkase annotations generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import com.airbnb.showkase.annotation.models.Showkase
        import androidx.compose.Composable
        
        @Showkase("name1", "group1")
        @Composable
        fun TestComposable1() {
            
        }
        
        @Showkase("name2", "group1")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name1",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable1() {
                  }
                
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name2",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable2() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `multiple composable functions with preview annotations generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        
        @Preview("name1", "group1")
        @Composable
        fun TestComposable1() {
            
        }
        
        @Preview("name2", "group1")
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
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name1",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable1() {
                  }
                
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name2",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable2() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `multiple composable functions with preview and showkase annotations generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.Showkase
        
        @Preview("name1", "group1")
        @Composable
        fun TestComposable1() {
            
        }
        
        @Showkase("name2", "group1")
        @Composable
        fun TestComposable2() {
            
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name2",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable2() {
                  }

                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name1",
                    showkaseComposableGroup = "group1",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable1() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function with both annotations gives priority to showkase annotation` () {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.Composable
        import com.airbnb.showkase.annotation.models.Showkase
        
        @Preview("name1", "group1")
        @Showkase("name2", "group2")
        @Composable
        fun TestComposable1() {
            
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.showkase
                
                import com.airbnb.showkase.annotation.models.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseComposableName = "name2",
                    showkaseComposableGroup = "group2",
                    packageName = "com.airbnb.showkase_processor_testing",
                    moduleName = "showkase_processor_testing",
                    composableMethodName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false
                  )
                  fun TestComposable1() {
                  }
                }
            """.trimIndent()
            )
        }
    }
    
    private fun compileKotlinSource(kotlinSourceFiles: List<SourceFile>): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            sources = kotlinSourceFiles
            annotationProcessors = listOf(ShowkaseProcessor())
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    }
}
