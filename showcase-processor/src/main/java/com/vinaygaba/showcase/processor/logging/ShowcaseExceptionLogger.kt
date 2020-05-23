package com.vinaygaba.showcase.processor.logging

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

internal class ShowcaseExceptionLogger {
    private val loggedExceptions: MutableList<Exception> = mutableListOf()
    
    internal fun logMessage(message: String) {
        logError(Exception(message))
    }

    private fun logError(e: Exception) {
        loggedExceptions += e
    }

    internal fun publishMessages(messager: Messager?) {
        loggedExceptions.forEach { messager?.printMessage(Diagnostic.Kind.ERROR, "${it.message}") }
    }
}
