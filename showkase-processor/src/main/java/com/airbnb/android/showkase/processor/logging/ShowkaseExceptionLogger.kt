package com.airbnb.android.showkase.processor.logging

import androidx.room.compiler.processing.XMessager
import com.airbnb.android.showkase.processor.exceptions.ShowkaseProcessorException
import javax.tools.Diagnostic

internal class ShowkaseExceptionLogger {
    private val loggedExceptions: MutableList<ShowkaseProcessorException> = mutableListOf()
    private val loggedInfoMessage: MutableList<String> = mutableListOf()

    internal fun logError(exception: ShowkaseProcessorException) {
        loggedExceptions.add(exception)
    }

    internal fun logInfoMessage(message: String) {
        loggedInfoMessage += message
    }

    internal fun publishMessages(messager: XMessager) {
        loggedExceptions.forEach { exception ->
            val elements = exception.elements.ifEmpty { listOf(null) }

            elements.forEach { element ->
                messager.printMessage(
                    kind = Diagnostic.Kind.ERROR,
                    msg = exception.message,
                    element = element
                )
            }
        }

        loggedInfoMessage.forEach { messager.printMessage(Diagnostic.Kind.NOTE, it) }
    }
}
