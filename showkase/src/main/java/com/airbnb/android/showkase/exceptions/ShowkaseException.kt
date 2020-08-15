package com.airbnb.android.showkase.exceptions

import java.lang.Exception

/**
 * Used to throw an exception for Showkase specific errors. 
 */
internal class ShowkaseException(message: String): Exception(message)
