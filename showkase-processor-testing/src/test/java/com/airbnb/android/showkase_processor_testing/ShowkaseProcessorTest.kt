package com.airbnb.android.showkase_processor_testing

import org.assertj.core.api.Assertions.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.airbnb.android.showkase.processor.ShowkaseProcessor
import org.junit.Test

class ShowkaseProcessorTest {
    @Test
    fun `composable with showkase annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class Composables {
            @ShowkaseComposable("name", "group")
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
    fun `color with showkase color annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        
        class Composables {
            @ShowkaseColor("name", "group")
            val red = Color(0xffff0000)
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `textstyle with ShowkaseTypography annotation inside class compiles ok`() {
        val kotlinSource = SourceFile.kotlin("Composables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography

        class Composables {
            @ShowkaseTypography("name", "group")
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `top level composable function with showkase annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
    fun `top level color property with ShowkaseColor annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `composable function inside object with showkase annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        object ShowkaseObject {
            @ShowkaseComposable("name", "group")
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
    fun `composable function inside object with preview annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        object ShowkaseObject {
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
    fun `color property inside object with ShowkaseColor annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        object ShowkaseObject {
            @ShowkaseColor("name", "group")
            val color = Color(0xffff0000)
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `textstyle property inside object with ShowkaseTypography annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography

        object ShowkaseObject {
            @ShowkaseTypography("name", "group")
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `composable function inside companion object with showkase annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class ShowkaseClass {
            companion object {
                @ShowkaseComposable("name", "group")
                @Composable
                fun TestComposable() {
                    
                }
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `composable function inside companion object with preview annotation compiles ok`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        class ShowkaseClass {
            companion object {
                @Preview("name", "group")
                @Composable
                fun TestComposable() {
                    
                }
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
    }

    @Test
    fun `private composable with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class GeneratedTestComposables {
            @ShowkaseComposable("name", "group")
            @Composable
            private fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "The methods annotated with ShowkaseComposable can't be private as " +
                "Showkase won't be able to access them otherwise."
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `private composable with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.compose.runtime.Composable
        import androidx.ui.tooling.preview.Preview
        
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
        val error = "The methods annotated with Preview can't be private as Showkase won't be " +
                "able to access them otherwise."
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `basic function annotated with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        
        class GeneratedTestComposables {
            @ShowkaseComposable("name", "group")
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with ShowkaseComposable"
        assertThat(result.messages).contains(error)
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
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `class annotated with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
        class GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with ShowkaseComposable"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `class annotated with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
        object GeneratedTestComposables {
            fun TestComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only composable methods can be annotated with ShowkaseComposable"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `object annotated with preview annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
        assertThat(result.messages).contains(error)
    }
    
    @Test
    fun `composable function that has parameters with showkase annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable

        class GeneratedTestComposables {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable(name: String, age: Int) {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Make sure that the @Composable functions that you annotate with the ShowkaseComposable" +
                " annotation only have a single parameter that is annotated with @PreviewParameter."
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `composable function that has non preview parameters and preview annotation throws Exception`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable

        class GeneratedTestComposables {
            @Preview("name", "group")
            @Composable
            fun TestComposable(name: String, age: Int) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Make sure that the @Composable functions that you annotate with the Preview " +
                "annotation only have a single parameter that is annotated with @PreviewParameter."
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `composable function with showkase annotation inside class with parameters throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable

        class GeneratedTestComposables(name: String) {
            @Composable
            @ShowkaseComposable("name", "group")
            fun TestComposable() {
                
            }
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only classes that don't accept any constructor parameters can " +
        "hold a @Composable function that's annotated with the @ShowkaseComposable/@Preview " +
                "annotation"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `composable function with preview annotation inside class with parameters throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import androidx.compose.runtime.Composable
        import androidx.ui.tooling.preview.Preview

        class GeneratedTestComposables(name: String) {
            @Composable
            @Preview("name", "group")
            fun TestComposable() {
                
            }
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only classes that don't accept any constructor parameters can " +
                "hold a @Composable function that's annotated with the @ShowkaseComposable/@Preview " +
                "annotation"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `non-long value annotated with ShowkaseColor annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        class GeneratedTestComposables {
            @ShowkaseColor("name", "group")
            val red = "Hello"
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only \"Color\" fields can be annotated with ShowkaseColor"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `non-text style value annotated with ShowkaseTypography annotation throws compilation error` () {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
            
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        class GeneratedTestComposables {
            @ShowkaseTypography("name", "group")
            val red = "Hello"
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only \"TextStyle\" fields can be annotated with ShowkaseTypography"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `class annotated with ShowkaseTypography annotation throws compilation error` () {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
            
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography("name", "group")
        class GeneratedTestComposables {
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only \"TextStyle\" fields can be annotated with ShowkaseTypography"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `multiple classes with showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule

        class GeneratedTestComposables {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
                
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
        val error = "Only one class in a module can be annotated with ShowkaseRoot"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `method with showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule

        class GeneratedTestComposables {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
                
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
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `class with no interface but showkaseroot annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule

        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
        
        @ShowkaseRoot
        class TestClass() {
        
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only an implementation of ShowkaseRootModule can be annotated with " +
                "@ShowkaseRoot"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `top level composable function with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level color property with ShowkaseColor annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "red",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COLOR"
                  )
                  fun red() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable with wrapped color property with ShowkaseColor annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
        
        class WrapperClass {
            @ShowkaseColor("name", "group")
            val red = Color(0xffff0000)
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable() {
                  }

                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "red",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COLOR"
                  )
                  fun WrapperClass_red() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level textstyle property with ShowkaseTypography annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography("name", "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "title",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "TYPOGRAPHY"
                  )
                  fun title() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable with wrapped textstyle property with ShowkaseColor annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
        
        class WrapperClass {
            @ShowkaseTypography("name", "group")
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable() {
                  }

                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "title",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "TYPOGRAPHY"
                  )
                  fun WrapperClass_title() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview parameter and showkase composable annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable(name = "name", group = "group")
        @Composable
        fun TestComposable(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
        }
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.ParameterProvider
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT",
                    previewParameterClass = [ParameterProvider::class]
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview parameter and preview annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.ui.tooling.preview.Preview
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        @Preview(name = "name", group = "group")
        @Composable
        fun TestComposable(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
        }
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.ParameterProvider
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT",
                    previewParameterClass = [ParameterProvider::class]
                  )
                  fun TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with showkase and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )
        
        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        @Preview("name", "group")
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level color property with showkasecolor and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                        ShowkaseBrowserColor(
                            colorGroup = "group",
                            colorName = "name",
                            colorKDoc = "",
                            color = red)
                      )
                
                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level textstyle property with showkasetypography and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography("name", "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
                        ShowkaseBrowserTypography(
                            typographyGroup = "group",
                            typographyName = "name",
                            typographyKDoc = "",
                            textStyle = title)
                      )

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level color property and composable function generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
        
        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )

                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                        ShowkaseBrowserColor(
                            colorGroup = "group",
                            colorName = "name",
                            colorKDoc = "",
                            color = red)
                      )

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level textstyle property and composable function generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name", "group")
        @Composable
        fun TestComposable() {
            
        }
        
        @ShowkaseTypography("name", "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )

                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
                        ShowkaseBrowserTypography(
                            typographyGroup = "group",
                            typographyName = "name",
                            typographyKDoc = "",
                            textStyle = title)
                      )

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside class with showkase annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
                
            }
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { WrapperClass().TestComposable() })
                      )

                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside object with showkase annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        object WrapperClass {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
                
            }
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { WrapperClass.TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `color property inside class with showkasecolor annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        class WrapperClass {
            @ShowkaseColor("name", "group")
            val color = Color(0xffff0000)
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                        ShowkaseBrowserColor(
                            colorGroup = "group",
                            colorName = "name",
                            colorKDoc = "",
                            color = WrapperClass().color)
                      )

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `textstyle property inside class with showkasetypography annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        class WrapperClass {
            @ShowkaseTypography("name", "group")
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
                        ShowkaseBrowserTypography(
                            typographyGroup = "group",
                            typographyName = "name",
                            typographyKDoc = "",
                            textStyle = WrapperClass().title)
                      )
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `color property inside object with showkasecolor annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        object WrapperClass {
            @ShowkaseColor("name", "group")
            val color = Color(0xffff0000)
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                        ShowkaseBrowserColor(
                            colorGroup = "group",
                            colorName = "name",
                            colorKDoc = "",
                            color = WrapperClass.color)
                      )

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `textstyle property inside object with showkasetypography annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        object WrapperClass {
            @ShowkaseTypography("name", "group")
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
                        ShowkaseBrowserTypography(
                            typographyGroup = "group",
                            typographyName = "name",
                            typographyKDoc = "",
                            textStyle = WrapperClass.title)
                      )

                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside companion object with showkase annotation and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            companion object {
                @ShowkaseComposable("name", "group")
                @Composable
                fun TestComposable() {
                    
                }
            }
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            component = @Composable { WrapperClass.TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `wrapped composable function with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
        
        class WrapperClass {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.ParameterProvider
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT",
                    previewParameterClass = [ParameterProvider::class]
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside object with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        
        object WrapperObject {
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
            
            }
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperObject
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperObject::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperObject_TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside object with preview annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        object WrapperObject {
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperObject
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperObject::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperObject_TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside companion object with showkase annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        
        class WrapperClass {
            companion object {
                @ShowkaseComposable("name", "group")
                @Composable
                fun TestComposable() {
                
                }
            }
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_TestComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function inside companion object with preview annotation generates only metadata file`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            companion object {
                @Preview("name", "group")
                @Composable
                fun TestComposable() {
                
                }
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name",
                    showkaseGroup = "group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
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
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable("name1", "group1")
        @Composable
        fun TestComposable1() {
            
        }
        
        @ShowkaseComposable("name2", "group1")
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name1",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable1() {
                  }
                
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name2",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name1",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable1() {
                  }
                
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name2",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
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
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        
        @Preview("name1", "group1")
        @Composable
        fun TestComposable1() {
            
        }

        @ShowkaseComposable("name2", "group1")
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name1",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable1() {
                  }

                  @ShowkaseCodegenMetadata(
                    showkaseName = "name2",
                    showkaseGroup = "group1",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable2",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable2() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function with both annotations gives priority to showkase annotation` () {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        
        @Preview("name1", "group1")
        @ShowkaseComposable("name2", "group2")
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
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "name2",
                    showkaseGroup = "group2",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "TestComposable1",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun TestComposable1() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `composable function with kdoc inside object with showkase annotation and showkaseroot generates 2 files`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        object WrapperClass {
            /**
             * This component shows some static text in cursive text style. 
             * 
             * Example usage:
             *
             * ```
             * @Composable
             * fun MyComposable() {
             *     CursiveTextComponentPreview()
             * }
             * ```
             */
            @ShowkaseComposable("name", "group")
            @Composable
            fun TestComposable() {
                
            }
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull() 
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = ""${'"'}
                            |This component shows some static text in cursive text style. 
                            |
                            | Example usage:
                            |
                            | ```
                            | @Composable
                            | fun MyComposable() {
                            |    CursiveTextComponentPreview()
                            | }
                            | ```
                            ""${'"'}.trimMargin(),
                            component = @Composable { WrapperClass.TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with showkase annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable
        @Composable
        fun testComposable() {
            
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "Default Group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with preview annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        @Preview
        @Composable
        fun testComposable() {
            
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "Default Group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level property with showkase color annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        @ShowkaseColor
        val red = Color(0xffff0000)
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Red",
                    showkaseGroup = "Default Group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "red",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "COLOR"
                  )
                  fun red() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level property with showkase typography annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Title",
                    showkaseGroup = "Default Group",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "title",
                    insideObject = false,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    showkaseMetadataType = "TYPOGRAPHY"
                  )
                  fun title() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside class with showkase annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            @ShowkaseComposable
            @Composable
            fun testComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside class with preview annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            @Preview
            @Composable
            fun testComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `property inside class with showkase color annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        class WrapperClass {
            @ShowkaseColor
            val red = Color(0xffff0000)
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Red",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "red",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COLOR"
                  )
                  fun WrapperClass_red() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `property inside class with showkase typography annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        class WrapperClass {
            @ShowkaseTypography
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Title",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "title",
                    insideObject = false,
                    insideWrapperClass = true,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "TYPOGRAPHY"
                  )
                  fun WrapperClass_title() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside object with showkase annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        object WrapperClass {
            @ShowkaseComposable
            @Composable
            fun testComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside object with preview annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        object WrapperClass {
            @Preview
            @Composable
            fun testComposable() {
                
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `property inside object with showkase color annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        object WrapperClass {
            @ShowkaseColor
            val red = Color(0xffff0000)
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Red",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "red",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COLOR"
                  )
                  fun WrapperClass_red() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `property inside object with showkase typography annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        object WrapperClass {
            @ShowkaseTypography
            val title = TextStyle(
                fontFamily = FontFamily.Cursive
            )
        }
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "Title",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "title",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "TYPOGRAPHY"
                  )
                  fun WrapperClass_title() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside companion object with showkase annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            companion object {
                @ShowkaseComposable
                @Composable
                fun testComposable() {
                    
                }
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `function inside companion object with preview annotation and no name or group`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        class WrapperClass {
            companion object {
                @Preview
                @Composable
                fun testComposable() {
                    
                }
            }
        }
    """)
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase
                
                import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
                import com.airbnb.android.showkase_processor_testing.WrapperClass
                
                class ShowkaseMetadataShowkase_processor_testing {
                  @ShowkaseCodegenMetadata(
                    showkaseName = "TestComposable",
                    showkaseGroup = "WrapperClass",
                    packageName = "com.airbnb.android.showkase_processor_testing",
                    packageSimpleName = "showkase_processor_testing",
                    showkaseElementName = "testComposable",
                    insideObject = true,
                    insideWrapperClass = false,
                    showkaseKDoc = "",
                    enclosingClass = [WrapperClass::class],
                    showkaseMetadataType = "COMPONENT"
                  )
                  fun WrapperClass_testComposable() {
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with showkase and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with showkase and showkaseroot and no name`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        
        @ShowkaseComposable(group = "group")
        @Composable
        fun testComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            component = @Composable { testComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with preview and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.compose.runtime.Composable
        
        @Preview
        @Composable
        fun TestComposable() {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      )
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `color property inside class with showkasecolor annotation and showkaseroot with no group`() {
    
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        class WrapperClass {
            @ShowkaseColor("name")
            val color = Color(0xffff0000)
        }
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                        ShowkaseBrowserColor(
                            colorGroup = "WrapperClass",
                            colorName = "name",
                            colorKDoc = "",
                            color = WrapperClass().color)
                      )

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level textstyle property with showkasetypography and showkaseroot with no name`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography(group = "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """
        )

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>()
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()

                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>(
                        ShowkaseBrowserTypography(
                            typographyGroup = "group",
                            typographyName = "Title",
                            typographyKDoc = "",
                            textStyle = title)
                      )

                  override fun getShowkaseComponents() = componentList

                  override fun getShowkaseColors() = colorList

                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with showkase composable and preview parameter and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
        
        @ShowkaseComposable
        @Composable
        fun TestComposable() {
            
        }
        
        @ShowkaseComposable
        @Composable
        fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      ).apply {
                          ParameterProvider().values.iterator().forEach { previewParam -> 
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "Default Group",
                                      componentName = "TestComposable2",
                                      componentKDoc = "",
                                      component = @Composable { TestComposable2(previewParam) }
                                  )
                              )
                          }
                      }
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()
                
                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList
                
                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with preview and preview parameter and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.ui.tooling.preview.Preview
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        @Preview
        @Composable
        fun TestComposable() {
            
        }
        
        @Preview
        @Composable
        fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
        }
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            component = @Composable { TestComposable() })
                      ).apply {
                          ParameterProvider().values.iterator().forEach { previewParam -> 
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "Default Group",
                                      componentName = "TestComposable2",
                                      componentKDoc = "",
                                      component = @Composable { TestComposable2(previewParam) }
                                  )
                              )
                          }
                      }
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()
                
                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList
                
                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `wrapped function with showkase composable and preview parameter and showkaseroot `() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
        
        class WrapperClass {
            @ShowkaseComposable(name = "name", group = "group")
            @Composable
            fun TestComposable(@PreviewParameter(provider = ParameterProvider::class) text: String) {
                
            }
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> =
                      mutableListOf<ShowkaseBrowserComponent>().apply {
                          ParameterProvider().values.iterator().forEach { previewParam -> 
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "group",
                                      componentName = "name",
                                      componentKDoc = "",
                                      component = @Composable { WrapperClass().TestComposable(previewParam) }
                                  )
                              )
                          }
                      }
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()
                
                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList
                
                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `object function with preview annotation and preview parameter and showkaseroot`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.ui.tooling.preview.Preview
        import androidx.ui.tooling.preview.PreviewParameter
        import androidx.ui.tooling.preview.PreviewParameterProvider
        import androidx.compose.runtime.Composable
        
        class ParameterProvider : PreviewParameterProvider<String> {
            override val values: Sequence<String>
                get() = sequenceOf(
                    "String1",
                    "String2"
                )
        
            override val count: Int
                get() = super.count
        }
        
        object WrapperClass {
            @Preview(name = "name", group = "group")
            @Composable
            fun TestComposable(@PreviewParameter(provider = ParameterProvider::class) text: String) {
                
            }
        }
    """)

        val kotlinShowkaseRootSource = SourceFile.kotlin("TestShowkaseRoot.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRoot
        import com.airbnb.android.showkase.annotation.ShowkaseRootModule
        
        @ShowkaseRoot
        class TestShowkaseRoot: ShowkaseRootModule {
        
        }
    """
        )

        val result = compileKotlinSource(listOf(kotlinComposableSource, kotlinShowkaseRootSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(2)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }).isNotNull()
        assertThat(result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> =
                      mutableListOf<ShowkaseBrowserComponent>().apply {
                          ParameterProvider().values.iterator().forEach { previewParam -> 
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "group",
                                      componentName = "name",
                                      componentKDoc = "",
                                      component = @Composable { WrapperClass.TestComposable(previewParam) }
                                  )
                              )
                          }
                      }
                
                  val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()
                
                  val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
                
                  override fun getShowkaseComponents() = componentList
                
                  override fun getShowkaseColors() = colorList
                
                  override fun getShowkaseTypography() = typographyList
                }
            """.trimIndent()
            )
        }
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootIntentCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                fun createShowkaseBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
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
