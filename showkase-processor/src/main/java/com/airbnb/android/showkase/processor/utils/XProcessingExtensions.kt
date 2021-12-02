package com.airbnb.android.showkase.processor.utils

import androidx.room.compiler.processing.XAnnotated
import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XElement
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException

fun XAnnotated.findAnnotationBySimpleName(simpleName: String): XAnnotation? {
    return getAllAnnotations().find { it.name == simpleName }
}

fun XAnnotated.requireAnnotationBySimpleName(simpleName: String): XAnnotation {
    return getAllAnnotations().find { it.name == simpleName }
        ?: throw ShowkaseProcessorException(
            "No annotation named $simpleName found",
            this as? XElement
        )
}