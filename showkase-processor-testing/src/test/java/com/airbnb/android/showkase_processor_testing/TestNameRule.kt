@file:Suppress("PackageName")
package com.airbnb.android.showkase_processor_testing

import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Similar to the rule Junit provides, but this one also holds class name.
 */
class TestNameRule : TestWatcher() {
    /**
     * @return the name of the currently-running test method
     */
    lateinit var methodName: String
        private set

    lateinit var className: String

    override fun starting(d: Description) {
        methodName = d.methodName
        className = d.className
    }
}
