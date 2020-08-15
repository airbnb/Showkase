package com.airbnb.android.showkase.processor.logging

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

internal class ShowkaseExceptionLogger {
    private val loggedExceptions: MutableList<Exception> = mutableListOf()
    private val loggedInfoMessage: MutableList<String> = mutableListOf()
    
    internal fun logErrorMessage(message: String) {
        logError(Exception(message))
    }

    internal fun logInfoMessage(message: String) {
        loggedInfoMessage += message
    }

    private fun logError(e: Exception) {
        loggedExceptions += e
    }

    internal fun publishMessages(messager: Messager?) {
        loggedExceptions.forEach { messager?.printMessage(Diagnostic.Kind.ERROR, "${it.message}") }
        loggedInfoMessage.forEach { messager?.printMessage(Diagnostic.Kind.NOTE, "$it") }
    }
}
