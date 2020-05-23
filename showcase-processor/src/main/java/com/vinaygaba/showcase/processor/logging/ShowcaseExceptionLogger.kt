package com.vinaygaba.showcase.processor.logging

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

internal class ShowcaseExceptionLogger {
    private val loggedExceptions: MutableList<Exception> = mutableListOf()
    
    
    fun logMessage(message: String) {
        logError(Exception(message))
    }
    
    fun logError(e: Exception) {
        loggedExceptions += e
    }
    
    fun publishMessages(messager: Messager?) {
        loggedExceptions.forEach { messager?.printMessage(Diagnostic.Kind.ERROR, "${it.message}") }
    }
}
