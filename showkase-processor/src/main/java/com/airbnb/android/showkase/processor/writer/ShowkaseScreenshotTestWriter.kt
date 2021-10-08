package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.COLOR_PROPERTY_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.COMPONENT_PROPERTY_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseBrowserWriter.Companion.TYPOGRAPHY_PROPERTY_NAME
import com.airbnb.android.showkase.processor.writer.ShowkaseExtensionFunctionsWriter.Companion.SHOWKASE_OBJECT_CLASS_NAME
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment

internal class ShowkaseScreenshotTestWriter(private val processingEnv: ProcessingEnvironment) {
    internal fun generateScreenshotTests(
        componentsSize: Int,
        colorsSize: Int,
        typographySize: Int,
        rootModulePackageName: String,
        testClassName: String
    ) {
        val showkaseScreenshotTestClassName = "${testClassName}_ShowkaseCodegen"
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
                    addProperty(addComposeTestRuleProperty())
                    addTest(componentsSize, "composable", COMPONENT_PROPERTY_NAME)
                    addTest(typographySize, "typography", TYPOGRAPHY_PROPERTY_NAME)
                    addTest(colorsSize, "color", COLOR_PROPERTY_NAME)
                    build()
                }
            )

        fileBuilder.build().writeTo(processingEnv.filer)
    }

    private fun addComposeTestRuleProperty() =
        PropertySpec.builder(
            "composeTestRule",
            COMPOSE_CONTENT_TEST_RULE_CLASS_NAME
        )
            .addAnnotation(
                AnnotationSpec.builder(RULE_CLASSNAME)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                    .build()
            )
            .addModifiers(KModifier.OVERRIDE)
            .initializer("%T()", CREATE_COMPOSE_RULE_CLASS_NAME)
            .build()

    private fun TypeSpec.Builder.addTest(
        size: Int,
        testNamePrefix: String,
        propertyName: String
    ) {
        for (index in 0 until size) {
            val functionName = "${testNamePrefix}_screenshot_test_$index"
            addFunction(
                FunSpec.builder(functionName)
                    .addAnnotation(
                        AnnotationSpec.builder(JUNIT_TEST).build()
                    )
                    .addCode(
                        "take${testNamePrefix.replaceFirstChar { it.uppercase() }}Screenshot(%T.getMetadata().$propertyName[$index])",
                        SHOWKASE_OBJECT_CLASS_NAME
                    )
                    .build()
            )
        }
    }

    companion object {
        private const val JUNIT_ORG = "org.junit"
        private const val JUNIT_RUNNER = "$JUNIT_ORG.runner"
        private val RUNWITH_CLASSNAME = ClassName(JUNIT_RUNNER, "RunWith")
        private val JUNIT_CLASSNAME = ClassName("${JUNIT_RUNNER}s", "JUnit4")
        private val JUNIT_TEST = ClassName(JUNIT_ORG, "Test")
        private val RULE_CLASSNAME = ClassName(JUNIT_ORG, "Rule")
        private const val JUNIT4_PACKAGE = "androidx.compose.ui.test.junit4"
        private val COMPOSE_CONTENT_TEST_RULE_CLASS_NAME =
            ClassName(JUNIT4_PACKAGE, "ComposeContentTestRule")
        private val CREATE_COMPOSE_RULE_CLASS_NAME = ClassName(JUNIT4_PACKAGE, "createComposeRule")
    }
}