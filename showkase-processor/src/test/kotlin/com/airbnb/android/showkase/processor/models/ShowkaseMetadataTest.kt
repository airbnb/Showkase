package com.airbnb.android.showkase.processor.models

import androidx.room.compiler.processing.XFieldElement
import androidx.room.compiler.processing.XMethodElement
import androidx.room.compiler.processing.util.Source
import androidx.room.compiler.processing.util.runProcessorTest
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import strikt.assertions.single

class ShowkaseMetadataTest {

    @Test
    fun isTopLevelFunction() {
        val libSource = Source.kotlin(
            "lib.kt",
            """
            @com.airbnb.android.showkase.processor.models.MyAnnotation
            fun foo() {}

            class Bar {
                fun enclosedFoo() {}
            }
            """.trimIndent()
        )
        runProcessorTest(listOf(libSource)) { invocation ->
            val barClass = invocation.processingEnv.requireTypeElement("Bar")

            expectThat(barClass.getDeclaredMethods()
                .single())
                .get { isTopLevel(enclosingElement) }
                .isFalse()

            expectThat(invocation.roundEnv.getElementsAnnotatedWith(MyAnnotation::class))
                .single()
                .isA<XMethodElement>()
                .get { isTopLevel(enclosingElement) }
                .isTrue()
        }
    }

    @Test
    fun isTopLevelFunctionProperty() {
        val libSource = Source.kotlin(
            "lib.kt",
            """
            @com.airbnb.android.showkase.processor.models.MyAnnotation
            val foo: Int = 0

            class Bar {
                val enclosedFoo: Int = 0
            }
            """.trimIndent()
        )
        runProcessorTest(listOf(libSource)) { invocation ->
            val barClass = invocation.processingEnv.requireTypeElement("Bar")

            expectThat(barClass.getDeclaredMethods().single())
                .get { isTopLevel(enclosingElement) }
                .isFalse()

            if (invocation.isKsp) {
                expectThat(invocation.roundEnv.getElementsAnnotatedWith(MyAnnotation::class))
                    .single()
                    .isA<XFieldElement>()
                    .get { isTopLevel(enclosingElement) }
                    .isTrue()
            } else {
                expectThat(invocation.roundEnv.getElementsAnnotatedWith(MyAnnotation::class))
                    .single()
                    .isA<XMethodElement>()
                    .get { isTopLevel(enclosingElement) }
                    .isTrue()
            }
        }
    }
}

annotation class MyAnnotation
