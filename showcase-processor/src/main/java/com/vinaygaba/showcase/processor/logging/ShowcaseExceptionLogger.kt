package com.vinaygaba.showcase.processor.logging

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

internal class ShowcaseExceptionLogger {
    private val loggedExceptions: MutableList<Exception> = mutableListOf()
    private val loggedInfo: MutableList<String> = mutableListOf()
    
    internal fun logMessage(message: String) {
        logError(Exception(message))
    }

    internal fun logInfo(message: String) {
        loggedInfo += message
    }

    private fun logError(e: Exception) {
        loggedExceptions += e
    }

    internal fun publishMessages(messager: Messager?) {
        loggedExceptions.forEach { messager?.printMessage(Diagnostic.Kind.ERROR, "${it.message}") }
        loggedInfo.forEach { messager?.printMessage(Diagnostic.Kind.NOTE, "$it") }
    }
}
