package com.vinaygaba.annotation.models

import javax.lang.model.element.ExecutableElement

data class ShowcaseMetadata(
    val methodElement: ExecutableElement,
    val methodName: String,
    val packageName: String,
    val name: String,
    val group: String
)
