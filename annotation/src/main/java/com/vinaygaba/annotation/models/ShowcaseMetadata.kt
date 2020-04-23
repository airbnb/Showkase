package com.vinaygaba.annotation.models

import javax.lang.model.element.Element

data class ShowcaseMetadata(
    val name: String,
    val group: String,
    val apiLevel: Int,
    val theme: String,
    // TODO(mount): Make this Dp when they are inline classes
    val widthDp: Int,
    // TODO(mount): Make this Dp when they are inline classes
    val heightDp: Int,
    val locale: String,
    val fontScale: Float = 1f,
    val showDecoration: Boolean = false,
    val showBackground: Boolean = false
) {
   
    companion object {
        fun getShowcaseMetadata(element: Element): ShowcaseMetadata {
            
        }
    }
}
