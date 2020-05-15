package com.vinaygaba.codegen.writer

import com.squareup.kotlinpoet.*
import com.vinaygaba.annotation.models.ShowcaseMetadata
import com.vinaygaba.codegen.exceptions.ShowcaseProcessorException
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class KotlinComposableWriter(private val processingEnv: ProcessingEnvironment) {
    
    fun generateShowcaseBrowserComponents(showcaseMetadataMap: Map<String, List<ShowcaseMetadata>>) {
        if (showcaseMetadataMap.isEmpty()) return
        val kaptKotlinDirPath = processingEnv.options[KAPT_KOTLIN_DIR_PATH] ?: throw ShowcaseProcessorException("Exception encountered")
        val fileBuilder = FileSpec.builder("com.vinaygaba.showcase", FILE_NAME)
            .addComment("This is an auto-generated file. Please do not edit/modify this file.")

        // @Composable
        val composableAnnotation = AnnotationSpec.builder(COMPOSE_CLASS_NAME).build()
        
        // @Composable () -> Unit
        val composablePredicate = LambdaTypeName.get(returnType = UNIT)
            .copy(annotations = arrayListOf(composableAnnotation))

        // List<@Composable () -> Unit>
        val composableParameterizedList = List::class.asClassName().parameterizedBy(composablePredicate)
        
        // Map<String, List<@Composable () -> Unit>>
        val mapType = Map::class.asClassName().parameterizedBy(String::class.asTypeName(), composableParameterizedList)

        // val componentsMap: Map<String, List<@Composable () -> Unit>>
        val componentMapProperty = PropertySpec.builder("composableMap", mapType)
        
        // mutableMapOf<String, List<@Composable @Composable () -> Unit>>(
        val mapInitializerCodeBlock = CodeBlock.Builder()
            .add(
                "mutableMapOf<%T, %T>(\n", String::class.asTypeName(), composableParameterizedList
            )
            .indent()

        showcaseMetadataMap.toList().forEachIndexed { mapIndex, (group, componentsList) ->
            // "String" to listOf<@Composable () -> Unit>(
            mapInitializerCodeBlock
                .add(
                    "%S to listOf<%T>(", group, composablePredicate
                )
            componentsList.forEachIndexed { index, listItem ->
                val composableLambdaCodeBlock = composePreviewFunctionLambda(listItem.packageName, listItem.methodName)
                // @Composable { MethodName() }
                mapInitializerCodeBlock.add(composableLambdaCodeBlock)
                if (index == componentsList.lastIndex) {
                    mapInitializerCodeBlock.addStatement(")")
                } else {
                    mapInitializerCodeBlock.add(",")
                }
            }
            if (mapIndex != showcaseMetadataMap.size - 1) {
                mapInitializerCodeBlock.add(",")
            }
        }

        mapInitializerCodeBlock.addStatement(")")
        
        componentMapProperty.initializer(
            mapInitializerCodeBlock.build()
        )
        
        fileBuilder
            .addType(TypeSpec.classBuilder("ShowcaseComponents")
                .addProperty(componentMapProperty.build())
                .build())
        
        fileBuilder.build().writeTo(File(kaptKotlinDirPath))
    }
    
    fun composePreviewFunctionLambda(functionPackageName: String, composeFunctionName: String): CodeBlock {
        val composeMember = MemberName(functionPackageName, composeFunctionName)
        return CodeBlock.Builder()
            .add("@%T { %M() }", COMPOSE_CLASS_NAME, composeMember)
            .build()
    }
    
    companion object {
        const val FILE_NAME = "ShowcaseComposables"
        // https://github.com/Kotlin/kotlin-examples/blob/master/gradle/kotlin-code-generation/
        // annotation-processor/src/main/java/TestAnnotationProcessor.kt
        const val KAPT_KOTLIN_DIR_PATH = "kapt.kotlin.generated"
        
        val COMPOSE_CLASS_NAME = ClassName("androidx.compose", "Composable")
    }
}
