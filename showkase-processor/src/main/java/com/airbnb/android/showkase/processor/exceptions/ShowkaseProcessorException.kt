package com.airbnb.android.showkase.processor.exceptions

import androidx.room.compiler.processing.XElement

internal class ShowkaseProcessorException(message: String, val element: XElement? = null) : Exception(message)
