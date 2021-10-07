package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.annotation.ShowkaseCodegenMetadata
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.airbnb.android.showkase.processor.models.kotlinMetadata
import com.airbnb.android.showkase.processor.writer.ShowkaseExtensionFunctionsWriter.Companion.SHOWKASE_OBJECT_CLASS_NAME
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class ShowkaseScreenshotTestWriter(private val processingEnv: ProcessingEnvironment) {
    internal fun generateScreenshotTests(
        screenshotTestElement: Element,
        componentsSize: Int,
        colorsSize: Int,
        typographySize: Int,
        rootModulePackageName: String,
        testClassName: String
    ) {
        val showkaseScreenshotTestClassName = "$testClassName${ShowkaseBrowserWriter.CODEGEN_AUTOGEN_CLASS_NAME}"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseScreenshotTestClassName)
        fileBuilder
            .addType(
                with(TypeSpec.classBuilder(showkaseScreenshotTestClassName)) {
                    superclass(ClassName(rootModulePackageName, testClassName))
                    addAnnotation(
                        AnnotationSpec.builder(RUNWITH_CLASSNAME)
                            .addMember("%T::class", JUNIT_CLASSNAME)
                            .build()
                    )
                    addComposableTests(componentsSize)
                    addTypographyTests(typographySize)
                    addColorTests(colorsSize)
                    build()
                }
            )

        fileBuilder.build().writeTo(processingEnv.filer)
    }

    private fun TypeSpec.Builder.addComposableTests(componentsSize: Int) {
        for (index in 0 until componentsSize) {
            val functionName = "component_screenshot_test_$index"
            addFunction(
                FunSpec.builder(functionName)
                    .addAnnotation(
                        AnnotationSpec.builder(JUNIT_TEST).build()
                    )
                    .addCode(
                        "runTest(%T.getMetadata().componentList[$index])",
                        SHOWKASE_OBJECT_CLASS_NAME
                    )
                    .build()
            )
        }
    }

    private fun TypeSpec.Builder.addTypographyTests(typographySize: Int) {
        for (index in 0 until typographySize) {
            val functionName = "typography_screenshot_test_$index"
            addFunction(
                FunSpec.builder(functionName)
                    .addAnnotation(
                        AnnotationSpec.builder(JUNIT_TEST).build()
                    )
                    .addCode(
                        "runTest(%T.getMetadata().typographyList[$index])",
                        SHOWKASE_OBJECT_CLASS_NAME
                    )
                    .build()
            )
        }
    }

    private fun TypeSpec.Builder.addColorTests(colorsSize: Int) {
        for (index in 0 until colorsSize) {
            val functionName = "color_screenshot_test_$index"
            addFunction(
                FunSpec.builder(functionName)
                    .addAnnotation(
                        AnnotationSpec.builder(JUNIT_TEST).build()
                    )
                    .addCode(
                        "runTest(%T.getMetadata().colorList[$index])",
                        SHOWKASE_OBJECT_CLASS_NAME
                    )
                    .build()
            )
        }
    }

    companion object {
        private const val SHOWKASE_TESTING_PACKAGE_NAME = "com.airbnb.android.showkasetesting"
        private const val JUNIT_ORG = "org.junit"
        private const val JUNIT_RUNNER = "$JUNIT_ORG.runner"
        private val RUNWITH_CLASSNAME = ClassName(JUNIT_RUNNER, "RunWith")
        private val JUNIT_CLASSNAME = ClassName("${JUNIT_RUNNER}s", "JUnit4")
        private val JUNIT_TEST = ClassName(JUNIT_ORG, "Test")
    }
}