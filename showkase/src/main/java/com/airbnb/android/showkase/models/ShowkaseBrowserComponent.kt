package com.airbnb.android.showkase.models

import androidx.compose.runtime.Composable

// TODO(vinaygaba): Move it to a different module that has Android/Compose dependencies hoooked up. 
// This was added here only because this module has compose dependencies.
data class ShowkaseBrowserComponent(
    val componentKey: String,
    val group: String,
    val componentName: String,
    val componentKDoc: String,
    val component: @Composable () -> Unit,
    val styleName: String? = null,
    val isDefaultStyle: Boolean = false,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
)

sealed interface BaseClass {
    val componentKey: String,
    val group: String,
    val componentName: String,
    val componentKDoc: String,
    val component: @Composable () -> Unit,
    val styleName: String? = null,
    val isDefaultStyle: Boolean = false,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
}

data class Child1(
    override val componentKey: String,
    override val group: String,
    override val componentName: String,
    override val componentKDoc: String,
    override val component: @Composable () -> Unit,
    override val styleName: String? = null,
    override val isDefaultStyle: Boolean = false,
    override val widthDp: Int? = null,
    override val heightDp: Int? = null,
) : BaseClass

data class Child2(
    override val componentKey: String,
    override val group: String,
    override val componentName: String,
    override val componentKDoc: String,
    override val component: @Composable () -> Unit,
    override val styleName: String? = null,
    override val isDefaultStyle: Boolean = false,
    override val widthDp: Int? = null,
    override val heightDp: Int? = null,
) : BaseClass


data class ShowkaseBrowserComponent2<T>(
    val componentKey: String,
    val group: String,
    val componentName: String,
    val componentKDoc: String,
    val component: @Composable () -> Unit,
    val previewParameter: T? = null,
    val styleName: String? = null,
    val isDefaultStyle: Boolean = false,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
) {
    inline fun <reified R : T> retrieveKClass(): String? {
        return R::class.qualifiedName
    }
//    fun <reified T> retrieveClass():T {
//       previewParameter::class.qualifiedName
//    }
}
