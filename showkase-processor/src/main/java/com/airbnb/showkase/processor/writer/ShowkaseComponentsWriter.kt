package com.airbnb.showkase.processor.writer

import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowkaseComponentsWriter(private val processingEnv: ProcessingEnvironment) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserComponents(
        showkaseMetadataList: List<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        if (showkaseMetadataList.isEmpty()) return
        val showkaseComponentsListClassName = "$rootModuleClassName$CODEGEN_AUTOGEN_CLASS_NAME"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)

        // val componentList: List<ShowkaseCodegenMetadata>
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
                showkaseMetadata.showkaseWidthDp?.let {
                    add("\nwidthDp = %L,", it)
                }
                showkaseMetadata.showkaseHeightDp?.let { 
                    add("\nheightDp = %L,", it)
                }
                add(
                    composePreviewFunctionLambda(
                        showkaseMetadata.packageName,
                        showkaseMetadata.enclosingClass,
                        showkaseMetadata.showkaseElementName,
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

        writeFile(
            processingEnv,
            fileBuilder,
            SHOWKASE_COMPONENTS_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            componentListProperty.build(),
            showkaseMetadataList,
            getShowkaseProviderInterfaceFunction(
                COMPONENT_INTERFACE_METHOD_NAME,
                COMPONENT_PROPERTY_NAME
            )
        )
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

    companion object {
        private const val CODEGEN_AUTOGEN_CLASS_NAME = "CodegenComponents"
        private const val COMPONENT_PROPERTY_NAME = "componentList"
        private const val COMPONENT_INTERFACE_METHOD_NAME = "getShowkaseComponents"
        internal const val SHOWKASE_MODELS_PACKAGE_NAME = "com.airbnb.showkase.models"

        val COMPOSE_CLASS_NAME = ClassName("androidx.compose.runtime", "Composable")
        val SHOWKASE_BROWSER_COMPONENT_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserComponent")
        val SHOWKASE_COMPONENTS_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseComponentsProvider")
    }
}
