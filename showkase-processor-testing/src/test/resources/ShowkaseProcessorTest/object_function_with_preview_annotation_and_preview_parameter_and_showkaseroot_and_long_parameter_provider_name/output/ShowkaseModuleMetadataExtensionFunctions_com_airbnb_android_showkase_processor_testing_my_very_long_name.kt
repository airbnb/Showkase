// This is an auto-generated file. Please do not edit/modify this file.This is an auto-generated file. Please do not edit/modify this file.
package com.airbnb.android.showkase_processor_testing_my_very_long_name

import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
import com.airbnb.android.showkase.models.ShowkaseProvider

/**
 * Helper function that gives you access to Showkase elements that are declared in a given module.
 * This contains data about the composables, colors and typography that are meant to be rendered inside
 * the Showkase browser. This is different from the Showkase.getMetadata() function, which contains all
 * the Showkase elements in a given ShowkaseRoot graph, whereas this function only contains metadata
 * about the module it's generated in. Each module where Showkase is setup will have this function
 * generated in it.
 */
public fun Showkase.getModuleMetadata(): ShowkaseElementsMetadata =
    (ShowkaseModuleMetadata_com_airbnb_android_showkase_processor_testing_my_very_long_name() as
    ShowkaseProvider).metadata()
