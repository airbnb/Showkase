package com.airbnb.android.showkase.processor.writer

import androidx.room.compiler.processing.XFiler
import androidx.room.compiler.processing.XProcessingEnv
import androidx.room.compiler.processing.addOriginatingElement
import androidx.room.compiler.processing.writeTo
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

val SPACE_REGEX = "\\s".toRegex()

internal fun getFileBuilder(
    rootModulePackageName: String,
    showkaseComponentsListClassName: String
) = FileSpec.builder(
    rootModulePackageName,
    showkaseComponentsListClassName
)
    .addFileComment("This is an auto-generated file. Please do not edit/modify this file.")

internal fun getPropertyList(className: ClassName, propertyName: String): PropertySpec.Builder {
    val parameterizedList = className.getCodegenMetadataParameterizedList()

    return PropertySpec.builder(propertyName, parameterizedList)
}

internal fun getShowkaseProviderInterfaceFunction(
    methodName: String,
    returnPropertyName: String,
    returnType: TypeName,
) = FunSpec.builder(methodName)
    .addModifiers(KModifier.OVERRIDE)
    .addStatement("return $returnPropertyName")
    .returns(returnType)
    .build()

@Suppress("LongParameterList")
internal fun writeFile(
    processingEnv: XProcessingEnv,
    fileBuilder: FileSpec.Builder,
    superInterfaceClassName: ClassName,
    showkaseComponentsListClassName: String,
    componentListProperty: PropertySpec,
    colorListProperty: PropertySpec,
    typographyListProperty: PropertySpec,
    showkaseMetadata: Set<ShowkaseMetadata>,
    componentInterfaceFunction: FunSpec,
    colorInterfaceFunction: FunSpec,
    typographyInterfaceFunction: FunSpec,
    showkaseRootCodegenAnnotation: AnnotationSpec
) {
    fileBuilder
        .addType(
            with(TypeSpec.classBuilder(showkaseComponentsListClassName)) {
                addAnnotation(showkaseRootCodegenAnnotation)
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

    fileBuilder.build().writeTo(processingEnv.filer, mode = XFiler.Mode.Aggregating)
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
    showkaseMetadata: ShowkaseMetadata.Component,
    isPreviewParameter: Boolean = false
) {
    val componentName = if (showkaseMetadata.componentIndex != null) {
        "_${showkaseMetadata.showkaseName}_${showkaseMetadata.componentIndex}"
    } else {
        "_${showkaseMetadata.showkaseName}"
    }
    var componentKey = (showkaseMetadata.packageName +
            "_${showkaseMetadata.enclosingClassName}" +
            "_${showkaseMetadata.showkaseGroup}" +
            componentName +
            "_${showkaseMetadata.showkaseStyleName}").replace(
        SPACE_REGEX,
        ""
    )
    if (isPreviewParameter) {
        componentKey += "_\$index"
    }
    add(
        "%T(\n",
        ShowkaseBrowserWriter.SHOWKASE_BROWSER_COMPONENT_CLASS_NAME
    )
    doubleIndent()
    add(
        "group = %S,\ncomponentName = %S,\ncomponentKDoc = %S,\ncomponentKey = %P,",
        showkaseMetadata.showkaseGroup,
        showkaseMetadata.showkaseName,
        showkaseMetadata.showkaseKDoc,
        componentKey,
    )
    if (isPreviewParameter) {
        add("\npreviewParam = previewParam,")
    }
    add("\nisDefaultStyle = ${showkaseMetadata.isDefaultStyle},")
    showkaseMetadata.apply {
        showkaseWidthDp?.let { add("\nwidthDp = %L,", it) }
        showkaseHeightDp?.let { add("\nheightDp = %L,", it) }
        showkaseStyleName?.let { add("\nstyleName = %S,", it) }
    }

    add(
        composePreviewFunctionLambdaCodeBlock(
            showkaseMetadata.packageName,
            showkaseMetadata.enclosingClassName,
            showkaseMetadata.elementName,
            showkaseMetadata.insideWrapperClass,
            showkaseMetadata.insideObject,
            showkaseMetadata.previewParameterProviderType,
            showkaseMetadata.previewParameterName
        )
    )
    doubleUnindent()
}

@Suppress("LongParameterList")
internal fun composePreviewFunctionLambdaCodeBlock(
    functionPackageName: String,
    enclosingClass: ClassName? = null,
    composeFunctionName: String,
    insideWrapperClass: Boolean,
    insideObject: Boolean,
    previewParameter: TypeName? = null,
    previewParameterName: String? = null
): CodeBlock {
    return when {
        // When enclosingClass is null, it denotes that the method was a top-level method 
        // declaration.
        enclosingClass == null -> {
            val composableFunctionString = previewParameter?.let {
                "%M($previewParameterName = previewParam)"
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
                "${composeFunctionName}($previewParameterName = previewParam)"
            } ?: "${composeFunctionName}()"
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T {\n    %T().${composableFunctionString}\n}",
                    ShowkaseBrowserWriter.COMPOSE_CLASS_NAME, enclosingClass
                )
                .build()
        }
        // It was declared inside an object or a companion object.
        insideObject -> {
            val composableFunctionString = previewParameter?.let {
                "${composeFunctionName}($previewParameterName = previewParam)"
            } ?: "${composeFunctionName}()"
            CodeBlock.Builder()
                .add(
                    "\ncomponent = @%T {\n    %T.${composableFunctionString}\n}",
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

internal fun CodeBlock.Builder.withDoubleIndent(block: CodeBlock.Builder.() -> Unit) =
    doubleIndent().also(block).doubleUnindent()

internal fun CodeBlock.Builder.doubleIndent() = indent().indent()

internal fun CodeBlock.Builder.doubleUnindent() = unindent().unindent()

internal fun CodeBlock.Builder.closeRoundBracket() = addLineBreak().add(")")

internal fun CodeBlock.Builder.closeCurlyBraces() = addLineBreak().add("}")

internal fun CodeBlock.Builder.addLineBreak() = add("\n")
