package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.writeTo
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class PaparazziShowkaseScreenshotTestWriter(private val environment: XProcessingEnv) {
    @Suppress("LongParameterList")
    internal fun generateScreenshotTests(
        screenshotTestPackageName: String,
        rootModulePackageName: String,
        testClassName: String
    ) {
        val showkaseScreenshotTestClassName = "${testClassName}_PaparazziShowkaseTest"
        val fileBuilder = getFileBuilder(screenshotTestPackageName, showkaseScreenshotTestClassName)
        fileBuilder
            .addImport(rootModulePackageName, "getMetadata")
            .addType(
                with(TypeSpec.classBuilder(showkaseScreenshotTestClassName)) {
                    superclass(ClassName(screenshotTestPackageName, testClassName))
                    addAnnotation(
                        AnnotationSpec.builder(ShowkaseScreenshotTestWriter.RUNWITH_CLASSNAME)
                            .addMember("%T::class", TEST_PARAMETER_INJECTOR_CLASSNAME)
                            .build()
                    )
                    addProperty(addPaparazziTestRuleProperty())
                    addPreviewProvider()
                    addProvider(
                        "PaparazziShowkaseDeviceConfigProvider",
                        LIST.parameterizedBy(DEVICE_CONFIG_CLASS_NAME),
                        "deviceConfigs"
                    )
                    addProvider(
                        "PaparazziShowkaseLayoutDirectionProvider",
                        LIST.parameterizedBy(LAYOUT_DIRECTION_CLASS_NAME),
                        "layoutDirections"
                    )
                    addProvider(
                        "PaparazziShowkaseUIModeProvider",
                        LIST.parameterizedBy(UI_MODE_CLASS_NAME),
                        "uiModes"
                    )
                    addTest()
                    build()
                }
            )

        fileBuilder.build().writeTo(environment.filer, mode = XFiler.Mode.Aggregating)
    }

    private fun TypeSpec.Builder.addPreviewProvider() {
        addType(with(TypeSpec.objectBuilder("PaparazziShowkasePreviewProvider")) {
            addSuperinterface(TEST_PARAMETER_VALUES_PROVIDER_CLASSNAME)
            addModifiers(KModifier.PRIVATE)
            addFunction(
                FunSpec.builder("provideValues")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(
                        LIST.parameterizedBy(
                            PAPARAZZI_SHOWKASE_TEST_PREVIEW_CLASS_NAME
                        )
                    )
                    .addCode(
                        CodeBlock.builder()
                            .add(
                                "val metadata = %T.getMetadata()",
                                ShowkaseExtensionFunctionsWriter.SHOWKASE_OBJECT_CLASS_NAME
                            )
                            .addLineBreak()
                            .add(
                                "val components = %N.componentList.map(::%T)",
                                "metadata",
                                COMPONENT_TEST_PREVIEW_CLASS_NAME
                            )
                            .addLineBreak()
                            .add(
                                "val colors = %N.colorList.map(::%T)",
                                "metadata",
                                COLOR_TEST_PREVIEW_CLASS_NAME
                            )
                            .addLineBreak()
                            .add(
                                "val typography = %N.typographyList.map(::%T)",
                                "metadata",
                                TYPOGRAPHY_TEST_PREVIEW_CLASS_NAME
                            )
                            .addLineBreak()
                            .add("return components + colors + typography")
                            .build()
                    )
                    .build()
            )
            build()
        })
    }

    private fun TypeSpec.Builder.addProvider(
        name: String,
        returnsTypeName: ParameterizedTypeName,
        returnsMember: String
    ) {
        addType(
            with(TypeSpec.objectBuilder(name)) {
                addSuperinterface(TEST_PARAMETER_VALUES_PROVIDER_CLASSNAME)
                addModifiers(KModifier.PRIVATE)
                addFunction(
                    FunSpec.builder("provideValues")
                        .addModifiers(KModifier.OVERRIDE)
                        .returns(returnsTypeName)
                        .addCode("return $returnsMember()")
                        .build()
                )
                build()
            }
        )
    }

    private fun addPaparazziTestRuleProperty() =
        PropertySpec.builder(
            "paparazzi",
            PAPARAZZI_CLASS_NAME
        )
            .addAnnotation(
                AnnotationSpec.builder(ShowkaseScreenshotTestWriter.RULE_CLASSNAME)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                    .build()
            )
            .initializer("%N()", "providePaparazzi")
            .build()

    @Suppress("LongMethod")
    private fun TypeSpec.Builder.addTest() {
        addFunction(
            FunSpec.builder("test_previews")
                .addAnnotation(
                    AnnotationSpec.builder(ShowkaseScreenshotTestWriter.JUNIT_TEST).build()
                )
                .addParameter(
                    ParameterSpec.builder(
                        "elementPreview",
                        PAPARAZZI_SHOWKASE_TEST_PREVIEW_CLASS_NAME
                    )
                        .addAnnotation(
                            AnnotationSpec.builder(TEST_PARAMETER_CLASS_NAME)
                                .addMember(
                                    "valuesProvider = %N::class",
                                    "PaparazziShowkasePreviewProvider"
                                )
                                .build()
                        )
                        .build()

                )
                .addParameter(
                    ParameterSpec.builder("config", DEVICE_CONFIG_CLASS_NAME)
                        .addAnnotation(
                            AnnotationSpec.builder(TEST_PARAMETER_CLASS_NAME)
                                .addMember(
                                    "valuesProvider = %N::class",
                                    "PaparazziShowkaseDeviceConfigProvider"
                                )
                                .build()
                        )
                        .build()
                )
                .addParameter(
                    ParameterSpec.builder("direction", LAYOUT_DIRECTION_CLASS_NAME)
                        .addAnnotation(
                            AnnotationSpec.builder(TEST_PARAMETER_CLASS_NAME)
                                .addMember(
                                    "valuesProvider = %N::class",
                                    "PaparazziShowkaseLayoutDirectionProvider"
                                )
                                .build()
                        )
                        .build()
                )
                .addParameter(
                    ParameterSpec.builder("uiMode", UI_MODE_CLASS_NAME)
                        .addAnnotation(
                            AnnotationSpec.builder(TEST_PARAMETER_CLASS_NAME)
                                .addMember(
                                    "valuesProvider = %N::class",
                                    "PaparazziShowkaseUIModeProvider"
                                )
                                .build()
                        )
                        .build()
                )
                .addCode(
                    "%N.unsafeUpdateConfig(%N.deviceConfig.copy(softButtons = false))",
                    "paparazzi",
                    "config"
                )
                .addCode("\n")
                .addCode(
                    "takePaparazziSnapshot(%N, %N, %N, %N)",
                    "paparazzi",
                    "elementPreview",
                    "direction",
                    "uiMode"
                )
                .build()
        )
    }

    companion object {
        private const val TEST_PARAMETER_INJECTOR_PACKAGE_NAME =
            "com.google.testing.junit.testparameterinjector"
        private val TEST_PARAMETER_INJECTOR_CLASSNAME = ClassName(
            TEST_PARAMETER_INJECTOR_PACKAGE_NAME,
            "TestParameterInjector"
        )
        private val TEST_PARAMETER_VALUES_PROVIDER_CLASSNAME = ClassName(
            TEST_PARAMETER_INJECTOR_PACKAGE_NAME,
            "TestParameter.TestParameterValuesProvider"
        )
        private val PAPARAZZI_CLASS_NAME = ClassName(
            "app.cash.paparazzi",
            "Paparazzi"
        )
        private val TEST_PARAMETER_CLASS_NAME = ClassName(
            TEST_PARAMETER_INJECTOR_PACKAGE_NAME,
            "TestParameter"
        )
        private const val PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME =
            "com.airbnb.android.showkase.screenshot.testing.paparazzi"
        private val PAPARAZZI_SHOWKASE_TEST_PREVIEW_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "PaparazziShowkaseTestPreview"
        )
        private val COMPONENT_TEST_PREVIEW_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "ComponentPaparazziShowkaseTestPreview"
        )
        private val COLOR_TEST_PREVIEW_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "ColorPaparazziShowkaseTestPreview"
        )
        private val TYPOGRAPHY_TEST_PREVIEW_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "TypographyPaparazziShowkaseTestPreview"
        )
        private val DEVICE_CONFIG_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "PaparazziShowkaseDeviceConfig"
        )
        private val UI_MODE_CLASS_NAME = ClassName(
            PAPARAZZI_SHOWKASE_ARTIFACT_PACKAGE_NAME,
            "PaparazziShowkaseUIMode"
        )
        private val LAYOUT_DIRECTION_CLASS_NAME = ClassName(
            "androidx.compose.ui.unit",
            "LayoutDirection"
        )
    }
}
