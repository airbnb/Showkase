package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror

internal fun getFileBuilder(
    rootModulePackageName: String,
    showkaseComponentsListClassName: String
) = FileSpec.builder(
    rootModulePackageName,
    showkaseComponentsListClassName
)
    .addComment("This is an auto-generated file. Please do not edit/modify this file.")

internal fun getPropertyList(className: ClassName, propertyName: String): PropertySpec.Builder {
    val parameterizedList = className.getCodegenMetadataParameterizedList()
    
    return PropertySpec.builder(propertyName, parameterizedList)
}

internal fun getShowkaseProviderInterfaceFunction(
    methodName: String,
    returnPropertyName: String
) = FunSpec.builder(methodName)
    .addModifiers(KModifier.OVERRIDE)
    .addStatement("return $returnPropertyName")
    .build()

@Suppress("LongParameterList")
internal fun writeFile(
    processingEnv: ProcessingEnvironment,
    fileBuilder: FileSpec.Builder,
    superInterfaceClassName: ClassName,
    showkaseComponentsListClassName: String,
    componentListProperty: PropertySpec,
    colorListProperty: PropertySpec,
    typographyListProperty: PropertySpec,
    showkaseMetadata: Set<ShowkaseMetadata>,
    componentInterfaceFunction: FunSpec,
    colorInterfaceFunction: FunSpec,
    typographyInterfaceFunction: FunSpec
) {
    fileBuilder
        .addType(
            with(TypeSpec.classBuilder(showkaseComponentsListClassName)) {
                addSuperinterface(superInterfaceClassName)
                addFunction(componentInterfaceFunction)
                addFunction(colorInterfaceFunction)
                addFunction(typographyInterfaceFunction)
                addProperty(componentListProperty)
                addProperty(colorListProperty)
                addProperty(typographyListProperty)
                showkaseMetadata.forEach { addOriginatingElement(it.element) }
                build()
            }
        )

    fileBuilder.build().writeTo(processingEnv.filer)
}

internal fun ClassName.listInitializerCodeBlock(): CodeBlock.Builder {
    return CodeBlock.Builder()
        .add(
            "listOf<%T>(",
            this
        )
        .indent()
}

internal fun ClassName.mutableListInitializerCodeBlock(): CodeBlock.Builder {
    return CodeBlock.Builder()
        .add(
            "mutableListOf<%T>(",
            this
        )
        .indent()
}

internal fun ClassName.getCodegenMetadataParameterizedList() = List::class
    .asClassName()
    .parameterizedBy(this)

internal fun CodeBlock.Builder.closeOrContinueListCodeBlock(
    index: Int,
    finalIndex: Int
) {
    if (index == finalIndex) {
        add(")")
        addLineBreak()
    } else {
        add("),")
    }
}

internal fun CodeBlock.Builder.addShowkaseBrowserComponent(
    showkaseMetadata: ShowkaseMetadata.Component
) {
    add(
        "%T(\n",
        ShowkaseBrowserWriter.SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
    )
    doubleIndent()
    add(
        "group = %S,\ncomponentName = %S,\ncomponentKDoc = %S,",
        showkaseMetadata.showkaseGroup,
        showkaseMetadata.showkaseName,
        showkaseMetadata.showkaseKDoc
    )
    showkaseMetadata.apply {
        showkaseWidthDp?.let {
            add("\nwidthDp = %L,", it)
        }
        showkaseHeightDp?.let {
            add("\nheightDp = %L,", it)
        }
    }

    add(
        composePreviewFunctionLambda(
            showkaseMetadata.packageName,
            showkaseMetadata.enclosingClass,
            showkaseMetadata.elementName,
            showkaseMetadata.insideWrapperClass,
            showkaseMetadata.insideObject,
            showkaseMetadata.previewParameter
        )
    )
    doubleUnindent()
}

@Suppress("LongParameterList")
internal fun composePreviewFunctionLambda(
    functionPackageName: String,
    enclosingClass: TypeMirror? = null,
    composeFunctionName: String,
    insideWrapperClass: Boolean,
    insideObject: Boolean,
    previewParameter: TypeMirror? = null
): CodeBlock {
    return when {
        // When enclosingClass is null, it denotes that the method was a top-level method 
        // declaration.
        enclosingClass == null -> {
            val composableFunctionString = previewParameter?.let {
                "%M(previewParam)"
            } ?: "%M()"

            val composeMember = MemberName(functionPackageName, composeFunctionName)
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { $composableFunctionString }",
                    ShowkaseBrowserWriter.COMPOSE_CLASS_NAME, composeMember
                )
                .build()
        }
        // It was declared inside a class.
        insideWrapperClass -> {
            val composableFunctionString = previewParameter?.let {
                "${composeFunctionName}(previewParam)"
            } ?: "${composeFunctionName}()"
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { %T().${composableFunctionString} }",
                    ShowkaseBrowserWriter.COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            val composableFunctionString = previewParameter?.let {
                "${composeFunctionName}(previewParam)"
            } ?: "${composeFunctionName}()"
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T { %T.${composableFunctionString} }",
                    ShowkaseBrowserWriter.COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
        else -> throw ShowkaseProcessorException(
            "Your @ShowkaseComposable/@Preview " +
                    "function:${composeFunctionName} is declared in a way that is not supported by " +
                    "Showkase"
        )
    }
}

internal fun CodeBlock.Builder.doubleIndent() = indent().indent()

internal fun CodeBlock.Builder.doubleUnindent() = unindent().unindent()

internal fun CodeBlock.Builder.closeRoundBracket() = addLineBreak().add(")")

internal fun CodeBlock.Builder.closeCurlyBraces() = addLineBreak().add("}")

internal fun CodeBlock.Builder.addLineBreak() = add("\n")
