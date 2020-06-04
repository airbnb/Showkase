package com.vinaygaba.showcase.processor.models

import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

internal data class ShowcaseMetadata(
    val methodElement: ExecutableElement,
    val methodName: String,
    val enclosingClass: TypeMirror? = null,
    val packageName: String,
    val name: String,
    val group: String,
    val widthDp: Int,
    val heightDp: Int
)
