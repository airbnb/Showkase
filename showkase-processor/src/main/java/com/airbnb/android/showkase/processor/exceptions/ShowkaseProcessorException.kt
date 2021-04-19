package com.airbnb.android.showkase.processor.exceptions

import androidx.room.compiler.processing.XElement
import java.lang.Exception

internal class ShowkaseProcessorException(
    override val message: String,
    val elements: Collection<XElement>
) : Exception(message) {
    constructor(message: String, element: XElement) : this(message, listOf(element))
}
