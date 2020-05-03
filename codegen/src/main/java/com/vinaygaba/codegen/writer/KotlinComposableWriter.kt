package com.vinaygaba.codegen.writer

import com.squareup.kotlinpoet.*
import com.vinaygaba.annotation.models.ShowcaseMetadata
import com.vinaygaba.codegen.exceptions.ShowcaseProcessorException
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class KotlinComposableWriter(private val processingEnv: ProcessingEnvironment) {
    
    fun generateShowcaseBrowserComponents(showcaseMetadataMap: MutableMap<ExecutableElement, ShowcaseMetadata>) {
        val kaptKotlinDirPath = processingEnv.options[KAPT_KOTLIN_DIR_PATH] ?: throw ShowcaseProcessorException("Exception encountered")
        val fileBuilder = FileSpec.builder("", FILE_NAME)
        
        val composableClassName = ClassName("androidx.compose", "Composable")
        val composableAnnotation = AnnotationSpec.builder(composableClassName).build()
        
        val composablePredicate = LambdaTypeName.get(returnType = UNIT)
//            .copy(annotations = arrayListOf(composableAnnotation))
        
//        val composableList = List::class.asClassName().parameterizedBy(composablePredicate)
//        val mapType = Map::class.asClassName().parameterizedBy(String::class.asTypeName(), composableList)
//        val componentMapProperty = PropertySpec.builder("componentMap", composablePredicate)
////            .build()
        
        if (showcaseMetadataMap.isEmpty()) return
        val first = showcaseMetadataMap.takeIf { true }?.values?.first() ?: return
        val composeMember = MemberName(first.packageName, first.methodName)
        val codeBlock = CodeBlock.Builder()
            .add("@%T{%M()}", composableClassName, composeMember)
            .build()
        
        val property = PropertySpec.builder("componentMap", composablePredicate)
            .initializer(
                codeBlock
            )
            .build()
//        val method = FunSpec.builder("preview")
//            .addAnnotation(composableAnnotation)
//            .addStatement("%M()", composeMember)
//            .build()
        
        fileBuilder
            .addProperty(property)
//            .addFunction(method)
        
        fileBuilder.build().writeTo(File(kaptKotlinDirPath))
    }
    
    companion object {
        const val FILE_NAME = "ShowcaseBrowserComponents"
        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
    }
}
