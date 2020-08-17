package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowkaseBrowserWriter(private val processingEnv: ProcessingEnvironment) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserFile(
        showkaseComponentMetadataList: List<ShowkaseMetadata>,
        showkaseColorMetadataList: List<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        val showkaseComponentsListClassName = "$rootModuleClassName$CODEGEN_AUTOGEN_CLASS_NAME"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)

        val componentListProperty = initializeComponentProperty(showkaseComponentMetadataList)
        val colorListProperty = initializeColorProperty(showkaseColorMetadataList)

        writeFile(
            processingEnv,
            fileBuilder,
            SHOWKASE_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            componentListProperty.build(),
            colorListProperty.build(),
            showkaseComponentMetadataList + showkaseColorMetadataList,
            getShowkaseProviderInterfaceFunction(
                COMPONENT_INTERFACE_METHOD_NAME,
                COMPONENT_PROPERTY_NAME
            ),
            getShowkaseProviderInterfaceFunction(
                COLOR_INTERFACE_METHOD_NAME,
                COLOR_PROPERTY_NAME
            )
        )
    }

    private fun initializeComponentProperty(
        showkaseMetadataList: List<ShowkaseMetadata>
    ): PropertySpec.Builder {
        val componentListProperty =
            getPropertyList(SHOWKASE_BROWSER_COMPONENT_CLASS_NAME, COMPONENT_PROPERTY_NAME)

        val componentListInitializerCodeBlock =
            SHOWKASE_BROWSER_COMPONENT_CLASS_NAME.listInitializerCodeBlock()

        showkaseMetadataList.forEachIndexed { index, showkaseMetadata ->
            componentListInitializerCodeBlock.apply {
                add("\n")
                add(
                    "%T(\n",
                    SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
                )
                doubleIndent()
                add(
                    "group = %S,\ncomponentName = %S,\ncomponentKDoc = %S,",
                    showkaseMetadata.showkaseGroup,
                    showkaseMetadata.showkaseName,
                    showkaseMetadata.showkaseKDoc
                )
                if (showkaseMetadata is ShowkaseMetadata.Component) {
                    showkaseMetadata.apply {
                        showkaseWidthDp?.let {
                            add("\nwidthDp = %L,", it)
                        }
                        showkaseHeightDp?.let {
                            add("\nheightDp = %L,", it)
                        }
                    }
                }
                
                add(
                    composePreviewFunctionLambda(
                        showkaseMetadata.packageName,
                        showkaseMetadata.enclosingClass,
                        showkaseMetadata.elementName,
                        showkaseMetadata.insideWrapperClass,
                        showkaseMetadata.insideObject
                    )
                )
                doubleUnindent()
                closeOrContinueListCodeBlock(index, showkaseMetadataList.lastIndex)
            }
        }
        componentListInitializerCodeBlock.apply {
            unindent()
            add(")")
        }
        componentListProperty.initializer(componentListInitializerCodeBlock.build())
        return componentListProperty
    }

    private fun initializeColorProperty(
        showkaseMetadataList: List<ShowkaseMetadata>
    ): PropertySpec.Builder {
        val colorListProperty =
            getPropertyList(
                SHOWKASE_BROWSER_COLOR_CLASS_NAME,
                COLOR_PROPERTY_NAME
            )

        val colorListInitializerCodeBlock =
            SHOWKASE_BROWSER_COLOR_CLASS_NAME.listInitializerCodeBlock()

        showkaseMetadataList.forEachIndexed { index, showkaseMetadata ->
            colorListInitializerCodeBlock.apply {
                add("\n")
                add(
                    "%T(\n",
                    SHOWKASE_BROWSER_COLOR_CLASS_NAME
                )
                doubleIndent()
                add(
                    "colorGroup = %S,\ncolorName = %S,\ncolorKDoc = %S,",
                    showkaseMetadata.showkaseGroup,
                    showkaseMetadata.showkaseName,
                    showkaseMetadata.showkaseKDoc
                )
                add(
                    colorPropertyValue(
                        showkaseMetadata.packageName,
                        showkaseMetadata.enclosingClass,
                        showkaseMetadata.elementName,
                        showkaseMetadata.insideWrapperClass,
                        showkaseMetadata.insideObject
                    )
                )
                doubleUnindent()
                closeOrContinueListCodeBlock(index, showkaseMetadataList.lastIndex)
            }
        }
        colorListInitializerCodeBlock.apply {
            unindent()
            add(")")
        }
        colorListProperty.initializer(colorListInitializerCodeBlock.build())
        return colorListProperty
    }

    private fun composePreviewFunctionLambda(
        functionPackageName: String,
        enclosingClass: TypeMirror? = null,
        composeFunctionName: String,
        insideWrapperClass: Boolean,
        insideObject: Boolean
    ) = when {
        // When enclosingClass is null, it denotes that the method was a top-level method 
        // declaration.
        enclosingClass == null -> {
            val composeMember = MemberName(functionPackageName, composeFunctionName)
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { %M() }",
                    COMPOSE_CLASS_NAME, composeMember
                )
                .build()
        }
        // It was declared inside a class.
        insideWrapperClass -> {
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { %T().${composeFunctionName}() }",
                    COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { %T.${composeFunctionName}() }",
                    COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
        else -> throw ShowkaseProcessorException("Your @Showkase/@Preview " +
                "function:${composeFunctionName} is declared in a way that is not supported by " +
                "Showkase")
    }

    private fun colorPropertyValue(
        functionPackageName: String,
        enclosingClass: TypeMirror? = null,
        colorFieldName: String,
        insideWrapperClass: Boolean,
        insideObject: Boolean
    ) = when {
        // When enclosingClass is null, it denotes that the method was a top-level method 
        // declaration.
        enclosingClass == null -> {
            val composeMember = MemberName(functionPackageName, colorFieldName)
            CodeBlock.Builder()
                .add("\ncolor = %M", composeMember)
                .build()
        }
        // It was declared inside a class.
        insideWrapperClass -> {
            CodeBlock.Builder()
                .add("\ncolor = %T().${colorFieldName}", enclosingClass)
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            CodeBlock.Builder()
                .add("\ncolor = %T.${colorFieldName}", enclosingClass)
                .build()
        }
        else -> throw ShowkaseProcessorException("Your @ShowkaseColor " +
                "field:${colorFieldName} is declared in a way that is not supported by " +
                "Showkase")
    }

    companion object {
        private const val CODEGEN_AUTOGEN_CLASS_NAME = "Codegen"
        private const val COMPONENT_PROPERTY_NAME = "componentList"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"
        internal const val SHOWKASE_MODELS_PACKAGE_NAME = "com.airbnb.android.showkase.models"
        private const val COLOR_PROPERTY_NAME = "colorList"
        private const val COLOR_INTERFACE_METHOD_NAME = "getShowkaseColors"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose.runtime", "Composable")
        val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        val SHOWKASE_BROWSER_COLOR_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserColor")
        val SHOWKASE_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseProvider")
    }
}
