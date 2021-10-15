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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        import androidx.compose.ui.tooling.preview.Preview
        
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
        import androidx.compose.ui.tooling.preview.Preview
        
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
        import androidx.compose.ui.tooling.preview.Preview
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
        import androidx.compose.ui.tooling.preview.Preview
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
        import androidx.compose.ui.tooling.preview.Preview
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
        import androidx.compose.ui.tooling.preview.Preview

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
    fun `open class with no interface but ShowkaseScreenshoTest annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("MyScreenshotTest.kt", """
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        
        @ShowkaseScreenshot
        open class MyScreenshotTest
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Only an implementation of ShowkaseScreenshotTest can be annotated with @ShowkaseScreenshot"
        assertThat(result.messages).contains(error)
    }

    @Test
    fun `closed class with right interface and showkasescreenshottest annotation throws compilation error`() {
        val kotlinSource = SourceFile.kotlin("MyScreenshotTest.kt", """
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        
        @ShowkaseScreenshot
        class MyScreenshotTest: ShowkaseScreenshotTest
    """
        )
        val result = compileKotlinSource(listOf(kotlinSource))

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(0)
        val error = "Class annotated with ShowkaseScreenshot needs to be an abstract/open class"
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable function with preview and showkaseroot generates 1 file`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.tooling.preview.Preview
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 1,
                  numTypography = 0
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 1
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 1,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 1
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 1,
                  numTypography = 0
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 1
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 1,
                  numTypography = 0
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 1
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "name",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
        
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
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
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
        
        import androidx.compose.ui.tooling.preview.Preview
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_TestComposable""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with preview and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.tooling.preview.Preview
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 1,
                  numTypography = 0
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 0,
                  numColors = 0,
                  numTypography = 1
                )
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 1,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable""${'"'},
                            component = @Composable { TestComposable() })
                      ).apply {
                          ParameterProvider().values.iterator().asSequence().forEachIndexed { index, previewParam ->
                              
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "Default Group",
                                      componentName = "TestComposable2",
                                      componentKDoc = "",
                                      componentKey =
                                          ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable2_${'$'}index""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level function with preview and preview parameter and showkaseroot and no name or group`() {
        val kotlinComposableSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 1,
                  numComposablesWithPreviewParameter = 1,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                        ShowkaseBrowserComponent(
                            group = "Default Group",
                            componentName = "TestComposable",
                            componentKDoc = "",
                            componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable""${'"'},
                            component = @Composable { TestComposable() })
                      ).apply {
                          ParameterProvider().values.iterator().asSequence().forEachIndexed { index, previewParam ->
                              
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "Default Group",
                                      componentName = "TestComposable2",
                                      componentKDoc = "",
                                      componentKey =
                                          ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable2_${'$'}index""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 1,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> =
                      mutableListOf<ShowkaseBrowserComponent>().apply {
                          ParameterProvider().values.iterator().asSequence().forEachIndexed { index, previewParam ->
                              
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "group",
                                      componentName = "name",
                                      componentKDoc = "",
                                      componentKey =
                                          ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name_${'$'}index""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
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
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }).isNotNull()
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name ==  "TestShowkaseRootCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing

                import androidx.compose.runtime.Composable
                import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
                import com.airbnb.android.showkase.models.ShowkaseBrowserColor
                import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
                import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import kotlin.collections.List
                
                @ShowkaseRootCodegen(
                  numComposablesWithoutPreviewParameter = 0,
                  numComposablesWithPreviewParameter = 1,
                  numColors = 0,
                  numTypography = 0
                )
                class TestShowkaseRootCodegen : ShowkaseProvider {
                  val componentList: List<ShowkaseBrowserComponent> =
                      mutableListOf<ShowkaseBrowserComponent>().apply {
                          ParameterProvider().values.iterator().asSequence().forEachIndexed { index, previewParam ->
                              
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "group",
                                      componentName = "name",
                                      componentKDoc = "",
                                      componentKey =
                                          ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name_${'$'}index""${'"'},
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
            it.name ==  "TestShowkaseRootShowkaseExtensionFunctionsCodegen.kt"
        }!!.let {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.content.Context
                import android.content.Intent
                import com.airbnb.android.showkase.models.Showkase
                import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
                import com.airbnb.android.showkase.models.ShowkaseProvider
                import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
                
                /**
                 * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
                 */
                fun Showkase.getBrowserIntent(context: Context): Intent {
                    val intent = Intent(context, ShowkaseBrowserActivity::class.java)
                    intent.putExtra("SHOWKASE_ROOT_MODULE",
                        "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
                    return intent
                }
                
                /**
                 * Helper function that's give's you access to Showkase metadata. This contains data about the
                 * composables, colors and typography in your codebase that's rendered in showakse.
                 */
                fun Showkase.getMetadata(): ShowkaseElementsMetadata {
                    try {
                      val showkaseComponentProvider =
                          Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                          as ShowkaseProvider
                      return showkaseComponentProvider.metadata()
                    } catch(exception: ClassNotFoundException) {
                      error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
                    }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level composable and class with @ScreenshotTest generates screenshot test for composable`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.runtime.Composable
        
        @Composable
        fun TestComposable1() {
            
        }

        @Composable
        fun TestComposable2() {
            
        }
    """)
        val kotlinRootCodegenSource = SourceFile.kotlin("TestShowkaseRootCodegen.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing

        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
        import com.airbnb.android.showkase.models.ShowkaseBrowserColor
        import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
        import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import kotlin.collections.List
        
        @ShowkaseRootCodegen(
          numComposablesWithoutPreviewParameter = 2,
          numComposablesWithPreviewParameter = 0,
          numColors = 0,
          numTypography = 0
        )
        class TestShowkaseRootCodegen : ShowkaseProvider {
          val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                ShowkaseBrowserComponent(
                    group = "group",
                    componentName = "name",
                    componentKDoc = "",
                    componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
                    component = @Composable { TestComposable1() }),
                ShowkaseBrowserComponent(
                    group = "group",
                    componentName = "name",
                    componentKDoc = "",
                    componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
                    component = @Composable { TestComposable2() })
              )
        
          val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>()
        
          val typographyList: List<ShowkaseBrowserTypography> = listOf<ShowkaseBrowserTypography>()
        
          override fun getShowkaseComponents() = componentList
        
          override fun getShowkaseColors() = colorList
        
          override fun getShowkaseTypography() = typographyList
        }
    """
        )

        val kotlinRootExtensionSource = SourceFile.kotlin("TestShowkaseRootExtensionFunctions.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import android.content.Context
        import android.content.Intent
        import com.airbnb.android.showkase.models.Showkase
        import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
        
        /**
         * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
         */
        fun Showkase.getBrowserIntent(context: Context): Intent {
            val intent = Intent(context, ShowkaseBrowserActivity::class.java)
            intent.putExtra("SHOWKASE_ROOT_MODULE",
                "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
            return intent
        }
        
        /**
         * Helper function that's give's you access to Showkase metadata. This contains data about the
         * composables, colors and typography in your codebase that's rendered in showakse.
         */
        fun Showkase.getMetadata(): ShowkaseElementsMetadata {
            try {
              val showkaseComponentProvider =
                  Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                  as ShowkaseProvider
              return showkaseComponentProvider.metadata()
            } catch(exception: ClassNotFoundException) {
              error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
            }
        }
    """
        )

        val kotlinTestSource = SourceFile.kotlin("MyShowkaseScreenshotTest.kt", """
        package com.airbnb.android.showkase_processor_testing

        import android.graphics.Bitmap
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
        
        @ShowkaseScreenshot
        abstract class MyScreenshotTest: ShowkaseScreenshotTest {
            override fun onScreenshot(
                id: String,
                name: String,
                group: String,
                screenshotType: ShowkaseScreenshotType,
                screenshotBitmap: Bitmap,
            ) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(
            listOf(
                kotlinSource,
                kotlinRootCodegenSource,
                kotlinRootExtensionSource,
                kotlinTestSource
            )
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.Manifest
                import androidx.compose.ui.test.junit4.ComposeContentTestRule
                import androidx.compose.ui.test.junit4.createComposeRule
                import androidx.test.rule.GrantPermissionRule
                import com.airbnb.android.showkase.models.Showkase
                import kotlin.jvm.JvmField
                import org.junit.Rule
                import org.junit.Test
                import org.junit.runner.RunWith
                import org.junit.runners.JUnit4
                
                @RunWith(JUnit4::class)
                class MyScreenshotTest_ShowkaseCodegen : MyScreenshotTest() {
                  @get:Rule
                  override val composeTestRule: ComposeContentTestRule = createComposeRule()
                
                  @Rule
                  @JvmField
                  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.READ_EXTERNAL_STORAGE 
                      )
                
                  @Test
                  fun composable_screenshot_test_0() {
                    takeComposableScreenshot(Showkase.getMetadata().componentList[0])
                  }

                  @Test
                  fun composable_screenshot_test_1() {
                    takeComposableScreenshot(Showkase.getMetadata().componentList[1])
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level color and class with @ScreenshotTest generates screenshot test for composable`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestColors.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.graphics.Color
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        
        
        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)
    """)
        val kotlinRootCodegenSource = SourceFile.kotlin("TestShowkaseRootCodegen.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
        import com.airbnb.android.showkase.models.ShowkaseBrowserColor
        import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
        import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import kotlin.collections.List
        
        @ShowkaseRootCodegen(
          numComposablesWithoutPreviewParameter = 0,
          numComposablesWithPreviewParameter = 0,
          numColors = 1,
          numTypography = 0
        )
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
    """
        )

        val kotlinRootExtensionSource = SourceFile.kotlin("TestShowkaseRootExtensionFunctions.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import android.content.Context
        import android.content.Intent
        import com.airbnb.android.showkase.models.Showkase
        import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
        
        /**
         * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
         */
        fun Showkase.getBrowserIntent(context: Context): Intent {
            val intent = Intent(context, ShowkaseBrowserActivity::class.java)
            intent.putExtra("SHOWKASE_ROOT_MODULE",
                "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
            return intent
        }
        
        /**
         * Helper function that's give's you access to Showkase metadata. This contains data about the
         * composables, colors and typography in your codebase that's rendered in showakse.
         */
        fun Showkase.getMetadata(): ShowkaseElementsMetadata {
            try {
              val showkaseComponentProvider =
                  Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                  as ShowkaseProvider
              return showkaseComponentProvider.metadata()
            } catch(exception: ClassNotFoundException) {
              error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
            }
        }
    """
        )

        val kotlinTestSource = SourceFile.kotlin("MyShowkaseScreenshotTest.kt", """
        package com.airbnb.android.showkase_processor_testing

        import android.graphics.Bitmap
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
        
        @ShowkaseScreenshot
        abstract class MyScreenshotTest: ShowkaseScreenshotTest {
            override fun onScreenshot(
                id: String,
                name: String,
                group: String,
                screenshotType: ShowkaseScreenshotType,
                screenshotBitmap: Bitmap,
            ) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(
            listOf(
                kotlinSource,
                kotlinRootCodegenSource,
                kotlinRootExtensionSource,
                kotlinTestSource
            )
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.Manifest
                import androidx.compose.ui.test.junit4.ComposeContentTestRule
                import androidx.compose.ui.test.junit4.createComposeRule
                import androidx.test.rule.GrantPermissionRule
                import com.airbnb.android.showkase.models.Showkase
                import kotlin.jvm.JvmField
                import org.junit.Rule
                import org.junit.Test
                import org.junit.runner.RunWith
                import org.junit.runners.JUnit4
                
                @RunWith(JUnit4::class)
                class MyScreenshotTest_ShowkaseCodegen : MyScreenshotTest() {
                  @get:Rule
                  override val composeTestRule: ComposeContentTestRule = createComposeRule()
                
                  @Rule
                  @JvmField
                  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.READ_EXTERNAL_STORAGE 
                      )
                
                  @Test
                  fun color_screenshot_test_0() {
                    takeColorScreenshot(Showkase.getMetadata().colorList[0])
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `top level textstyle and class with @ScreenshotTest generates screenshot test for composable`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestColors.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        
        @ShowkaseTypography("name", "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """)
        val kotlinRootCodegenSource = SourceFile.kotlin("TestShowkaseRootCodegen.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
        import com.airbnb.android.showkase.models.ShowkaseBrowserColor
        import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
        import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import kotlin.collections.List
        
        @ShowkaseRootCodegen(
          numComposablesWithoutPreviewParameter = 0,
          numComposablesWithPreviewParameter = 0,
          numColors = 0,
          numTypography = 1
        )
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
    """
        )

        val kotlinRootExtensionSource = SourceFile.kotlin("TestShowkaseRootExtensionFunctions.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import android.content.Context
        import android.content.Intent
        import com.airbnb.android.showkase.models.Showkase
        import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
        
        /**
         * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
         */
        fun Showkase.getBrowserIntent(context: Context): Intent {
            val intent = Intent(context, ShowkaseBrowserActivity::class.java)
            intent.putExtra("SHOWKASE_ROOT_MODULE",
                "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
            return intent
        }
        
        /**
         * Helper function that's give's you access to Showkase metadata. This contains data about the
         * composables, colors and typography in your codebase that's rendered in showakse.
         */
        fun Showkase.getMetadata(): ShowkaseElementsMetadata {
            try {
              val showkaseComponentProvider =
                  Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                  as ShowkaseProvider
              return showkaseComponentProvider.metadata()
            } catch(exception: ClassNotFoundException) {
              error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
            }
        }
    """
        )

        val kotlinTestSource = SourceFile.kotlin("MyShowkaseScreenshotTest.kt", """
        package com.airbnb.android.showkase_processor_testing

        import android.graphics.Bitmap
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
        
        @ShowkaseScreenshot
        abstract class MyScreenshotTest: ShowkaseScreenshotTest {
            override fun onScreenshot(
                id: String,
                name: String,
                group: String,
                screenshotType: ShowkaseScreenshotType,
                screenshotBitmap: Bitmap,
            ) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(
            listOf(
                kotlinSource,
                kotlinRootCodegenSource,
                kotlinRootExtensionSource,
                kotlinTestSource
            )
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.Manifest
                import androidx.compose.ui.test.junit4.ComposeContentTestRule
                import androidx.compose.ui.test.junit4.createComposeRule
                import androidx.test.rule.GrantPermissionRule
                import com.airbnb.android.showkase.models.Showkase
                import kotlin.jvm.JvmField
                import org.junit.Rule
                import org.junit.Test
                import org.junit.runner.RunWith
                import org.junit.runners.JUnit4
                
                @RunWith(JUnit4::class)
                class MyScreenshotTest_ShowkaseCodegen : MyScreenshotTest() {
                  @get:Rule
                  override val composeTestRule: ComposeContentTestRule = createComposeRule()
                
                  @Rule
                  @JvmField
                  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.READ_EXTERNAL_STORAGE 
                      )
                
                  @Test
                  fun typography_screenshot_test_0() {
                    takeTypographyScreenshot(Showkase.getMetadata().typographyList[0])
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `class with @ScreenshotTest only generates screenshot test for only non preview parameter composable`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import androidx.compose.ui.tooling.preview.PreviewParameter
        import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
            @ShowkaseComposable
            @Composable
            fun TestComposable1() {
                
            }
        }
        
        
        @ShowkaseComposable
        @Composable
        fun TestComposable2(@PreviewParameter(provider = ParameterProvider::class) text: String) {
            
        }
    """)
        val kotlinRootCodegenSource = SourceFile.kotlin("TestShowkaseRootCodegen.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing

        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
        import com.airbnb.android.showkase.models.ShowkaseBrowserColor
        import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
        import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import kotlin.collections.List
        
        @ShowkaseRootCodegen(
          numComposablesWithoutPreviewParameter = 1,
          numComposablesWithPreviewParameter = 1,
          numColors = 0,
          numTypography = 0
        )
        class TestShowkaseRootCodegen : ShowkaseProvider {
          val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                ShowkaseBrowserComponent(
                    group = "group",
                    componentName = "name",
                    componentKDoc = "",
                    componentKey =
                                ""${'"'}com.airbnb.android.showkase_processor_testing_com.airbnb.android.showkase_processor_testing.WrapperClass_group_name""${'"'},
                    component = @Composable { WrapperClass().TestComposable1() }),
              ).apply {
                          ParameterProvider().values.iterator().asSequence().forEachIndexed { index, previewParam ->
                              
                              add(
                                  ShowkaseBrowserComponent(
                                      group = "Default Group",
                                      componentName = "TestComposable2",
                                      componentKDoc = "",
                                      componentKey =
                                          ""${'"'}com.airbnb.android.showkase_processor_testing_null_DefaultGroup_TestComposable2_${'$'}index""${'"'},
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
    """
        )

        val kotlinRootExtensionSource = SourceFile.kotlin("TestShowkaseRootExtensionFunctions.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import android.content.Context
        import android.content.Intent
        import com.airbnb.android.showkase.models.Showkase
        import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
        
        /**
         * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
         */
        fun Showkase.getBrowserIntent(context: Context): Intent {
            val intent = Intent(context, ShowkaseBrowserActivity::class.java)
            intent.putExtra("SHOWKASE_ROOT_MODULE",
                "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
            return intent
        }
        
        /**
         * Helper function that's give's you access to Showkase metadata. This contains data about the
         * composables, colors and typography in your codebase that's rendered in showakse.
         */
        fun Showkase.getMetadata(): ShowkaseElementsMetadata {
            try {
              val showkaseComponentProvider =
                  Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                  as ShowkaseProvider
              return showkaseComponentProvider.metadata()
            } catch(exception: ClassNotFoundException) {
              error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
            }
        }
    """
        )

        val kotlinTestSource = SourceFile.kotlin("MyShowkaseScreenshotTest.kt", """
        package com.airbnb.android.showkase_processor_testing

        import android.graphics.Bitmap
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
        
        @ShowkaseScreenshot
        abstract class MyScreenshotTest: ShowkaseScreenshotTest {
            override fun onScreenshot(
                id: String,
                name: String,
                group: String,
                screenshotType: ShowkaseScreenshotType,
                screenshotBitmap: Bitmap,
            ) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(
            listOf(
                kotlinSource,
                kotlinRootCodegenSource,
                kotlinRootExtensionSource,
                kotlinTestSource
            )
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.Manifest
                import androidx.compose.ui.test.junit4.ComposeContentTestRule
                import androidx.compose.ui.test.junit4.createComposeRule
                import androidx.test.rule.GrantPermissionRule
                import com.airbnb.android.showkase.models.Showkase
                import kotlin.jvm.JvmField
                import org.junit.Rule
                import org.junit.Test
                import org.junit.runner.RunWith
                import org.junit.runners.JUnit4
                
                @RunWith(JUnit4::class)
                class MyScreenshotTest_ShowkaseCodegen : MyScreenshotTest() {
                  @get:Rule
                  override val composeTestRule: ComposeContentTestRule = createComposeRule()
                
                  @Rule
                  @JvmField
                  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.READ_EXTERNAL_STORAGE 
                      )
                
                  @Test
                  fun composable_screenshot_test_0() {
                    takeComposableScreenshot(Showkase.getMetadata().componentList[0])
                  }
                }
            """.trimIndent()
            )
        }
    }

    @Test
    fun `class with @ScreenshotTest generates screenshot test for all UI elements`() {
        val kotlinSource = SourceFile.kotlin("GeneratedTestComposables.kt", """
        package com.airbnb.android.showkase_processor_testing
        
        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseColor
        import com.airbnb.android.showkase.annotation.ShowkaseComposable
        import com.airbnb.android.showkase.annotation.ShowkaseTypography
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        
        @ShowkaseComposable(name = "name1", group = "group1")
        @Composable
        fun TestComposable1() {
            
        }

        @ShowkaseComposable(name = "name2", group = "group2")
        @Composable
        fun TestComposable2() {
            
        }

        @ShowkaseColor("name", "group")
        val red = Color(0xffff0000)

        @ShowkaseTypography("name", "group")
        val title = TextStyle(
            fontFamily = FontFamily.Cursive
        )
    """)
        val kotlinRootCodegenSource = SourceFile.kotlin("TestShowkaseRootCodegen.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing

        import androidx.compose.runtime.Composable
        import com.airbnb.android.showkase.annotation.ShowkaseRootCodegen
        import com.airbnb.android.showkase.models.ShowkaseBrowserColor
        import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
        import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import kotlin.collections.List
        
        @ShowkaseRootCodegen(
          numComposablesWithoutPreviewParameter = 2,
          numComposablesWithPreviewParameter = 0,
          numColors = 1,
          numTypography = 1
        )
        class TestShowkaseRootCodegen : ShowkaseProvider {
          val componentList: List<ShowkaseBrowserComponent> = mutableListOf<ShowkaseBrowserComponent>(
                ShowkaseBrowserComponent(
                    group = "group1",
                    componentName = "name1",
                    componentKDoc = "",
                    componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
                    component = @Composable { TestComposable1() }),
                ShowkaseBrowserComponent(
                    group = "group2",
                    componentName = "name2",
                    componentKDoc = "",
                    componentKey = ""${'"'}com.airbnb.android.showkase_processor_testing_null_group_name""${'"'},
                    component = @Composable { TestComposable2() })
              )
        
          val colorList: List<ShowkaseBrowserColor> = listOf<ShowkaseBrowserColor>(
                ShowkaseBrowserColor(
                    colorGroup = "group",
                    colorName = "name",
                    colorKDoc = "",
                    color = red)
              )
        
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
    """
        )

        val kotlinRootExtensionSource = SourceFile.kotlin("TestShowkaseRootExtensionFunctions.kt", """
        // This is an auto-generated file. Please do not edit/modify this file.
        package com.airbnb.android.showkase_processor_testing
        
        import android.content.Context
        import android.content.Intent
        import com.airbnb.android.showkase.models.Showkase
        import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
        import com.airbnb.android.showkase.models.ShowkaseProvider
        import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
        
        /**
         * Helper function that's autogenerated and gives you an intent to start the ShowkaseBrowser.
         */
        fun Showkase.getBrowserIntent(context: Context): Intent {
            val intent = Intent(context, ShowkaseBrowserActivity::class.java)
            intent.putExtra("SHOWKASE_ROOT_MODULE",
                "com.airbnb.android.showkase_processor_testing.TestShowkaseRoot")
            return intent
        }
        
        /**
         * Helper function that's give's you access to Showkase metadata. This contains data about the
         * composables, colors and typography in your codebase that's rendered in showakse.
         */
        fun Showkase.getMetadata(): ShowkaseElementsMetadata {
            try {
              val showkaseComponentProvider =
                  Class.forName("com.airbnb.android.showkase_processor_testing.TestShowkaseRootCodegen").newInstance()
                  as ShowkaseProvider
              return showkaseComponentProvider.metadata()
            } catch(exception: ClassNotFoundException) {
              error("The class wasn't generated correctly. Make sure that you have setup Showkase correctly by following the steps here - https://github.com/airbnb/Showkase#Installation.")
            }
        }
    """
        )

        val kotlinTestSource = SourceFile.kotlin("MyShowkaseScreenshotTest.kt", """
        package com.airbnb.android.showkase_processor_testing

        import android.graphics.Bitmap
        import com.airbnb.android.showkase.annotation.ShowkaseScreenshot
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotTest
        import com.airbnb.android.showkase.screenshot.testing.ShowkaseScreenshotType
        
        @ShowkaseScreenshot
        abstract class MyScreenshotTest: ShowkaseScreenshotTest {
            override fun onScreenshot(
                id: String,
                name: String,
                group: String,
                screenshotType: ShowkaseScreenshotType,
                screenshotBitmap: Bitmap,
            ) {
                
            }
        }
    """
        )
        val result = compileKotlinSource(
            listOf(
                kotlinSource,
                kotlinRootCodegenSource,
                kotlinRootExtensionSource,
                kotlinTestSource
            )
        )

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        assertThat(result.sourcesGeneratedByAnnotationProcessor.size).isEqualTo(1)
        result.sourcesGeneratedByAnnotationProcessor.forEach {
            assertThat(it).hasContent("""
                // This is an auto-generated file. Please do not edit/modify this file.
                package com.airbnb.android.showkase_processor_testing
                
                import android.Manifest
                import androidx.compose.ui.test.junit4.ComposeContentTestRule
                import androidx.compose.ui.test.junit4.createComposeRule
                import androidx.test.rule.GrantPermissionRule
                import com.airbnb.android.showkase.models.Showkase
                import kotlin.jvm.JvmField
                import org.junit.Rule
                import org.junit.Test
                import org.junit.runner.RunWith
                import org.junit.runners.JUnit4
                
                @RunWith(JUnit4::class)
                class MyScreenshotTest_ShowkaseCodegen : MyScreenshotTest() {
                  @get:Rule
                  override val composeTestRule: ComposeContentTestRule = createComposeRule()
                
                  @Rule
                  @JvmField
                  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.READ_EXTERNAL_STORAGE 
                      )
                
                  @Test
                  fun composable_screenshot_test_0() {
                    takeComposableScreenshot(Showkase.getMetadata().componentList[0])
                  }

                  @Test
                  fun composable_screenshot_test_1() {
                    takeComposableScreenshot(Showkase.getMetadata().componentList[1])
                  }

                  @Test
                  fun typography_screenshot_test_0() {
                    takeTypographyScreenshot(Showkase.getMetadata().typographyList[0])
                  }

                  @Test
                  fun color_screenshot_test_0() {
                    takeColorScreenshot(Showkase.getMetadata().colorList[0])
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
