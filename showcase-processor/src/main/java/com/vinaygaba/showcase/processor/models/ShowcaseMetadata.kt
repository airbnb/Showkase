package com.vinaygaba.showcase.processor.models

import javax.lang.model.element.ExecutableElement

internal data class ShowcaseMetadata(
    val methodElement: ExecutableElement,
    val methodName: String,
    val packageName: String,
    val name: String,
    val group: String
)
