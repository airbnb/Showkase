package com.airbnb.android.showkase.processor.writer

import com.airbnb.android.showkase.processor.models.ShowkaseMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment

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

internal fun ClassName.getCodegenMetadataParameterizedList() = List::class
    .asClassName()
    .parameterizedBy(this)

internal fun CodeBlock.Builder.closeOrContinueListCodeBlock(
    index: Int,
    finalIndex: Int
) {
    if (index == finalIndex) {
        add(")\n")
    } else {
        add("),")
    }
}

internal fun CodeBlock.Builder.doubleIndent() = indent().indent()

internal fun CodeBlock.Builder.doubleUnindent() = unindent().unindent()

