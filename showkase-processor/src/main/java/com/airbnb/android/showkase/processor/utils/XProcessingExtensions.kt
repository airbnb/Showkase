package com.airbnb.android.showkase.processor.utils

import androidx.room.compiler.processing.XAnnotated
import androidx.room.compiler.processing.XAnnotation
import androidx.room.compiler.processing.XElement
import androidx.room.compiler.processing.isMethod
import androidx.room.compiler.processing.isTypeElement

fun XAnnotated.findAnnotationBySimpleName(simpleName: String): XAnnotation? {
    return getAllAnnotations().find { it.name == simpleName }
}

fun XAnnotated.requireAnnotationBySimpleName(simpleName: String): List<XAnnotation> {
    return getAllAnnotations().filter { it.name == simpleName }
}

/**
 * The order of symbols returns by KSP2 differs from that returned by KSP1.
 * This workaround ensure that the order of symbols is consistent across both KSP versions.
 *
 * @see [https://github.com/google/ksp/issues/1719]
 * */
internal fun <T : XElement> Collection<T>.ensureConsistentOrdering(): Sequence<T> {
    return this.asSequence()
        .sortedWith(
            compareBy { element ->
                when {
                    element.isTypeElement() -> 0
                    element.isMethod() -> 1
                    else -> 2
                }
            }
        )
}
