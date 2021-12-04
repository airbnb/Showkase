package com.airbnb.android.showkase.processor.utils

/**
 * Easy way to retrieve the value of a field via reflection.
 *
 * @param fieldName Name of the field on this class
 * @param U The type of the field..
 */
inline fun <reified U> Any.getFieldWithReflection(fieldName: String): U {

    val field = (javaClass.fields + javaClass.declaredFields).firstOrNull {
        it.name == fieldName
    }

    val value = if (field != null) {
        field.isAccessible = true
        field.get(this)
    } else {
        val methodName = "get${fieldName.capitalize()}"
        // Kotlin sometimes does not have a field backing a property, so we try a getter method
        // for it.
        val method = (javaClass.methods + javaClass.declaredMethods).firstOrNull {
            it.name == methodName
        }

        if (method != null) {
            method.isAccessible = true
            method.invoke(this)
        } else {
            error("Field named $fieldName not found on $javaClass")
        }
    }

    check(value is U) {
        "Expected field '$fieldName' to be ${U::class.java.simpleName} but got a ${value.javaClass.simpleName}"
    }
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return value as U
}

inline fun <reified U> Any.getFieldWithReflectionOrNull(fieldName: String): U? {
    return kotlin.runCatching<U> {
        getFieldWithReflection(fieldName)
    }.getOrNull()
}
