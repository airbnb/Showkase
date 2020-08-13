package com.airbnb.showkase.processor.writer

import com.airbnb.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.showkase.processor.models.ShowkaseMetadata
import com.airbnb.showkase.processor.writer.ShowkaseComponentsWriter.Companion.SHOWKASE_MODELS_PACKAGE_NAME
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal class ShowkaseColorsWriter(private val processingEnv: ProcessingEnvironment) {
    @Suppress("LongMethod")
    internal fun generateShowkaseBrowserColors(
        showkaseMetadataList: List<ShowkaseMetadata>,
        rootModulePackageName: String,
        rootModuleClassName: String
    ) {
        if (showkaseMetadataList.isEmpty()) return
        val showkaseComponentsListClassName = "$rootModuleClassName${COLORS_AUTOGEN_CLASS_NAME}"
        val fileBuilder = getFileBuilder(rootModulePackageName, showkaseComponentsListClassName)

        // val componentList: List<ShowkaseCodegenMetadata>
        val colorListProperty = 
            getPropertyList(SHOWKASE_BROWSER_COLOR_CLASS_NAME, COLOR_PROPERTY_NAME)

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
                        showkaseMetadata.showkaseElementName,
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

        writeFile(
            processingEnv,
            fileBuilder,
            SHOWKASE_COLORS_PROVIDER_CLASS_NAME,
            showkaseComponentsListClassName,
            colorListProperty.build(),
            showkaseMetadataList,
            getShowkaseProviderInterfaceFunction(COLOR_INTERFACE_METHOD_NAME, COLOR_PROPERTY_NAME)
        )
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
        private const val COLORS_AUTOGEN_CLASS_NAME = "CodegenColors"
        private const val COLOR_PROPERTY_NAME = "colorList"
        private const val COLOR_INTERFACE_METHOD_NAME = "getShowkaseColors"
        val SHOWKASE_BROWSER_COLOR_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseBrowserColor")
        val SHOWKASE_COLORS_PROVIDER_CLASS_NAME =
            ClassName(SHOWKASE_MODELS_PACKAGE_NAME, "ShowkaseColorsProvider")
    }
}
