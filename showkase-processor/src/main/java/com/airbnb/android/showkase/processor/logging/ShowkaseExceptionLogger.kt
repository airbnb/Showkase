package com.airbnb.android.showkase.processor.logging

import androidx.room.compiler.processing.XMessager
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
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

    internal fun publishMessages(messager: XMessager) {
        loggedExceptions.forEach {
            if (it is ShowkaseProcessorException && it.element != null) {
                messager.printMessage(Diagnostic.Kind.ERROR, "${it.message}", it.element)
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "${it.message}")
            }
        }
        loggedInfoMessage.forEach { messager.printMessage(Diagnostic.Kind.NOTE, it) }
    }
}
